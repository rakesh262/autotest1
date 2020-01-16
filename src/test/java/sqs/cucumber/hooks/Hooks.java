package sqs.cucumber.hooks;

import cucumber.api.Scenario;
import cucumber.api.java.*;
import sqs.framework.FrameworkBase;

import static sqs.core.constants.Constants.EXCEPTION_ON;
import static sqs.core.utils.Utilities.getCallerMethodName;

/**
 * @ScriptName : hooks
 * @Description : This class contains
 * @Author : Automation Tester (SQS)
 * @Creation Date : 17 Jun 2015 @Modified Date:
 */
public class Hooks extends FrameworkBase {
	@Before
	public void applyHook(Scenario scenario) {
		try {				
			beforeScenario(scenario);
		} catch (Exception exception) {
			logger.error(EXCEPTION_ON+ getCallerMethodName()+" :",exception);
		}
	}

	@After	
	public void removeHook(Scenario scenario)  {
		try {
			afterScenario(scenario);
		} catch (Exception exception) {
			logger.error(EXCEPTION_ON+ getCallerMethodName()+" :",exception);
		}
	}



	    
}
