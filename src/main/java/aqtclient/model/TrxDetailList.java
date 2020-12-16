package aqtclient.model;

public class TrxDetailList {
    private String svcid;
    private String svckor;
    private String scrno;
    private Long tcnt;
    private Double avgt;
    private Long scnt;
    private Long fcnt;
 
	
	public TrxDetailList(String svcid, String svckor, String scrno,
			Long tcnt, Double avgt, Long scnt, Long fcnt) {
		this.svcid = svcid;
		this.svckor = svckor;
		this.scrno = scrno;
		this.tcnt = tcnt;
		this.avgt = avgt;
		this.scnt = scnt;
		this.fcnt = fcnt;
	}
	
    
    public String getSvcid() {
        return svcid;
    }
 
    public void setSvcid(String svcid) {
        this.svcid = svcid;
    }

    public String getSvckor() {
        return svckor;
    }
 
    public void setSvckor(String svckor) {
        this.svckor = svckor;
    }
    
    public String getScrno() {
        return scrno;
    }
 
    public void setScrno(String scrno) {
        this.scrno = scrno;
    }
   
    public Long getTcnt() {
        return tcnt;
    }
 
    public void setTcnt(Long tcnt) {
        this.tcnt = tcnt;
    }
    
    public Double getAvgt() {
        return avgt;
    }
 
    public void setAvgt1(Double avgt) {
        this.avgt = avgt;
    }
   
    public Long getScnt() {
        return scnt;
    }
 
    public void setScnt(Long scnt) {
        this.scnt = scnt;
    }
    
    public Long getFcnt() {
        return fcnt;
    }
 
    public void setFcnt(Long fcnt) {
        this.fcnt = fcnt;
    }

 
}



