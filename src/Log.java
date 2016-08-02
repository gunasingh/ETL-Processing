import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Log extends Thread {
	private File m_fileLog;
	private static BlockingQueue<String> s_qLog = new LinkedBlockingQueue<String>();
	
	public static final String INFO = "INFO";
	public static final String ERROR = "ERROR";
	public static final String WARNING = "WARN";

	public Log (String stLog) {
		m_fileLog = new File(stLog);
	}
	
	// Add the log line to the linked blocking queue
	public static void log (String stLevel, String stLog) {
		try {
			if (stLog != null && stLog.length() > 0) {
				StringBuilder sb = new StringBuilder();
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
				Calendar cal = Calendar.getInstance();
				sb.append(dateFormat.format(cal.getTime()))
					.append(" " + stLevel)
					.append(" Hour ")
					.append(stLog)
					.append(Constants.NEW_LINE);
				s_qLog.put(sb.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Retrieve the entries from linked blocking queue and writes to log file
	@Override
	public void run() {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(m_fileLog, true));
			String stLog = null;
			while ((stLog = s_qLog.poll(Constants.LOG_POLL_MS, TimeUnit.MILLISECONDS)) != null) {
				bw.write(stLog);
				stLog = null;	
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.flush();
					bw.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
