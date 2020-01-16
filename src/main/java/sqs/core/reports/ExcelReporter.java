package sqs.core.reports;

import cucumber.api.Scenario;
import cucumber.runtime.CucumberException;
import sqs.core.constants.ApplicationTypes;
import sqs.core.constants.PropertyConstants;
import sqs.core.utils.Utilities;
import sqs.framework.FrameworkData;
import sqs.framework.FrameworkBase;
import sqs.core.utils.ResultUtils;
import sqs.core.utils.TagHandler;

import static sqs.core.constants.Constants.EXCEPTION_ON;

public class ExcelReporter {
    private ExcelReporter(){

    }
    /**
     * Method: embedScreenshot Description: This method attach screenshot to the
     * reports report.
     *
     * @author Automation Tester (SQS)
     * @Creation Date: 20 July 2015 Modified Date:
     */

    public static void updateStatusInExcelFile(Scenario scenario)  {
        try {
            String scenarioID;
            String deviceName;
            String scenarioStatus = "Failed";
            String scenarioStatusFromCucumber = scenario.getStatus().toString();
            FrameworkBase.logger.debug("Scenario Status From reports:" + scenarioStatusFromCucumber);
            if (scenarioStatusFromCucumber.equalsIgnoreCase("passed")) {
                scenarioStatus = "Passed";
            } else if (scenarioStatusFromCucumber.equalsIgnoreCase("UNDEFINED")) {
                scenarioStatus = "UNDEFINED";
            }
            if (FrameworkData.getApplicationTypeToExecute() == ApplicationTypes.ApplicationType.DESKTOPWEB) {
                deviceName = FrameworkData.getCurrentBrowser().getName();
            } else {
                deviceName = FrameworkData.getConfig().getProperty(PropertyConstants.DEVICE_NAME);
            }
            scenarioID = TagHandler.getScenarioIDFromTag(scenario);
            if (scenarioID.isEmpty()) {
                throw new CucumberException("The Scenario is not having tag or A tag should have '-' or '_' in it. Please include '-' or '_' on one tag name." + scenario.getSourceTagNames().toArray());
            } else {
                ResultUtils.updateResultsInReportFile(FrameworkData.getConfig(), scenarioID, deviceName, scenarioStatus);
            }
        } catch (Exception exception) {
            FrameworkBase.logger.error(EXCEPTION_ON+ Utilities.getCallerMethodName()+" :",exception);

        }

    }
}
