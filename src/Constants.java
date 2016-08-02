import java.util.Map;

public class Constants {
	public static final String DIR_DIMENSIONS = System.getProperty("user.dir") + "/in/dimensions";
	public static final String DIR_IMPS = System.getProperty("user.dir") + "/in/facts/imps";
	public static final String DIR_CLICKS = System.getProperty("user.dir") + "/in/facts/clicks";
	public static final String DIR_OUT = System.getProperty("user.dir") + "/out";
	public static final String DIR_LOG = System.getProperty("user.dir") + "/log";
	public static final String FILE_DEVICE_TYPE = DIR_DIMENSIONS + "/device_type.json";
	public static final String FILE_CONNECTION_TYPE = DIR_DIMENSIONS + "/connection_type.json";
	public static final String FILE_LOG = DIR_LOG + "/etl.log";
	public static final String OUT_TIMEZONE = "PST";
	public static final int LOG_POLL_MS = 100; 
	public static final int THREAD_COUNT = 5;
	public static final int IMP_FIELD_COUNT = 5;
	public static final int CLICK_FIELD_COUNT = 3;
	public static final String NEW_LINE = "\r\n";
	
	public static Map<Integer, String> s_mapDevice = null;
	public static Map<Integer, String> s_mapConnection = null;
}
