package aqtclient.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tservice database table.
 * 
 */
@Entity
@NamedQuery(name="Tservice.findAll", query="SELECT t FROM Tservice t")
@NamedQuery(name="Tservice.findById", query="SELECT t FROM Tservice t WHERE t.svcid = :svcid ORDER BY t.svcid")
@NamedQuery(name="Tservice.TotalCnt", query="SELECT COUNT(t.svcid) cnt FROM Tservice t")
public class Tservice implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String svcid = "";

	private String svceng = "";

	private String svckind  = "";

	private String svckor = "";
	
	@Transient
	private boolean isNew = false ;
	
	public Tservice() {
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public String getSvcid() {
		return this.svcid;
	}

	public void setSvcid(String svcid) {
		this.svcid = svcid;
	}

	public String getSvceng() {
		return this.svceng;
	}

	public void setSvceng(String svceng) {
		this.svceng = svceng;
	}

	public String getSvckind() {
		return this.svckind;
	}

	public void setSvckind(String svckind) {
		this.svckind = svckind;
	}

	public String getSvckor() {
		return (this.svckor == null ? "": this.svckor );
	}

	public void setSvckor(String svckor) {
		this.svckor = svckor;
	}
}