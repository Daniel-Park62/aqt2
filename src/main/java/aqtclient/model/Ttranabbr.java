package aqtclient.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


/**
 * The persistent class for the ttransaction database table.
 * 
 */
@Entity
public class Ttranabbr  {
	
	public Ttranabbr(int pkey, String uuid, String msgcd, String rcvmsg, String errinfo, String rdata, int rlen,
			Timestamp rtime, String scrno, String sdata, String sflag, int slen, Timestamp stime, String svrnm, String svcid,
			String userid, double svctime, String tcode) {
		super();
		this.pkey = pkey;
		this.uuid = uuid;
		this.msgcd = msgcd;
		this.rcvmsg = rcvmsg;
		this.errinfo = errinfo;
		this.rdata = rdata;
		this.rlen = rlen;
		this.rtime = rtime;
		this.scrno = scrno;
		this.sdata = sdata;
		this.sflag = sflag;
		this.slen = slen;
		this.stime = stime;
		this.svrnm = svrnm;
		this.svcid = svcid;
		this.userid = userid;
		this.svctime = svctime;
		this.tcode = tcode;
	}

	public Ttranabbr() {
		
	}
	
	public void setPkey(int pkey) {
		this.pkey = pkey;
	}

	public void setErrinfo(String errinfo) {
		this.errinfo = errinfo;
	}

	public void setRdata(String rdata) {
		this.rdata = rdata;
	}

	public void setSdata(String sdata) {
		this.sdata = sdata;
	}

	@Id
	private int pkey ;
	
	public int getPkey() {
		return pkey;
	}

	private String uuid;

	private Timestamp cdate;

	private double elapsed;

	private String msgcd;
	private String rcvmsg;
	private String errinfo;
	private String rdata;

	private int rlen;

	private Timestamp rtime;

	private String scrno;
	private String sdata;
	private String sflag;

	private int slen;

	private Timestamp stime;

	private String svrnm;

	private String svcid;

	private String userid;

	private double svctime;
	
	private String tcode;

	@ManyToOne(targetEntity = Tmaster.class)
	@JoinColumn(name = "tcode", referencedColumnName = "code" ) 
	private Tmaster tmaster ;

	public Tmaster getTmaster() {
		return tmaster ;
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

	public void setCdate(Timestamp cdate) {
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
		return this.rcvmsg;
	}

	public String getErrinfo() {
		return this.errinfo;
	}
	
	public void setRcvmsg(String rcvmsg) {
		this.rcvmsg = rcvmsg;
	}

	public String getRdata() {
		return new String(this.rdata);
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

	public void setRtime(Timestamp rtime) {
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

	public void setStime(Timestamp stime) {
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