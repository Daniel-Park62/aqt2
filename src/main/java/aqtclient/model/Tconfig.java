package aqtclient.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tconfig database table.
 * 
 */
@Entity
@NamedQuery(name="Tconfig.findAll", query="SELECT t FROM Tconfig t")
@NamedQuery(name="Tconfig.findById", query="SELECT t FROM Tconfig t WHERE t.id = CAST(:id AS INT)")
public class Tconfig implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String pass1;

	public Tconfig() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPass1() {
		return this.pass1;
	}

	public void setPass1(String pass1) {
		this.pass1 = pass1;
	}

}