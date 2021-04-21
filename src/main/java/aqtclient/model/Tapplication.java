package aqtclient.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the tapplication database table.
 * 
 */
@Entity
@NamedQuery(name="Tapplication.findAll", query="SELECT t FROM Tapplication t")
public class Tapplication implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String appid;

	private String appnm;

	private String manager;

	//bi-directional many-to-one association to Tapphost
	@OneToMany(mappedBy="tapplication")
	private List<Tapphost> tapphosts;

	public Tapplication() {
	}

	public String getAppid() {
		return this.appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getAppnm() {
		return this.appnm;
	}

	public void setAppnm(String appnm) {
		this.appnm = appnm;
	}

	public String getManager() {
		return this.manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public List<Tapphost> getTapphosts() {
		return this.tapphosts;
	}

	public void setTapphosts(List<Tapphost> tapphosts) {
		this.tapphosts = tapphosts;
	}

	public Tapphost addTapphost(Tapphost tapphost) {
		getTapphosts().add(tapphost);
		tapphost.setTapplication(this);

		return tapphost;
	}

	public Tapphost removeTapphost(Tapphost tapphost) {
		getTapphosts().remove(tapphost);
		tapphost.setTapplication(null);

		return tapphost;
	}

}