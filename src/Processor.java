import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONObject;


public class Processor implements Runnable {
	
	// Thread specific data
	private Map<String, Imp> m_mapImp;
	private Map<String, Click> m_mapClick;
	private File m_fileImp;
	private File m_fileClick;
	private File m_fileOut;
	private String m_stHour;
	
	public Processor (File fileImp) throws IllegalArgumentException {
		m_fileImp = fileImp;
		init();
	}

	// Initialize thread member variables
	public void init() throws IllegalArgumentException {
		if (m_fileImp == null || !new File (Constants.DIR_CLICKS, m_fileImp.getName()).exists()) {
			Log.log(Log.ERROR, m_stHour + " Invalid imp file.");
			throw new IllegalArgumentException ("Invalid imp file.");
		}
		m_stHour = m_fileImp.getName().substring(0, m_fileImp.getName().lastIndexOf("."));
		m_fileClick = new File (Constants.DIR_CLICKS, m_fileImp.getName());
		File dirOut = new File(Constants.DIR_OUT);
		if (!dirOut.exists() || !dirOut.isDirectory()) {
			dirOut.mkdirs();
		}
		m_fileOut = new File(Constants.DIR_OUT, m_stHour + ".json");
		m_mapImp = new HashMap<String, Imp>();
		m_mapClick = new HashMap<String, Click>();
	}
	
	// Parse imp file and populate map 
	public void populateImpMap() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(m_fileImp));
			String stLine = null;
			while ((stLine = br.readLine()) != null) {
				Imp imp = Utilities.getImp(stLine);
				if (imp != null) {
					m_mapImp.put(imp.getTransactionId(), imp);
				} else {
					Log.log(Log.WARNING, m_stHour + " Invalid entry found in imp file.");
				}
			}
		} catch (FileNotFoundException e) {
			Log.log(Log.ERROR, m_stHour + " Unable to open imp file." + Constants.NEW_LINE + e.getMessage());
			m_mapImp = null;
		} catch (IOException e) {
			Log.log(Log.ERROR, m_stHour + " Unable to read from imp file." + Constants.NEW_LINE + e.getMessage());
			m_mapImp = null;
		}
	}
	
	// Parse click file and populate map
	public void populateClickMap() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(m_fileClick));
			String stLine = null;
			while ((stLine = br.readLine()) != null) {
				Click click = Utilities.getClick(stLine);
				if (click != null) {
					m_mapClick.put(click.getTransactionId(), click);
				} else {
					Log.log(Log.WARNING, m_stHour + " Invalid entry found in click file.");
				}
			}
		} catch (FileNotFoundException e) {
			Log.log(Log.ERROR, m_stHour + " Unable to open click file." + Constants.NEW_LINE + e.getMessage());
		} catch (IOException e) {
			Log.log(Log.ERROR, m_stHour + " Unable to read from click file." + Constants.NEW_LINE + e.getMessage()); 
		}
	}

	// Merge imp and click maps and write JSON entry to out file
	@SuppressWarnings("rawtypes")
	public void writeToOutput (BufferedWriter bw) throws IOException {
		if (m_mapImp != null) {
			Iterator<Map.Entry<String, Imp>> impIter = m_mapImp.entrySet().iterator();
			while (impIter.hasNext()) {
				Map.Entry<String, Imp> impEntry = impIter.next();
				String transId = impEntry.getKey();
				Imp imp = impEntry.getValue();
				Click click = m_mapClick.get(transId);
				Map mapJSON = Utilities.getOutputJSON(imp, click);
				bw.write(JSONObject.toJSONString(mapJSON) + Constants.NEW_LINE);
				impIter.remove();
				m_mapClick.remove(transId);
			}
		}
	}
	
	@Override
	public void run() {
		long startTime = System.currentTimeMillis();
		Log.log(Log.INFO, m_stHour + " ETL start.");
		populateImpMap();
		if (m_mapImp != null) {
			populateClickMap();
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter(m_fileOut, true));
				writeToOutput(bw);
			} catch (IOException e) {
				Log.log(Log.ERROR, m_stHour + " Unable to write to out file." + Constants.NEW_LINE + e.getMessage());
			} finally {
				long elapsedTime = System.currentTimeMillis()-startTime;
				String stTimeUnit = "ms";
				if (elapsedTime > 1000) {
					elapsedTime /= 1000;
					stTimeUnit = "s";
				}
				Log.log(Log.INFO, m_stHour + " ETL complete, elapsed time: " + 
							elapsedTime + stTimeUnit + ".");
				if (bw != null) {
					try {
						bw.flush();
						bw.close();
					} catch (IOException e) {
						Log.log(Log.ERROR, m_stHour + " Unable to close out file." + Constants.NEW_LINE + e.getMessage()); 
					}
				}
			}
		} else {
			long elapsedTime = System.currentTimeMillis()-startTime;
			String stTimeUnit = "ms";
			if (elapsedTime > 1000) {
				elapsedTime /= 1000;
				stTimeUnit = "s";
			}
			Log.log(Log.ERROR, m_stHour + " ETL complete, elapsed time: " + 
					elapsedTime + stTimeUnit + ".");
		}
	}
	
}
