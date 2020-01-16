package sqs.core.constants;

public class Constants {

	private Constants(){

	}

	public static final String  MOBILE_WEB = "MobileWeb";
	public static final String DESKTOP_WEB = "DeskTopWeb";
	public static final String NATIVE_APP = "NativeApp";
	public static final String HYBRID_APP = "hybridapp";
	public static final String IOS = "iOS";
	public static final String ANDROID = "Android";
	public static final int WAIT_FOR_SHORT_ACTION = 3000;
	public static final Object CHROME = "Chrome";
	public static final Object FIREFOX = "Firefox";
	public static final Object SAFARI = "Safari";
	public static final Object IE = "IE";
	public static final Object Edge = "Edge";

	// FRAMEWORK CONSTANTS //
	public static final String TEST_DATA="TestData";
	public static final String CONFIG ="Config";
	public static final String TO_ADD_DEVICE ="To add Device ";
	public static final String TO_ADD_TEST_SUITE_ID = "To Add TestSuite Id";
	public static final String EXCEPTION_ON = "Exception occured in ";
	public static final String METHOD_NOT_IMPLEMENTED = "Method not implemented";
	public static final String DO_NOTHING ="Do Nothing";
	public static final String YET_TO_IMPLEMENT ="Yet to be implemented";
	public static final String REPORTS_PATH="/Result/Reports";

	//ACTION CONSTANTS //
	public static final String CLICKED_ON_ELEMENT = "Clicked on Element ";
	public static final String CLICK ="Click";
	public static final String ELEMENT ="Element ";
	public static final String TEXT =" text";
	public static final String SELECT_DROPDOWN_OPTION ="SelectDropdownOption";
	public static final String FEATURE ="Feature";
	public static final String USER_DIR= System.getProperty("user.dir");
	public  static final String JSON_EXTN=".json";



	// JAVASCRIPT //
	public static final String JAVASCRIPT_ARGUMENTS_0_CLICK= "arguments[0].click();";

	// DOM ELEMENTS//
	public static final String TABLE_TD =".//td";
	public static final String TABLE_HEADER =".//thead/tr/th";
	public static final String TABLE_ROWS =".//tbody/tr";

	// API CONSTANTS //

	public static final String API_HEADER_DEST ="api.header.dest";
	public static final String API_BODY_DEST ="api.body.dest";
	public static final String API_PARAM_DEST ="api.param.dest";
	public static final String API_TEST_URL="api.test.url";
	public static final String VERIFY_THE_VALID_SESSION_ID_IS_RETURNED ="\tverify the valid session id is returned";


	// LOGGER CONSTANTS //
	public static final String LOG_START ="______________________________________________________________________________________start___________________________________________________________";
	public static final String LOG_POST="______________________________________________________________________________________post___________________________________________________________";
	public static final String LOG_GET="_______________________________________________________________________________________get____________________________________________________________";
	public static final String LOG_PUT="_______________________________________________________________________________________put____________________________________________________________";
	public static final String LOG_DELETE="_______________________________________________________________________________________delete____________________________________________________________";
	public static final String LOG_END="_______________________________________________________________________________________end____________________________________________________________";
	public static final String LOG_NEW_LINE="______________________________________________________________________________________________________________________________________________________";





}
