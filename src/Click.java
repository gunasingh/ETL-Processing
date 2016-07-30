
public class Click {
	private long timeStamp;
	private String transactionId;
	private int count;
	
	public Click(long ts, String transId, int count) {
		this.timeStamp = ts;
		this.transactionId = transId;
		this.count = count;
	}
	
	public long getTimestamp() {
		return timeStamp;
	}
	
	public String getTransactionId() {
		return transactionId;
	}
	
	public int getCount() {
		return count;
	}
}
