package aqtclient.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;

import org.eclipse.persistence.annotations.ReadOnly;


/**
 * The persistent class for the vtrxlist database table.
 * 
 */
@Entity
@ReadOnly
@NamedQuery(name="Vtrxlist.findAll", query="SELECT v FROM Vtrxlist v  order by v.tdate desc")
public class Vtrxlist  {
	
	@Id
	private String code;

	@Column(name="data_cnt")
	private Long dataCnt;

	private String desc1;
	private String lvl;   //  1.단위테스트 3.통합테스트

	private Long fcnt;

	private Long scnt;

	@Column(name="svc_cnt")
	private Long svcCnt;

	@Column(name="fsvc_cnt")
	private Long fsvcCnt;

	@Column(name="tot_svccnt")
	private Long totSvcCnt;

	private String tdate;

	private String thost;
	
	private double spct ;
	
	public Vtrxlist() {
	}

	public String getCode() {
		return this.code;
	}

	public long getDataCnt() {
		return this.dataCnt;
	}

	public String getDesc1() {
		return this.desc1;
	}

	public Long getFcnt() {
		return this.fcnt;
	}

	public Long getScnt() {
		return this.scnt;
	}

	public double getSpct() {
		return this.spct;
	}

	public Long getSvcCnt() {
		return this.svcCnt;
	}

	public Long getFsvcCnt() {
		return fsvcCnt;
	}

	public Long getTotSvcCnt() {
		return this.totSvcCnt;
	}

	public String getTdate() {
		return this.tdate;
	}

	public String getThost() {
		return this.thost;
	}

	public String getLvl() {
		return this.lvl;
	}

	public String getLvlNm() {
		return "1".equals(this.lvl) ? "단위테스트" : "2".equals(this.lvl) ? "통합테스트" : "실시간" ;
	}

}