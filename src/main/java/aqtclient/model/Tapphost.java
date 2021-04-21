package aqtclient.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tapphosts database table.
 * 
 */
@Entity
@Table(name="tapphosts")
@NamedQuery(name="Tapphost.findAll", query="SELECT t FROM Tapphost t")
public class Tapphost implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int pkey;

	private String thost;

	private int tport;

	//bi-directional many-to-one association to Tapplication
	@ManyToOne
	@JoinColumn(name="appid")
	private Tapplication tapplication;

	public Tapphost() {
	}

	public int getPkey() {
		return this.pkey;
	}

	public void setPkey(int pkey) {
		this.pkey = pkey;
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

	public Tapplication getTapplication() {
		return this.tapplication;
	}

	public void setTapplication(Tapplication tapplication) {
		this.tapplication = tapplication;
	}

}