package aqtclient.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the texecjob database table.
 * 
 */
@Entity
@NamedQuery(name = "Texecjob.findAll", query = "SELECT t FROM Texecjob t")
public class Texecjob implements Cloneable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int pkey;

	private int jobkind = 9;

	private String dbskip = "0";

	@Temporal(TemporalType.TIMESTAMP)
	private Date endDt;

	private String etc = "";

	private String limits = "";

	private int exectype = 0;

	@Column(name="in_file")
	private String infile = "";

	@Lob
	private String msg = "";

	private String outlogdir = "";

	private int reqnum = 0;

	private int repnum = 1;

	@Temporal(TemporalType.TIMESTAMP)
	private Date reqstartDt = new Date();

	private int resultstat = 0;

	@Temporal(TemporalType.TIMESTAMP)
	private Date startDt;

	private String tcode;

	private String tdir = "";

	private String tenv = "";

	private String tdesc = "";

	private int tnum = 1;

	private String tuser = "";

	public Texecjob() {
	}

	public Texecjob copy() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		Texecjob te = (Texecjob)this.clone() ; 
		return te ;
	}

	public int getPkey() {
		return this.pkey;
	}

	public void setPkey(int pkey) {
		this.pkey = pkey;
	}

	public String getJobkindNm() {
		switch (jobkind) {
		case 0:
			return "패킷캡쳐";
		case 1:
			return "Import패킷";
		case 3:
			return "전문생성";
		case 9:
			return "전문송신";
		default:
			return "";
		}
	}
	
	public int getJobkind() {
		return jobkind;
	}

	public void setJobkind(int jobkind) {
		this.jobkind = jobkind;
	}

	public String getDbskip() {
		return this.dbskip;
	}

	public void setDbskip(String dbskip) {
		this.dbskip = dbskip;
	}

	public Date getEndDt() {
		return this.endDt;
	}

	public void setEndDt(Date endDt) {
		this.endDt = endDt;
	}

	public String getEtc() {
		return this.etc;
	}

	public void setEtc(String etc) {
		this.etc = etc;
	}

	public int getExectype() {
		return this.exectype;
	}

	public void setExectype(int exectype) {
		this.exectype = exectype;
	}

	public String getInfile() {
		return this.infile;
	}

	public void setInfile(String infile) {
		this.infile = infile;
	}

	public String getMsg() {
		return (this.msg == null ? "" : msg);
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getOutlogdir() {
		return this.outlogdir;
	}

	public void setOutlogdir(String outlogdir) {
		this.outlogdir = outlogdir;
	}

	public int getReqnum() {
		return this.reqnum;
	}

	public void setReqnum(int reqnum) {
		this.reqnum = reqnum;
	}

	public int getRepnum() {
		return this.repnum;
	}

	public void setRepnum(int repnum) {
		this.repnum = repnum;
	}

	public Date getReqstartDt() {
		return this.reqstartDt;
	}

	public void setReqstartDt(Date reqstartDt) {
		this.reqstartDt = reqstartDt;
	}

	public int getResultstat() {
		return this.resultstat;
	}

	public void setResultstat(int resultstat) {
		this.resultstat = resultstat;
	}

	public Date getStartDt() {
		return this.startDt;
	}

	public void setStartDt(Date startDt) {
		this.startDt = startDt;
	}

	public String getTcode() {
		return this.tcode;
	}

	public void setTcode(String tcode) {
		this.tcode = tcode;
	}

	public String getTdir() {
		return this.tdir;
	}

	public void setTdir(String tdir) {
		this.tdir = tdir;
	}

	public String getTenv() {
		return this.tenv;
	}

	public void setTenv(String tenv) {
		this.tenv = tenv;
	}

	public String getTdesc() {
		return this.tdesc;
	}

	public void setTdesc(String tdesc) {
		this.tdesc = tdesc;
	}

	public int getTnum() {
		return this.tnum;
	}

	public void setTnum(int tnum) {
		this.tnum = tnum;
	}

	public String getTuser() {
		return this.tuser;
	}

	public void setTuser(String tuser) {
		this.tuser = tuser;
	}

	public String getLimits() {
		return limits;
	}

	public void setLimits(String limits) {
		this.limits = limits;
	}

}