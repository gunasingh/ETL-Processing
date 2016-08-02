
public class Imp {
	private long m_timeStamp;
	private String m_stTransactionId;
	private int m_connectionType;
	private int m_deviceType;
	private int m_count;
	
	public Imp(long ts, String transId, int connType, int devType, int count) {
		this.m_timeStamp = ts;
		this.m_stTransactionId = transId;
		this.m_connectionType = connType;
		this.m_deviceType = devType;
		this.m_count = count;
	}
	
	public long getTimestamp() {
		return m_timeStamp;
	}
	
	public String getTransactionId() {
		return m_stTransactionId;
	}
	
	public int getConnectionType() {
		return m_connectionType;
	}
	
	public int getDeviceType() {
		return m_deviceType;
	}
	
	public int getCount() {
		return m_count;
	}
}
