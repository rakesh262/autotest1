package sqs.core.utils;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import sqs.framework.FrameworkBase;

public class RetryRule implements TestRule {

    private int retryCount;
 
    public RetryRule (int retryCount) {
        this.retryCount = retryCount;
    }
 
    public Statement apply(Statement base, Description description) {
        return statement(base, description);
    }
 
    private Statement statement(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Throwable caughtThrowable = null;
                try {
                    for (int i = 0; i < retryCount; i++) {
                        caughtThrowable =getEvaluate(i);
                    }
                    FrameworkBase.logger.debug(description.getDisplayName() + ": giving up after " + retryCount + " failures.");
                    if (caughtThrowable!=null)throw caughtThrowable;
                }catch (Exception exception){
                    FrameworkBase.logger.error( "Exception on RetryRule.statement :",exception);
                }
            }

            private Throwable getEvaluate(int i) {
                Throwable throwable= null;
                try {
                    base.evaluate();
                    return throwable;
                } catch (Throwable t) {
                    FrameworkBase.logger.error(description.getDisplayName() + ": run " + (i + 1) + " failed.");
                    return t;
                }
            }
        };
    }
}