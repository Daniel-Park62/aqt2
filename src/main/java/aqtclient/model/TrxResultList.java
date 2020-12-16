package aqtclient.model;

public class TrxResultList {
    private String svcid;
    private String svckor;
    private String scrno;
    private Long tcnt1;
    private Double avgt1;
    private Long scnt1;
    private Long fcnt1;
    private Long tcnt2;
    private Double avgt2;
    private Long scnt2;
    private Long fcnt2;
 
	
	public TrxResultList(String svcid, String svckor, String scrno,
			Long tcnt1, Double avgt1, Long scnt1, Long fcnt1,
			Long tcnt2, Double avgt2, Long scnt2, Long fcnt2) {
		this.svcid = svcid;
		this.svckor = svckor;
		this.scrno = scrno;
		this.tcnt1 = tcnt1;
		this.avgt1 = avgt1;
		this.scnt1 = scnt1;
		this.fcnt1 = fcnt1;
		this.tcnt2 = tcnt2;
		this.avgt2 = avgt2;
		this.scnt2 = scnt2;
		this.fcnt2 = fcnt2;
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
   
    public Long getTcnt1() {
        return tcnt1;
    }
 
    public void setTcnt1(Long tcnt1) {
        this.tcnt1 = tcnt1;
    }
    
    public Double getAvgt1() {
        return avgt1;
    }
 
    public void setAvgt1(Double avgt1) {
        this.avgt1 = avgt1;
    }
   
    public Long getScnt1() {
        return scnt1;
    }
 
    public void setScnt1(Long scnt1) {
        this.scnt1 = scnt1;
    }
    
    public Long getFcnt1() {
        return fcnt1;
    }
 
    public void setFcnt1(Long fcnt1) {
        this.fcnt1 = fcnt1;
    }
 
    public Long getTcnt2() {
        return tcnt2;
    }
 
    public void setTcnt2(Long tcnt2) {
        this.tcnt2 = tcnt2;
    }
    
    public Double getAvgt2() {
        return avgt2;
    }
 
    public void setAvgt2(Double avgt2) {
        this.avgt2 = avgt2;
    }
    
    public Long getScnt2() {
        return scnt2;
    }
 
    public void setScnt2(Long scnt2) {
        this.scnt2 = scnt2;
    }
    
    public Long getFcnt2() {
        return fcnt2;
    }
 
    public void setFcnt2(Long fcnt2) {
        this.fcnt2 = fcnt2;
    }
 
}



