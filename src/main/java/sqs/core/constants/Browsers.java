package sqs.core.constants;

import org.apache.log4j.Logger;

public class Browsers {
    private static Logger logger = Logger.getLogger(Browser.class);

    public static boolean isBrowser(String value) {
        boolean isItBrowser = false;
        try {
            Browser.valueOf(value.toUpperCase().trim());
            isItBrowser = true;
        } catch (IllegalArgumentException e) {
            logger.debug(value + " is not a Browser.");
        }
        return isItBrowser;
    }

    public static Browser getBrowser(String value) {
        Browser browser;
        try {
            browser = Browser.valueOf(value.toUpperCase());

        } catch (IllegalArgumentException e) {
            logger.error("Given browser type " + value + " is not a BrowserName. If it is a Browser Please add it in sqs.core.constants.Browser Enum.");
            throw e;
        }
        return browser;
    }

    public enum Browser {
        CHROME("Chrome"), FIREFOX("Firefox"), SAFARI("Safari"), IE("IE"), EDGE("Edge");
        private final String browserName;

        Browser(String browserName) {
            this.browserName = browserName;
        }

        public String getName() {
            return this.browserName;
        }
    }
}