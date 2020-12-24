-- --------------------------------------------------------
-- 호스트:                          127.0.0.1
-- 서버 버전:                        10.5.8-MariaDB - mariadb.org binary distribution
-- 서버 OS:                        Win64
-- HeidiSQL 버전:                  11.1.0.6116
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- 테이블 aqtdb2.tconfig 구조 내보내기
CREATE TABLE IF NOT EXISTS `tconfig` (
  `id` int(11) NOT NULL DEFAULT 1,
  `pass1` varchar(50) DEFAULT NULL COMMENT '테스트admin passwd',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 aqtdb2.texecjob 구조 내보내기
CREATE TABLE IF NOT EXISTS `texecjob` (
  `pkey` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `tcode` varchar(50) NOT NULL DEFAULT '',
  `tdesc` varchar(80) NOT NULL DEFAULT '' COMMENT '테스트설명',
  `tnum` smallint(5) unsigned NOT NULL DEFAULT 10 COMMENT '쓰레드 수',
  `dbskip` char(1) NOT NULL DEFAULT '0' COMMENT '1. dbupdate skip',
  `etc` varchar(256) NOT NULL DEFAULT '' COMMENT '기타 선택조건',
  `inlog` varchar(50) NOT NULL DEFAULT '' COMMENT '입력로그파일',
  `outlogdir` varchar(50) NOT NULL DEFAULT '' COMMENT 'out로그위치',
  `tuser` varchar(50) NOT NULL DEFAULT '',
  `tdir` varchar(50) NOT NULL DEFAULT '',
  `tenv` varchar(50) NOT NULL DEFAULT '',
  `reqstartDt` datetime NOT NULL DEFAULT curdate() COMMENT '작업시작요청일시',
  `exectype` smallint(5) unsigned NOT NULL DEFAULT 0 COMMENT '0.일괄실행  1.송신시간에 맞추어',
  `resultstat` smallint(5) unsigned NOT NULL DEFAULT 0 COMMENT '0. 미실행 1.수행중  2.완료 3.실행오류',
  `reqnum` smallint(5) unsigned NOT NULL DEFAULT 0 COMMENT '재요청횟수',
  `startDt` datetime DEFAULT NULL COMMENT '작업시작시간',
  `endDt` datetime DEFAULT NULL COMMENT '작업종료시간',
  `msg` text DEFAULT NULL COMMENT '작업메세지',
  PRIMARY KEY (`pkey`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8 COMMENT='테스트작업요청';

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 aqtdb2.tmaster 구조 내보내기
CREATE TABLE IF NOT EXISTS `tmaster` (
  `tcode` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `code` varchar(20) NOT NULL,
  `type` char(1) DEFAULT '1' COMMENT '1.배치테스트 2.실시간',
  `lvl` char(1) DEFAULT '1' COMMENT '0.대상아님 1.단위  2.통합테스트',
  `desc1` varchar(50) DEFAULT NULL,
  `cmpCode` varchar(20) DEFAULT NULL COMMENT '주비교테스트',
  `tdate` varchar(10) DEFAULT NULL COMMENT '테스트시작일',
  `endDate` varchar(10) DEFAULT NULL COMMENT '테스트종료일',
  `tdir` varchar(80) DEFAULT NULL,
  `tuser` varchar(20) DEFAULT NULL,
  `thost` varchar(50) DEFAULT NULL,
  `tport` varchar(5) DEFAULT NULL,
  `tenv` varchar(50) DEFAULT NULL COMMENT '별도환경파일위치',
  PRIMARY KEY (`tcode`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='테스트 기본정보';

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 aqtdb2.trequest 구조 내보내기
CREATE TABLE IF NOT EXISTS `trequest` (
  `pkey` int(10) unsigned NOT NULL,
  `tcode` varchar(50) NOT NULL DEFAULT '',
  `uuid` char(32) NOT NULL DEFAULT '',
  `reqUser` varchar(50) NOT NULL DEFAULT '' COMMENT '요청자',
  `reqDt` timestamp NULL DEFAULT current_timestamp() COMMENT '요청일시',
  PRIMARY KEY (`pkey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='tr재전송요청';

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 aqtdb2.tservice 구조 내보내기
CREATE TABLE IF NOT EXISTS `tservice` (
  `svcid` varchar(40) NOT NULL COMMENT '서비스id',
  `svckor` varchar(60) DEFAULT NULL COMMENT '한글서비스명',
  `svceng` varchar(60) DEFAULT NULL COMMENT '영문서비스명',
  `svckind` char(1) DEFAULT NULL COMMENT '서비스종류',
  `cumcnt` int(10) unsigned DEFAULT 0 COMMENT '누적건수',
  PRIMARY KEY (`svcid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='서비스 id / name';

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 이벤트 aqtdb2.tservice_cnt_upd 구조 내보내기
DELIMITER //
CREATE EVENT `tservice_cnt_upd` ON SCHEDULE EVERY 1 DAY STARTS '2020-06-02 16:09:55' ON COMPLETION PRESERVE ENABLE COMMENT '서비스별 총누적건수 업데이트' DO BEGIN

UPDATE tservice a, (SELECT svcid ,COUNT(1) cnt FROM Ttransaction t, tmaster m 
WHERE m.code = t.tcode and m.lvl > '0' GROUP BY svcid) b  SET cumcnt = b.cnt WHERE a.svcid = b.svcid ;

END//
DELIMITER ;

-- 테이블 aqtdb2.ttcppacket 구조 내보내기
CREATE TABLE IF NOT EXISTS `ttcppacket` (
  `pkey` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `tcode` varchar(50) NOT NULL,
  `o_stime` datetime(6) NOT NULL COMMENT 'org 송신시간',
  `stime` datetime(6) NOT NULL COMMENT '송신시간',
  `rtime` datetime(6) NOT NULL COMMENT '수신시간',
  `svctime` double(22,6) NOT NULL DEFAULT (`rtime` - `stime`) COMMENT '순수서비스소요시간',
  `elapsed` double(22,6) DEFAULT NULL,
  `srcip` varchar(30) DEFAULT NULL COMMENT '소스ip',
  `srcport` int(10) unsigned DEFAULT NULL COMMENT '소스port',
  `dstip` varchar(30) DEFAULT NULL COMMENT '목적지ip',
  `dstport` int(10) unsigned DEFAULT NULL COMMENT '목적지port',
  `proto` char(1) DEFAULT NULL COMMENT '0.tcp 1.http 2.https',
  `method` varchar(20) DEFAULT NULL COMMENT 'method',
  `uri` varchar(4096) DEFAULT NULL,
  `seqno` int(10) unsigned DEFAULT NULL,
  `ackno` int(10) unsigned DEFAULT NULL,
  `rcode` varchar(10) DEFAULT NULL COMMENT 'return code',
  `slen` int(10) unsigned DEFAULT NULL COMMENT '송신데이터길이',
  `rlen` int(10) unsigned DEFAULT NULL COMMENT '수신데이터길이',
  `sdata` mediumblob DEFAULT NULL COMMENT '송신데이터',
  `rdata` mediumblob DEFAULT NULL COMMENT '수신데이터',
  `cdate` datetime DEFAULT NULL COMMENT '생성일시',
  PRIMARY KEY (`pkey`)
) ENGINE=InnoDB AUTO_INCREMENT=95 DEFAULT CHARSET=utf8;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 aqtdb2.ttransaction 구조 내보내기
CREATE TABLE IF NOT EXISTS `ttransaction` (
  `pkey` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `uuid` char(32) NOT NULL COMMENT 'uuid',
  `tcode` varchar(50) NOT NULL COMMENT '테스트코드',
  `svrnm` varchar(50) DEFAULT NULL COMMENT '서버명',
  `svcid` varchar(20) DEFAULT NULL COMMENT '서비스id',
  `o_stime` datetime(6) DEFAULT NULL COMMENT 'asis송신시간',
  `stime` datetime(6) DEFAULT NULL COMMENT '송신시간',
  `rtime` datetime(6) DEFAULT NULL COMMENT '수신시간',
  `userid` varchar(20) DEFAULT NULL COMMENT '사용자id',
  `clientIp` varchar(40) DEFAULT NULL COMMENT '사용자ip',
  `scrno` varchar(20) DEFAULT NULL COMMENT '화면ID',
  `msgcd` varchar(10) DEFAULT NULL COMMENT '수신메세지코드',
  `rcvmsg` varchar(120) DEFAULT NULL COMMENT '수신메세지',
  `errinfo` varchar(120) DEFAULT NULL,
  `sflag` char(1) DEFAULT NULL COMMENT '1.성공 2.실패',
  `async` char(1) DEFAULT NULL COMMENT '0.tpacall  1.tpcall',
  `elapsed` double DEFAULT NULL COMMENT '총소요시간',
  `svctime` double DEFAULT NULL COMMENT '순수서비스소요시간',
  `slen` int(10) unsigned DEFAULT NULL COMMENT '송신데이터길이',
  `rlen` int(10) unsigned DEFAULT NULL COMMENT '수신데이터길이',
  `sdata` mediumblob DEFAULT NULL COMMENT '송신데이터',
  `rdata` mediumblob DEFAULT NULL COMMENT '수신데이터',
  `cdate` datetime DEFAULT current_timestamp() COMMENT '생성시간',
  PRIMARY KEY (`pkey`),
  KEY `tcode_uuid` (`tcode`,`uuid`),
  KEY `tcode_svcid_stime` (`tcode`,`svcid`,`o_stime`)
) ENGINE=InnoDB AUTO_INCREMENT=2107 DEFAULT CHARSET=utf8 COMMENT='거래데이터 ';

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 aqtdb2.ttrxlist 구조 내보내기
CREATE TABLE IF NOT EXISTS `ttrxlist` (
  `tcode` varchar(20) NOT NULL,
  `svc_cnt` int(11) NOT NULL,
  `fsvc_cnt` int(11) NOT NULL,
  `data_cnt` int(11) NOT NULL,
  `scnt` int(11) NOT NULL,
  `fcnt` int(11) NOT NULL,
  `UDATE` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`tcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 뷰 aqtdb2.vtrxdetail 구조 내보내기
-- VIEW 종속성 오류를 극복하기 위해 임시 테이블을 생성합니다.
CREATE TABLE `vtrxdetail` (
	`pkey` BIGINT(21) UNSIGNED NOT NULL,
	`tcode` VARCHAR(50) NOT NULL COMMENT '테스트코드' COLLATE 'utf8_general_ci',
	`svcid` VARCHAR(20) NULL COMMENT '서비스id' COLLATE 'utf8_general_ci',
	`scrno` VARCHAR(20) NULL COMMENT '화면ID' COLLATE 'utf8_general_ci',
	`svckor` VARCHAR(60) NULL COMMENT '한글서비스명' COLLATE 'utf8_general_ci',
	`tcnt` BIGINT(21) NOT NULL,
	`avgt` DOUBLE NULL,
	`scnt` DECIMAL(22,0) NULL,
	`fcnt` DECIMAL(22,0) NULL,
	`cumcnt` DECIMAL(42,0) NULL
) ENGINE=MyISAM;

-- 뷰 aqtdb2.vtrxlist 구조 내보내기
-- VIEW 종속성 오류를 극복하기 위해 임시 테이블을 생성합니다.
CREATE TABLE `vtrxlist` (
	`code` VARCHAR(20) NOT NULL COLLATE 'utf8_general_ci',
	`type` CHAR(1) NULL COMMENT '1.배치테스트 2.실시간' COLLATE 'utf8_general_ci',
	`lvl` CHAR(1) NULL COMMENT '0.대상아님 1.단위  2.통합테스트' COLLATE 'utf8_general_ci',
	`desc1` VARCHAR(50) NULL COLLATE 'utf8_general_ci',
	`cmpCode` VARCHAR(20) NULL COMMENT '주비교테스트' COLLATE 'utf8_general_ci',
	`tdate` VARCHAR(10) NULL COMMENT '테스트시작일' COLLATE 'utf8_general_ci',
	`endDate` VARCHAR(10) NULL COMMENT '테스트종료일' COLLATE 'utf8_general_ci',
	`tdir` VARCHAR(80) NULL COLLATE 'utf8_general_ci',
	`tuser` VARCHAR(20) NULL COLLATE 'utf8_general_ci',
	`thost` VARCHAR(50) NULL COLLATE 'utf8_general_ci',
	`tport` VARCHAR(5) NULL COLLATE 'utf8_general_ci',
	`tenv` VARCHAR(50) NULL COMMENT '별도환경파일위치' COLLATE 'utf8_general_ci',
	`svc_cnt` BIGINT(21) NOT NULL,
	`fsvc_cnt` BIGINT(21) NOT NULL,
	`data_cnt` BIGINT(21) NOT NULL,
	`scnt` DECIMAL(22,0) NOT NULL,
	`fcnt` DECIMAL(22,0) NOT NULL,
	`spct` DECIMAL(29,4) NOT NULL,
	`tot_svccnt` BIGINT(21) NOT NULL
) ENGINE=MyISAM;

-- 뷰 aqtdb2.vtrxdetail 구조 내보내기
-- 임시 테이블을 제거하고 최종 VIEW 구조를 생성
DROP TABLE IF EXISTS `vtrxdetail`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `vtrxdetail` AS select uuid_short() pkey, a.tcode, a.svcid, a.scrno, s.svckor svckor, a.tcnt, a.avgt ,a.scnt ,a.fcnt,
       sum(tcnt) OVER (PARTITION BY a.svcid) cumcnt
from   (
select t.tcode, t.svcid, t.scrno, count(1) tcnt, avg(t.svctime) avgt, sum(case when t.sflag = '1' then 1 else 0 end) scnt
, sum(case when t.sflag = '2' then 1 else 0 end) fcnt
from   Ttransaction t, tmaster m where m.code = t.tcode and m.lvl > '0'
group by t.tcode, t.svcid, t.scrno
) as a
left outer join Tservice s on a.svcid = s.svcid ;

-- 뷰 aqtdb2.vtrxlist 구조 내보내기
-- 임시 테이블을 제거하고 최종 VIEW 구조를 생성
DROP TABLE IF EXISTS `vtrxlist`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `vtrxlist` AS SELECT      t.code, `type`, t.lvl, desc1, cmpCode, tdate, endDate, tdir, tuser, thost, tport, tenv,
				ifnull(t.svc_cnt, 0) svc_cnt,
				ifnull(t.fsvc_cnt, 0) fsvc_cnt,
            ifnull(t.data_cnt, 0) data_cnt,
            ifnull(t.scnt, 0) scnt,
            ifnull(t.fcnt, 0) fcnt,
            ifnull(scnt * 100 / (scnt+fcnt) ,0)  spct,
            ifnull(tot_svccnt,0) tot_svccnt
from 
( SELECT m.*, count(distinct svcid) svc_cnt
, count(distinct case when sflag = '2' then svcid end ) fsvc_cnt
, count(1) data_cnt
, sum(case when sflag = '1' then 1 else 0 end) scnt
, sum(case when sflag = '2' then 1 else 0 end) fcnt
 from  tmaster m ,ttransaction x WHERE m.code = x.tcode AND m.lvl > '0'
 group by tcode )  t 
 left join 
 (select lvl, count(distinct svcid) tot_svccnt from  ttransaction join tmaster m 
   on tcode = m.code AND lvl > '0' group by lvl) tot 
on t.lvl = tot.lvl ;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
