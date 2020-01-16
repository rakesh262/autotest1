package sqs.core.actions.mobile;

public class MobileIOSWebActions extends MobileIOSActions {
	@Override
	public boolean click(String logicalName)
	{
		return action.clickByJavaScript(logicalName);
	}
			
}
