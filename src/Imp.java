
public class Imp {
	private long timeStamp;
	private String transactionId;
	private int connectionType;
	private int deviceType;
	private int count;
	
	public Imp(long ts, String transId, int connType, int devType, int count) {
		this.timeStamp = ts;
		this.transactionId = transId;
		this.connectionType = connType;
		this.deviceType = devType;
		this.count = count;
	}
	
	public long getTimestamp() {
		return timeStamp;
	}
	
	public String getTransactionId() {
		return transactionId;
	}
	
	public int getConnectionType() {
		return connectionType;
	}
	
	public int getDeviceType() {
		return deviceType;
	}
	
	public int getCount() {
		return count;
	}
}
