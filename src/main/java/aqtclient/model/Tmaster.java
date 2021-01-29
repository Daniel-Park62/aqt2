package aqtclient.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


/**
 * The persistent class for the tmaster database table.
 * 
 */
@Entity
@NamedQuery(name="Tmaster.findAll", query="SELECT t FROM Tmaster t ORDER BY t.tdate desc,t.lvl desc, t.code")
@NamedQuery(name="Tmaster.TotalCnt", query="SELECT COUNT(t.code) cnt FROM Tmaster t")
public class Tmaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String code = "";

	private String desc1 = "";

	private String lvl  = "1";   //  1.단위테스트 2.통합테스트 3.실시간

	@Temporal(TemporalType.DATE)
	private Date tdate  ;

	private String cmpCode = "";

	@Temporal(TemporalType.DATE)
	private Date endDate ;

	private String tdir = "";

	private String tenv = "";

	private String thost = "";

	private int tport = 0;

	private String tuser = "";

	private String type = "1";    // 1.배치테스트 2.실시간
	
	@Transient
	private boolean newFlag = false ;


	public Tmaster() {

	}
	public boolean isNew() {
		return newFlag;
	}

	public void setNew(boolean isNew) {
		this.newFlag = isNew;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc1() {
		return this.desc1;
	}

	public void setDesc1(String desc1) {
		this.desc1 = desc1;
	}

	public String getLvl() {
		return this.lvl == null ? "0": this.lvl;
	}

	public void setLvl(String lvl) {
		this.lvl = lvl;
	}

	public String getLvlNm() {
		return "1".equals(this.lvl) ? "단위테스트" : 
			   "2".equals(this.lvl) ? "통합테스트" : 
			   "3".equals(this.lvl) ? "실시간" : 
			   "0".equals(this.lvl) ? "Origin" : "" ;
	}

	public Date getTdate() {
		return this.tdate;
	}

	public void setTdate(Date tdate) {
		this.tdate = tdate;
	}

	public Date getEndDate() {
		return endDate ;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getCmpCode() {
		return cmpCode;
	}

	public void setCmpCode(String cmpCode) {
		this.cmpCode = cmpCode;
	}

	public String getTdir() {
		return this.tdir;
	}

	public void setTdir(String tdir) {
		this.tdir = tdir;
	}

	public String getTenv() {
		return tenv == null ? "" : this.tenv;
	}

	public void setTenv(String tenv) {
		this.tenv = tenv;
	}

	public String getThost() {
		return this.thost;
	}

	public void setThost(String thost) {
		this.thost = thost;
	}

	public int getTport() {
		return this.tport;
	}

	public void setTport(int tport) {
		this.tport = tport;
	}

	public String getTuser() {
		return this.tuser;
	}

	public void setTuser(String tuser) {
		this.tuser = tuser;
	}

	public String getType() {
		return this.type;
	}

	public String getTypeNm() {
		return "1".equals(type) ? "배치" : "실시간";
	}

	public void setType(String type) {
		this.type = type;
	}

}