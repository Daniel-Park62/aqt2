package aqtclient.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.ReadOnly;

@Entity
@ReadOnly
public class ChartData {
	
	@Id
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtime;
	private long trxCnt;
	
	public ChartData() {
		
	}
	public Date getDtime() {
		return dtime;
	}
	public void setDtime(Date dtime) {
		this.dtime = dtime;
	}


	public double getTrxCnt() {
		return trxCnt;
	}
	public void setTrxCnt(long trxCnt) {
		this.trxCnt = trxCnt;
	}   

}
