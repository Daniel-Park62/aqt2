package aqtclient.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the ttrxlist database table.
 * 
 */
@Entity
@NamedQuery(name="Ttrxlist.findAll", query="SELECT t FROM Ttrxlist t  ")
public class Ttrxlist implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String tcode;

	@Column(name="data_cnt")
	private int dataCnt;

	private int fcnt;

	@Column(name="fsvc_cnt")
	private int fsvcCnt;

	private int scnt;

	@Column(name="svc_cnt")
	private int svcCnt;

	@Temporal(TemporalType.TIMESTAMP)
	private Date udate;

	public Ttrxlist() {
	}

	public String getTcode() {
		return this.tcode;
	}

	public void setTcode(String tcode) {
		this.tcode = tcode;
	}

	public int getDataCnt() {
		return this.dataCnt;
	}

	public void setDataCnt(int dataCnt) {
		this.dataCnt = dataCnt;
	}

	public int getFcnt() {
		return this.fcnt;
	}

	public void setFcnt(int fcnt) {
		this.fcnt = fcnt;
	}

	public int getFsvcCnt() {
		return this.fsvcCnt;
	}

	public void setFsvcCnt(int fsvcCnt) {
		this.fsvcCnt = fsvcCnt;
	}

	public int getScnt() {
		return this.scnt;
	}

	public void setScnt(int scnt) {
		this.scnt = scnt;
	}

	public int getSvcCnt() {
		return this.svcCnt;
	}

	public void setSvcCnt(int svcCnt) {
		this.svcCnt = svcCnt;
	}

	public Date getUdate() {
		return this.udate;
	}

	public void setUdate(Date udate) {
		this.udate = udate;
	}

}