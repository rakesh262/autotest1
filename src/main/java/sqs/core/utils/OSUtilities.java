package sqs.core.utils;

import java.util.Locale;

public class OSUtilities {
	public enum OSType {
	    WINDOWS, MAC, ANDROID,IOS, OTHER
	  }
	  protected static OSType detectedOS;	  
	  public static OSType getOperatingSystemType() {
	    if (detectedOS == null) {
	      String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
	      if ((os.indexOf("mac") >= 0) || (os.indexOf("darwin") >= 0)) {
	        detectedOS = OSType.MAC;
	      } else if (os.indexOf("win") >= 0) {
	        detectedOS = OSType.WINDOWS;	     
	      } else {
	        detectedOS = OSType.OTHER;
	      }
	    }
	    return detectedOS;
	  }
}
