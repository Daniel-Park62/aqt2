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
	private String appid = "";

	private String svceng = "";

	private String svckind  = "";

	private String svckor = "";
	private String task = "";
	private String manager = "";
	
	@Transient
	private boolean newFlag = false ;
	
	public Tservice() {
	}

	public boolean isNew() {
		return newFlag;
	}

	public void setNew(boolean isNew) {
		this.newFlag = isNew;
	}

	public String getSvcid() {
		return this.svcid;
	}

	public void setSvcid(String svcid) {
		this.svcid = svcid;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
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

	public String getTask() {
		return task == null ? "" : task ;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getManager() {
		return manager == null ? "" : manager ;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}
}