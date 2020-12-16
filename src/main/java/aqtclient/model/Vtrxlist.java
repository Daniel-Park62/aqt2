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
@NamedNativeQuery(name="Vtrxlist.findByCode", query="SELECT a.svcid,  MAX(a.svckor) svckor, a.scrno, SUM(a.tcnt1) tcnt1, SUM(a.avgt1) avgt1, SUM(a.scnt1) scnt1, SUM(a.fcnt1) fcnt1, SUM(a.tcnt2) tcnt2, SUM(a.avgt2) avgt2, SUM(a.scnt2) scnt2, SUM(a.fcnt2) fcnt2 " + 
												 " FROM ( " + 
												 " SELECT v.svcid, v.scrno, v.svckor, v.tcnt tcnt1, v.avgt avgt1, v.scnt scnt1 " + 
												 " , fcnt fcnt1, 0 tcnt2, 0 avgt2, 0 scnt2, 0 fcnt2 " + 
												 " FROM vtrxdetail v " + 
												 " WHERE v.tcode = ?" + 
												 " UNION ALL " + 
												 " SELECT v.svcid, v.scrno, v.svckor, 0, 0, 0, 0, v.tcnt tcnt1, v.avgt avgt1, v.scnt scnt1 " + 
												 " , v.fcnt fcnt1 " + 
												 " FROM vtrxdetail v " + 
												 " WHERE v.tcode = ? " + 
												 " ) AS a " + 
												 " GROUP BY a.svcid, a.scrno")
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
		return (double)this.scnt * 100 / (this.scnt+this.fcnt);
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