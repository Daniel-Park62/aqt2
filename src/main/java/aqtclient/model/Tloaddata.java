package aqtclient.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the Tloaddata database table.
 * 
 */
@Entity
@Table(name="Tloaddata")
public class Tloaddata implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkey;

	private long ackno;

	@Temporal(TemporalType.TIMESTAMP)
	private Date cdate;

	private String dstip;

	private int dstport;

	private double elapsed;

	private String method;

//	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="o_stime")
	private LocalDateTime oStime;

	private String proto;

	private int rcode;
	
	private char sflag ;
	
	private String rhead;

	private String errinfo;

	@Lob
	private byte[] rdata;

	private int rlen;

//	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime rtime;

	@Lob
	private byte[] sdata;

	private long seqno;

	private int slen;

	private String srcip;

	private int srcport;

//	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime stime;

	@Column(updatable=false, insertable=false )
	private double svctime;

	private String tcode;

	private String uri;

	@ManyToOne(targetEntity = Tmaster.class)
	@JoinColumn(name = "tcode", referencedColumnName = "code" ,updatable=false, insertable=false ) 
	public Tmaster tmaster ;

	@ManyToOne(targetEntity = Tservice.class)
	@JoinColumn(name = "uri", referencedColumnName = "svcid" ,updatable=false, insertable=false ) 
	public Tservice tservice ;

	public Tloaddata() {
	}

	public Tmaster getTmaster() {
		return tmaster;
	}

	public Tservice getTservice() {
		return (tservice == null ? new Tservice() : tservice );
	}

	public long getPkey() {
		return this.pkey;
	}

	public char getSflag() {
		return sflag ;
	}

	public void setPkey(int pkey) {
		this.pkey = pkey;
	}

	public long getAckno() {
		return this.ackno;
	}

	public void setAckno(int ackno) {
		this.ackno = ackno;
	}

	public Date getCdate() {
		return this.cdate;
	}

	public void setCdate(Date cdate) {
		this.cdate = cdate;
	}

	public String getDstip() {
		return this.dstip;
	}

	public void setDstip(String dstip) {
		this.dstip = dstip;
	}

	public int getDstport() {
		return this.dstport;
	}

	public void setDstport(int dstport) {
		this.dstport = dstport;
	}

	public double getElapsed() {
		return this.elapsed;
	}

	public void setElapsed(double elapsed) {
		this.elapsed = elapsed;
	}

	public String getMethod() {
		return this.method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public LocalDateTime getOStime() {
		return this.oStime;
	}

	public void setOStime(LocalDateTime oStime) {
		this.oStime = oStime;
	}

	public String getProto() {
		return this.proto;
	}

	public void setProto(String proto) {
		this.proto = proto;
	}

	public int getRcode() {
		return this.rcode ;
	}

	public String getRhead() {
		return this.rhead == null ? "" : this.rhead ;
	}

	public String getErrinfo() {
		return errinfo == null ? "" : errinfo ;
	}

	public void setErrinfo(String errinfo) {
		this.errinfo = errinfo;
	}

	public String getRdata()  {
//		try {
//			return this.rdata == null ? "" : new String(this.rdata, rhead.toLowerCase().contains("utf")   ? "utf-8" : "euc-kr") ;
			return this.rdata == null ? "" : new String(this.rdata)   ;
//		} catch (UnsupportedEncodingException e) {
//			return "";
//		}
	}

	public String getRdataUTF()  {
		try {
			return this.rdata == null ? "" : new String(this.rdata, "utf-8") ;
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	public String getRdataENCODE(String enc)  {
		try {
			return this.rdata == null ? "" : new String(this.rdata, enc) ;
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	public String getRdataENCODE(String enc, int ln)  {
		if ( rdata == null ) return "";
		try {
			byte[] rdatam = new byte[ ln  ] ;
			for (int i = 0; i < rdatam.length && i < rdata.length ; i++) {
				rdatam[i] = rdata[i] ;
			}
			return new String(rdatam, enc) ;
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	public String getRdatam()  {
		if ( rdata == null ) return "";
		
		try {
			byte[] rdatam = new byte[ rdata.length > 250 ? 250 : rdata.length  ] ;
			for (int i = 0; i < rdatam.length ; i++) {
				rdatam[i] = rdata[i] ;
			}
			return  new String( rdatam, "utf-8" )  ;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			return "" ;
		}
	}
	
	public byte[] getRdatab() {
		return this.rdata == null ? "".getBytes() : this.rdata ;
	}

	public int getRlen() {
		return this.rlen;
	}

	public void setRlen(int rlen) {
		this.rlen = rlen;
	}

	public LocalDateTime getRtime() {
		return this.rtime;
	}

	public void setRtime(LocalDateTime rtime) {
		this.rtime = rtime;
	}

	public String getSdata() {
		return this.sdata == null ? "" : new String(this.sdata) ;
	}
	
	public byte[] getSdatab() {
		return sdata ;
	}
	public void setSdata(String sdata) {
		this.sdata = sdata.getBytes() ;
	}
	public long getSeqno() {
		return this.seqno;
	}

	public void setSeqno(int seqno) {
		this.seqno = seqno;
	}

	public int getSlen() {
		return this.slen;
	}

	public void setSlen(int slen) {
		this.slen = slen;
	}

	public String getSrcip() {
		return this.srcip;
	}

	public void setSrcip(String srcip) {
		this.srcip = srcip;
	}

	public int getSrcport() {
		return this.srcport;
	}

	public void setSrcport(int srcport) {
		this.srcport = srcport;
	}

	public LocalDateTime getStime() {
		return this.stime;
	}

	public void setStime(LocalDateTime stime) {
		this.stime = stime;
	}

	public double getSvctime() {
		return this.svctime;
	}

	public void setSvctime(double svctime) {
		this.svctime = svctime;
	}

	public String getTcode() {
		return this.tcode;
	}

	public void setTcode(String tcode) {
		this.tcode = tcode;
	}

	public String getUri() {
		return this.uri;
	}

}