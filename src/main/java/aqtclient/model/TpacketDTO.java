package aqtclient.model;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Date;
/**
 * The tcppacket dto.
 * 
 */
public class TpacketDTO  {

	private long pkey;
	private String tcode;
	private long cmpid;
	private String uri;
	private LocalDateTime oStime;
	private LocalDateTime stime;
	private LocalDateTime rtime;
	private double svctime;
	private byte[] rdata;
	private int rlen;
	private String rhead;
	private char sflag ;
	private String errinfo;
	private byte[] sdata;
	private long ackno;
	private Date cdate;
	private String dstip;
	private int dstport;
	private double elapsed;
	private String method;
	private String proto;
	private int rcode;
	private long seqno;
	private int slen;
	private String srcip;
	private int srcport;
	public Tmaster tmaster ;
	public Tservice tservice ;
	public TpacketDTO() {
	}

	public TpacketDTO(Ttcppacket tp) {
			
		pkey = tp.getPkey() ;
		tcode = tp.getTcode() ;
		ackno = tp.getAckno() ;
		cdate = tp.getCdate() ;
		cmpid = tp.getCmpid() ;
		dstip = tp.getDstip() ;
		dstport = tp.getDstport() ;
		elapsed = tp.getElapsed() ;
		errinfo = tp.getErrinfo() ;
		method = tp.getMethod() ;
		oStime = tp.getOStime() ;
		proto = tp.getProto() ;
		rcode = tp.getRcode() ;
		rdata = tp.getRdatab() ;
		rhead = tp.getRhead() ;
		rlen = tp.getRlen() ;
		rtime = tp.getRtime() ;
		sdata = tp.getSdatab() ;
		seqno = tp.getSeqno() ;
		sflag = tp.getSflag() ;
		slen  =  tp.getSlen() ;
		srcip = tp.getSrcip() ;
		srcport = tp.getSrcport() ;
		stime = tp.getStime() ;
		svctime = tp.getSvctime() ;
		tmaster = tp.getTmaster() ;
		tservice = tp.getTservice() ;
		uri = tp.getUri() ;
	}

	public TpacketDTO(Tloaddata tp) {
		
		pkey = tp.getPkey() ;
		tcode = tp.getTcode() ;
		ackno = tp.getAckno() ;
		cdate = tp.getCdate() ;
		cmpid = tp.getCmpid() ;
		dstip = tp.getDstip() ;
		dstport = tp.getDstport() ;
		elapsed = tp.getElapsed() ;
		errinfo = tp.getErrinfo() ;
		method = tp.getMethod() ;
		oStime = tp.getOStime() ;
		proto = tp.getProto() ;
		rcode = tp.getRcode() ;
		rdata = tp.getRdatab() ;
		rhead = tp.getRhead() ;
		rlen = tp.getRlen() ;
		rtime = tp.getRtime() ;
		sdata = tp.getSdatab() ;
		seqno = tp.getSeqno() ;
		sflag = tp.getSflag() ;
		slen  =  tp.getSlen() ;
		srcip = tp.getSrcip() ;
		srcport = tp.getSrcport() ;
		stime = tp.getStime() ;
		svctime = tp.getSvctime() ;
		tmaster = tp.getTmaster() ;
		tservice = tp.getTservice() ;
		uri = tp.getUri() ;
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
		return this.rdata == null ? "" : new String(this.rdata)   ;
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
	public void setRdata(byte[] rdata) {
		this.rdata = rdata ;
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
	public void setSdata(byte[] sdata) {
		this.sdata = sdata ;
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
}