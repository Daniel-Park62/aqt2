package aqtclient.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the thostmap database table.
 * 
 */
@Entity
@NamedQuery(name="Thostmap.findAll", query="SELECT t FROM Thostmap t")
@NamedQuery(name="Thostmap.findCode", query="SELECT t FROM Thostmap t where t.tcode = :tcode")
public class Thostmap implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int pkey;

	@ManyToOne
	@JoinColumn(name = "tcode", referencedColumnName = "code" ,updatable=false, insertable=false )
	private Tmaster tmaster ;

	private String tcode;

	private String thost = "";

	private String thost2;

	private int tport;

	private int tport2;

	public Thostmap() {
	}

	public int getPkey() {
		return this.pkey;
	}

//	public void setPkey(int pkey) {
//		this.pkey = pkey;
//	}


	public Tmaster getTmaster() {
		return tmaster;
	}

//	public void setTmaster(Tmaster tmaster) {
//		this.tmaster = tmaster;
//	}

	public String getTcode() {
		return tcode;
	}

	public void setTcode(String tcode) {
		this.tcode = tcode;
	}

	public String getThost() {
		return this.thost;
	}

	public void setThost(String thost) {
		this.thost = thost;
	}

	public String getThost2() {
		return this.thost2 != null ? this.thost2 : "";
	}

	public void setThost2(String thost2) {
		this.thost2 = thost2;
	}

	public int getTport() {
		return this.tport;
	}

	public void setTport(int tport) {
		this.tport = tport;
	}

	public int getTport2() {
		return this.tport2;
	}

	public void setTport2(int tport2) {
		this.tport2 = tport2;
	}

}