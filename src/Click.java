
public class Click {
	private long m_timeStamp;
	private String m_stTransactionId;
	private int m_count;
	
	public Click(long ts, String transId, int count) {
		this.m_timeStamp = ts;
		this.m_stTransactionId = transId;
		this.m_count = count;
	}
	
	public long getTimestamp() {
		return m_timeStamp;
	}
	
	public String getTransactionId() {
		return m_stTransactionId;
	}
	
	public int getCount() {
		return m_count;
	}
}
