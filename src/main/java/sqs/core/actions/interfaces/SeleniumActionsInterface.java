package sqs.core.actions.interfaces;

import java.util.Set;

public interface SeleniumActionsInterface {
	/***
	 * To get the parent window name
	 * 
	 * @return - Parent window name
	 */
	String getMainWindowHandle();
	
	/***
	 * To set the implicit wait time
	 * 
	 * @param waitInSeconds - Wait time in seconds
	 */
	void setImplicitWait(int waitInSeconds);
	
	/***
	 * To get the list of Windows opened.
	 * 
	 * @return - List of Windows opened in Set<String> type
	 */
	Set <String> getWindowHandles();
	
	/***
	 * To get the active Window name
	 * 
	 * @return - Active window name
	 */
	String getWindowHandle();
	
	/***
	 * To switch to the base window
	 * 
	 * @param mainWindowHandle - Main window name
	 */
	void setMainWindowHandle(String mainWindowHandle);
	
}
