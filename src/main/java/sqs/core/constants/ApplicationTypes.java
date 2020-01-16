package sqs.core.constants;

import org.apache.log4j.Logger;

import sqs.framework.FrameworkData;

public class ApplicationTypes {
	private static Logger logger = Logger.getLogger(ApplicationTypes.class);

	public enum ApplicationType {
		DESKTOPWEB, MOBILEWEB, NATIVEAPP, HYBRIDAPP, OTHERS
	}

	public static boolean isApplicationType(String value) {
		boolean isItApplicationType = false;
		try {
			ApplicationType.valueOf(value.toUpperCase());
			isItApplicationType = true;
		} catch (IllegalArgumentException e) {
			logger.debug(value + " is not a ApplicationType.");
		}
		return isItApplicationType;
	}

	public static ApplicationType getApplicationType(String value) {
		ApplicationType applicationType = null;

		try {
			if (value.contains(",")) {
				applicationType = getApplicationTypeMultipleProjects(value, applicationType);
			} else {
				applicationType = ApplicationType.valueOf(value.toUpperCase());
			}

		} catch (IllegalArgumentException e) {
			logger.error("Given ApplicationType type " + value
					+ " is not a ApplicationType. If it is a ApplicationType Please add it in sqs.core.constants.ApplicationType Enum.");
			throw e;
		}
		return applicationType;
	}

	private static ApplicationType getApplicationTypeMultipleProjects(String value, ApplicationType applicationType) {

		String[] s = value.split(",");
		if (s[0].equalsIgnoreCase(Constants.NATIVE_APP) || s[1].equalsIgnoreCase(Constants.NATIVE_APP)) {
			if (FrameworkData.getApplicationCount() <= 1) {
				if (s[0].equalsIgnoreCase(Constants.NATIVE_APP)) {
					applicationType = ApplicationType.valueOf(s[0].toUpperCase());
					FrameworkData.incrementApplicationCount();
				} else if (s[1].equalsIgnoreCase(Constants.NATIVE_APP)) {
					applicationType = ApplicationType.valueOf(s[1].toUpperCase());
					FrameworkData.incrementApplicationCount();
				}

			}
		} else {
			if (s[0].equals("DeskTopWeb")) {
				applicationType = ApplicationType.valueOf(s[0].toUpperCase());
			} else if (s[1].equals("DeskTopWeb")) {
				applicationType = ApplicationType.valueOf(s[1].toUpperCase());
			}
		}

		return applicationType;
	}
}