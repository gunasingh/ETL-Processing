import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class Processor {

	private static final String DIMENSIONS="in\\dimensions";
	private static final String IMPS = "in\\facts\\imps";
	private static final String CLICKS = "in\\facts\\clicks";
	private static final String DEVICE_TYPE = DIMENSIONS + "\\device_type.json";
	private static final String CONNECTION_TYPE = DIMENSIONS + "\\connection_type.json";
	
	/*public static Map<Integer, String> mapDevice = null;
	public static Map<Integer, String> mapConnection = null;*/
	
	public static void loadDevices() throws Exception {
		
	}
	
	public static Imp readImp (File inFile) {
		return null;
	}
	
	public static void main(String[] args) throws Exception {
		loadDevices();
	}
}
