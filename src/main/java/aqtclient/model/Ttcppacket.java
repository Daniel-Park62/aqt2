package aqtclient.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the ttcppacket database table.
 * 
 */
@Entity
@NamedQuery(name="Ttcppacket.SvcCnt", query="SELECT COUNT(DISTINCT t.uri) FROM Ttcppacket t where t.tcode = :tcode")
@NamedQuery(name="Ttcppacket.FlagCnt", query="SELECT COUNT(t.pkey) trxCnt " +
		", COUNT(CASE WHEN t.sflag = '1' THEN 1 ELSE NULL END) validCnt " + 
		", COUNT(CASE WHEN t.sflag = '2' THEN 1 ELSE NULL END) invalidCnt " +
		" FROM Ttcppacket t WHERE t.tcode = :tcode")
@NamedNativeQuery(name="Ttcppacket.chartData", 
query="select date_format(t.stime, '%Y-%m-%d %H:%i:00') dtime, count(t.pkey) trxCnt from Ttcppacket t where t.tcode = ? group by date_format(t.stime, '%Y-%m-%d %H:%i:00') ")

public class Ttcppacket implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long pkey;

	private long cmpid;

	private String appid;

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
	private String col1;
	private String col2;


	@ManyToOne(targetEntity = Tmaster.class)
	@JoinColumn(name = "tcode", referencedColumnName = "code" ,updatable=false, insertable=false ) 
	private Tmaster tmaster ;

	@ManyToOne(targetEntity = Tservice.class)
	@JoinColumn(name = "uri", referencedColumnName = "svcid" ,updatable=false, insertable=false ) 
	private Tservice tservice ;

	@ManyToOne
	@JoinColumn(name="cmpid",updatable=false, insertable=false)
	private Tloaddata tloaddata ;
	
	public Ttcppacket() {
	}

	public Tmaster getTmaster() {
		return tmaster;
	}

	public Tloaddata getTloaddata() {
		return (tloaddata == null ? new Tloaddata() : tloaddata) ;
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
	public long getCmpid() {
		return cmpid;
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
	
	public String getSdataENCODE(String enc)  {
		try {
			return this.sdata == null ? "" : new String(this.sdata, enc) ;
		} catch (UnsupportedEncodingException e) {
			return "";
		}
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

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getSflagNm() {
		return this.rcode > 399 ? "실패" : this.rcode > 199 ? "성공" : "" ;
	}

	public String getAppid() {
		return appid;
	}
	public String getCol1() {
		return col1;
	}

	public void setCol1(String col1) {
		this.col1 = col1;
	}

	public String getCol2() {
		return col2;
	}

	public void setCol2(String col2) {
		this.col2 = col2;
	}


}