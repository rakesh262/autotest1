package sqs.core.utils;

import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import sqs.core.constants.Constants;
import sqs.core.constants.PropertyConstants;
import sqs.framework.FrameworkData;
import sqs.framework.FrameworkBase;

import java.io.FileInputStream;
import java.util.Properties;

public class LoggerUtils {
    private LoggerUtils(){

    }

    public static void reConfigureLogPath()  {
        Properties logProperties = new Properties();
        final String LOG4J_PROPERTIES =FrameworkData.MAIN_RESOURCES_PATH +"/log4j.properties";
        try {
            FrameworkBase.logger.info("Log file Path:" + FrameworkData.PROJECT_PATH + "");
            logProperties.load(new FileInputStream(LOG4J_PROPERTIES));
            FileUtils.createFolderIfNotExist(FrameworkData.getConfig().getProperty(PropertyConstants.REPORT_FOLDER_PATH));
            logProperties.setProperty("log4j.appender.writeLogInFile.File", FrameworkData.getConfig().getProperty(PropertyConstants.REPORT_FOLDER_PATH) + "/TestScriptExecution.log");
            LogManager.resetConfiguration();
            PropertyConfigurator.configure(logProperties);
        }  catch (Exception exception){
            FrameworkBase.logger.error(Constants.EXCEPTION_ON+Utilities.getCallerMethodName()+" :",exception);

        }
    }

    public static void actionStartedLog(String methodName){
        FrameworkBase.logger.debug(methodName+" action process started...");
    }

    public static void actionCompletedLog(String methodName){
        FrameworkBase.logger.debug(methodName+" action process completed.");

    }

    public static void methodNotImplemented(String methodName){
        FrameworkBase.logger.error(methodName+ " "+Constants.METHOD_NOT_IMPLEMENTED);

    }



}
