package aqtclient.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.ReadOnly;


/**
 * The persistent class for the ttransaction database table.
 * 
 */
@ReadOnly
@Entity
@NamedQuery(name="Ttransaction.findAll", query="SELECT t FROM Ttransaction t")
@NamedQuery(name="Ttransaction.findByCode", query="SELECT t FROM Ttransaction t WHERE t.tcode = :tcode ORDER BY t.uuid")
@NamedQuery(name="Ttransaction.SvcCnt", query="SELECT COUNT(DISTINCT t.svcid) FROM Ttransaction t where t.tcode = :tcode")
@NamedQuery(name="Ttransaction.FlagCnt", query="SELECT COUNT(t.uuid) trxCnt " +
												", COUNT(CASE WHEN t.sflag = '1' THEN 1 ELSE NULL END) validCnt " + 
												", COUNT(CASE WHEN t.sflag = '2' THEN 1 ELSE NULL END) invalidCnt " +
												" FROM Ttransaction t WHERE t.tcode = :tcode")
@NamedNativeQuery(name="Ttransaction.chartData", 
 query="select date_format(t.stime, '%Y-%m-%d %H:%i:00') stime, count(t.uuid) cnt from Ttransaction t where t.tcode = ? group by date_format(t.stime, '%Y-%m-%d %H:%i:00') ")
public class Ttransaction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int pkey ;
	
	public int getPkey() {
		return pkey;
	}

	private String uuid;

	@Temporal(TemporalType.TIMESTAMP)
	private Date cdate;

	private double elapsed;

	private String msgcd;
	
	private String rcvmsg;
	
	private String errinfo;

	@Lob
	private byte[] rdata;

	private int rlen;

	@Temporal(TemporalType.TIMESTAMP)
	private Date rtime;

	private String scrno;

	@Lob
	private byte[]  sdata;

	private String sflag;

	private int slen;

	@Temporal(TemporalType.TIMESTAMP)
	private Date stime;

	private String svrnm;

	private String svcid;

	private String userid;

	private double svctime;

	private String tcode;
	
	public Ttransaction() {
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Date getCdate() {
		return this.cdate;
	}

	public void setCdate(Date cdate) {
		this.cdate = cdate;
	}

	public double getElapsed() {
		return this.elapsed;
	}

	public void setElapsed(double elapsed) {
		this.elapsed = elapsed;
	}

	public String getMsgcd() {
		return msgcd;
	}

	public void setMsgcd(String msgcd) {
		this.msgcd = msgcd;
	}

	public String getRcvmsg() {
		return this.rcvmsg == null ? "" : new String(this.rcvmsg);
	}

	public String getErrinfo() {
		return this.errinfo == null ? "" : new String(this.errinfo);
	}
	
	public String getRdata() {
		return this.rdata== null ? "" : new String(this.rdata);
	}


	public int getRlen() {
		return this.rlen;
	}

	public void setRlen(int rlen) {
		this.rlen = rlen;
	}

	public Date getRtime() {
		return this.rtime;
	}

	public void setRtime(Date rtime) {
		this.rtime = rtime;
	}

	public String getScrno() {
		return this.scrno;
	}

	public void setScrno(String scrno) {
		this.scrno = scrno;
	}

	public String getSdata() {
		return new String(this.sdata) ;
	}


	public String getSflag() {
		return this.sflag;
	}

	public void setSflag(String sflag) {
		this.sflag = sflag;
	}

	public String getSflagNm() {
		return "2".equals(this.sflag) ? "실패" : "성공" ;
	}

	public int getSlen() {
		return this.slen;
	}

	public void setSlen(int slen) {
		this.slen = slen;
	}

	public Date getStime() {
		return this.stime;
	}

	public void setStime(Date stime) {
		this.stime = stime;
	}

	public String getSvcid() {
		return this.svcid;
	}

	public void setSvcid(String svcid) {
		this.svcid = svcid;
	}

	public String getSvrnm() {
		return svrnm;
	}

	public void setSvrnm(String svrnm) {
		this.svrnm = svrnm;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public double getSvctime() {
		return this.svctime;
	}

	public void setSvctime(double svctime) {
		this.svctime = svctime;
	}

	public String getTcode() {
		return this.tcode;
	}

	public void setTcode(String tcode) {
		this.tcode = tcode;
	}

}