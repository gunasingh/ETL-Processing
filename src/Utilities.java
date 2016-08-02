import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;


public class Utilities {
	// Parse a line from imp file and generate Imp object
	public static Imp getImp (String stEntry) {
		if (stEntry == null || stEntry.length() <= 0) {
			return null;
		}
		String[] aStFields = stEntry.split(",");
		if (aStFields.length != Constants.IMP_FIELD_COUNT) {
			return null;
		}
		
		long timeStamp = Long.parseLong(aStFields[0]);
		String transId = aStFields[1];
		int connType = Integer.parseInt(aStFields[2]);
		int devType = Integer.parseInt(aStFields[3]);
		int count = Integer.parseInt(aStFields[4]);
		
		return new Imp(timeStamp, transId, connType, devType, count);
	}
	
	// Parse a line from click file and generate Click object
	public static Click getClick (String stEntry) {
		if (stEntry == null || stEntry.length() <= 0) {
			return null;
		}
		String[] aStFields = stEntry.split(",");
		if (aStFields.length != Constants.CLICK_FIELD_COUNT) {
			return null;
		}
		
		long timeStamp = Long.parseLong(aStFields[0]);
		String transId = aStFields[1];
		int count = Integer.parseInt(aStFields[2]);
		
		return new Click(timeStamp, transId, count);
	}
	
	// Generate ordered map of the fields to be written to out
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map getOutputJSON (Imp imp, Click click) {
		Map orderedMap = new LinkedHashMap();
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
		df.setTimeZone(TimeZone.getTimeZone(Constants.OUT_TIMEZONE));
		orderedMap.put("iso8601_timestamp", df.format(new Date(imp.getTimestamp()*1000)));
		orderedMap.put("transaction_id", imp.getTransactionId());
		orderedMap.put("connection_type", Constants.s_mapConnection.get(imp.getConnectionType()));
		orderedMap.put("device_type", Constants.s_mapDevice.get(imp.getDeviceType()));
		orderedMap.put("imps", imp.getCount());
		if (click != null) {
			orderedMap.put("clicks", click.getCount());
		}
		return orderedMap;
	}
	
	// Check if a given file has an extension of .csv
	public static boolean isCSV (String stFile) {
		int index = stFile.lastIndexOf(".");
		return index > 0 && stFile.substring(index+1).equalsIgnoreCase("csv");
	}
}
