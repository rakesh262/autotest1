package sqs.core.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import cucumber.api.junit.Cucumber;

public class RetryRunner extends  Cucumber {//BlockJUnit4ClassRunner

	 private final String className;

	    @SuppressWarnings("rawtypes")
	    public RetryRunner(Class clazz) throws InitializationError, IOException {
	        super(clazz);
	        className = clazz.getSimpleName();
	    }


	    @Override
	    public void run(RunNotifier notifier) {
	        notifier.addListener(new RunListener(){
                @Override
	            public void testFailure(Failure failure) throws Exception {

	                Throwable error = failure.getException();
	                if (error instanceof AssertionError){
	                    return;
	                }
	                RetryRunner.addFile(className);
	            }

	        });
	        super.run(notifier);
	    }


	    private static final String FILE_NAME = "target/rerun.properties";
	    private static final Set<String> ADDED_CLASSES = new HashSet<>();
	    public static synchronized void addFile(String className) throws IOException{

	        if (ADDED_CLASSES.contains(className)){
	            return;
	        }

	        File file = new File(FILE_NAME);
	        if (!file.exists()){
	            //Need to create the file
	            PrintWriter writer = new PrintWriter(file, "UTF-8");
	            writer.print("retryclasses=**/"+className+".class");
	            writer.close();
	        }
	        else {
	            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
	            out.print(",**/"+className+".class");
	            out.close();
	        }

	        ADDED_CLASSES.add(className);
	    }
}