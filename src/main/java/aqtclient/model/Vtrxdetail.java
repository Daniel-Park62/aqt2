package aqtclient.model;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.ReadOnly;

/**
 * The persistent class for the vtrxdetail database table.
 * 
 */
@Entity
@ReadOnly
@NamedQuery(name="Vtrxdetail.findAll", query="SELECT v FROM Vtrxdetail v")
@NamedQuery(name="Vtrxdetail.findByCode", query="SELECT v FROM Vtrxdetail v WHERE v.tcode = :tcode")
public class Vtrxdetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id private long pkey ;

	private double avgt;

	private long fcnt;

	private long scnt;

//	private String appid;

	private String scrno;

	private String svcid;

	private String svckor;

	private long tcnt;

	private long cumcnt;

	private String tcode;

	public Vtrxdetail() {
	}

	public Vtrxdetail(String svcid, String svckor, long cumcnt, long tcnt, double avgt, long scnt, long fcnt) {
		super();
		this.svcid = svcid;
		this.svckor = svckor;
		this.cumcnt = cumcnt;
		this.tcnt = tcnt;
		this.avgt = avgt;
		this.scnt = scnt;
		this.fcnt = fcnt;
	}

	public long getPkey() {
		return this.pkey;
	}
	public double getAvgt() {
		return this.avgt;
	}

	public void setAvgt(double avgt) {
		this.avgt = avgt;
	}

	public long getFcnt() {
		return this.fcnt;
	}

	public void setFcnt(long fcnt) {
		this.fcnt = fcnt;
	}

	public long getScnt() {
		return this.scnt;
	}

	public void setScnt(long scnt) {
		this.scnt = scnt;
	}

	public String getScrno() {
		return this.scrno;
	}

	public void setScrno(String scrno) {
		this.scrno = scrno;
	}

	public String getSvcid() {
		return this.svcid;
	}

	public void setSvcid(String svcid) {
		this.svcid = svcid;
	}

	public String getSvckor() {
		return this.svckor;
	}

	public void setSvckor(String svckor) {
		this.svckor = svckor;
	}

	public long getTcnt() {
		return this.tcnt;
	}

	public void setTcnt(long tcnt) {
		this.tcnt = tcnt;
	}

	public long getCumcnt() {
		return this.cumcnt;
	}

	public String getTcode() {
		return this.tcode;
	}

	public void setTcode(String tcode) {
		this.tcode = tcode;
	}


}