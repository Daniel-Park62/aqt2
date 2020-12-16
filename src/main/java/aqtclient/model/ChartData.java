package aqtclient.model;

import java.sql.Timestamp;

public class ChartData {
	private Timestamp dtime;
	private double trxCnt;
	
	public ChartData(Timestamp dtime, double trxCnt) {
		super();
		this.dtime = dtime;

		this.trxCnt = trxCnt;
	}
	
	public Timestamp getDtime() {
		return dtime;
	}
	public void setDtime(Timestamp dtime) {
		this.dtime = dtime;
	}


	public double getTrxCnt() {
		return trxCnt;
	}
	public void setTrxCnt(double trxCnt) {
		this.trxCnt = trxCnt;
	}   

}
