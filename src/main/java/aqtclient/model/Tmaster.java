package aqtclient.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tmaster database table.
 * 
 */
@Entity
@NamedQuery(name="Tmaster.findAll", query="SELECT t FROM Tmaster t ORDER BY t.tdate desc,t.lvl desc, t.code")
@NamedQuery(name="Tmaster.TotalCnt", query="SELECT COUNT(t.tcode) cnt FROM Tmaster t")
public class Tmaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int tcode ;
	
	private String code = "";

	private String desc1 = "";

	private String lvl  = "1";   //  1.단위테스트 2.통합테스트 3.실시간

	private String tdate = "";

	private String cmpCode = "";

	private String endDate = "";

	private String tdir = "";

	private String tenv = "";

	private String thost = "";

	private String tport = "";

	private String tuser = "";

	private String type = "1";    // 1.배치테스트 2.실시간

	public Tmaster() {

	}

	public int getTcode() {
		return this.tcode;
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
		return "1".equals(this.lvl) ? "단위테스트" : "통합테스트" ;
	}

	public String getTdate() {
		return this.tdate;
	}

	public void setTdate(String tdate) {
		this.tdate = tdate;
	}

	public String getEndDate() {
		return endDate == null ? "" : endDate;
	}

	public void setEndDate(String endDate) {
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

	public String getTport() {
		return this.tport;
	}

	public void setTport(String tport) {
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