package aqtclient.model;

public class TrxCount {
	private long trxCnt;
	private long validCnt;
	private long invalidCnt;
	
	public TrxCount(long trxCnt, long validCnt, long invalidCnt) {
		super();

		this.trxCnt = trxCnt;
		this.validCnt = validCnt;
		this.invalidCnt = invalidCnt;
	}
	
	public long getTrxCnt() {
		return trxCnt;
	}
	public void setTrxCnt(long trxCnt) {
		this.trxCnt = trxCnt;
	}

	public long getValidCnt() {
		return validCnt;
	}
	public void setValidCnt(long validCnt) {
		this.validCnt = validCnt;
	}
	public long getInvalidCnt() {
		return invalidCnt;
	}
	public void setInvalidCnt(long invalidCnt) {
		this.invalidCnt = invalidCnt;
	}	
}
