package sqs.core.utils;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;

public class PropertyUtils {
	private PropertyUtils(){

	}
	static  String lineSplitter="----------------------------------------------------------------";
	private static Logger logger=Logger.getLogger(PropertyUtils.class);

	public static Properties loadProperties(String propertyFile)
	{
		Properties projectProperties=null;
		try {
			projectProperties = new Properties();
			projectProperties.load(new FileInputStream(new File(propertyFile)));
			for (Map.Entry<Object, Object> entry : projectProperties.entrySet()) {
				entry.setValue(entry.getValue().toString().trim());
			}
			return projectProperties;
		}catch(Exception exception){
			logger.error(exception);
		}
		return projectProperties;
	}
	public static void printAllProperty(Properties property)	
	{
		logger.info(lineSplitter);
		logger.info("                     Property Details");
		logger.info(lineSplitter);
		property.forEach((Object key,Object value) -> {
		if(String.valueOf(value).length()!=0)
			logger.info(key+"="+value);
		} );
		logger.info(lineSplitter);
	}
	
	public static boolean getBooleanStatus(Properties property,String key)	
	{
		boolean booleanState=false;
		String status=property.getProperty(key,"yes");
		if(status.equalsIgnoreCase("yes"))
		{
			booleanState=true;
		}
		
		return booleanState;
	}
	
}
