package aqtclient.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the trequest database table.
 * 
 */
@Entity
@NamedQuery(name="Trequest.findAll", query="SELECT t FROM Trequest t")
public class Trequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long pkey;

	private long cmpid;

//	private Timestamp reqDt;

	private String reqUser;

	private String tcode;

	private String uuid;

	public Trequest() {
	}
	

	public Trequest(long pkey2, String tcode2, long cmpid2, String reqUser) {
		pkey = pkey2;
		tcode = tcode2;
		cmpid = cmpid2 ;
		this.reqUser = reqUser ;
//		reqDt = new Timestamp(System.currentTimeMillis()) ;
	}

	public long getPkey() {
		return this.pkey;
	}

	public void setPkey(int pkey) {
		this.pkey = pkey;
	}

//	public Timestamp getReqDt() {
//		return this.reqDt;
//	}
//
//	public void setReqDt(Timestamp reqDt) {
//		this.reqDt = reqDt;
//	}

	public String getReqUser() {
		return this.reqUser;
	}

	public void setReqUser(String reqUser) {
		this.reqUser = reqUser;
	}

	public String getTcode() {
		return this.tcode;
	}

	public void setTcode(String tcode) {
		this.tcode = tcode;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


	public long getCmpid() {
		return cmpid;
	}

}