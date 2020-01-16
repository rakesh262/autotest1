package sqs.core.actions.interfaces;
 public interface MobileActionsInterface {
	/***
	 * To touch/click on an element using Java-script
	 * 
	 * @param logicalName - The field name mentioned in the ObjectRepository file
	 * @return <b>True</b> if touched without any issues <br>
	 *         <b>False</b> if any error.<br>
	 *         Eg: <code> action.touch('Login')</code>
	 */
	boolean touch(String logicalName);
	
	/***
	 * To scroll to an element
	 * 
	 * @param logicalName - The field name mentioned in the ObjectRepository file
	 */
	void scrollToElement(String logicalName);
	
	/***
	 * To swipe/scroll down the screen
	 */
	void swipeDown();
	
	/***
	 * To swipe/scroll up the screen
	 */
	void swipeUp();
	
	/***
	 * To dismiss/hide the keyboard from the screen
	 */
	void dismissKeyBoard();
	
	/***
	 * To switch to the Native/Parent window
	 */
	void switchToNative();
	
	/***
	 * To switch to the web view
	 */
	void switchToWebView();

}
