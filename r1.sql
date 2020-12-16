-- --------------------------------------------------------
-- 호스트:                          127.0.0.1
-- 서버 버전:                        10.5.8-MariaDB - mariadb.org binary distribution
-- 서버 OS:                        Win64
-- HeidiSQL 버전:                  11.0.0.5919
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 뷰 aqtdb2.tconfig 구조 내보내기
-- VIEW 종속성 오류를 극복하기 위해 임시 테이블을 생성합니다.
CREATE TABLE `tconfig` (
	`id` INT(11) NOT NULL,
	`pass1` VARCHAR(50) NULL COMMENT '테스트admin passwd' COLLATE 'utf8_general_ci'
) ;

-- 뷰 aqtdb2.texecjob 구조 내보내기
-- VIEW 종속성 오류를 극복하기 위해 임시 테이블을 생성합니다.
CREATE TABLE `texecjob` (
	`pkey` INT(10) UNSIGNED NOT NULL,
	`tcode` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`tdesc` VARCHAR(80) NOT NULL COMMENT '테스트설명' COLLATE 'utf8_general_ci',
	`tnum` SMALLINT(5) UNSIGNED NOT NULL COMMENT '쓰레드 수',
	`dbskip` CHAR(1) NOT NULL COMMENT '1. dbupdate skip' COLLATE 'utf8_general_ci',
	`etc` VARCHAR(256) NOT NULL COMMENT '기타 선택조건' COLLATE 'utf8_general_ci',
	`inlog` VARCHAR(50) NOT NULL COMMENT '입력로그파일' COLLATE 'utf8_general_ci',
	`outlogdir` VARCHAR(50) NOT NULL COMMENT 'out로그위치' COLLATE 'utf8_general_ci',
	`tuser` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`tdir` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`tenv` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`reqstartDt` DATETIME NOT NULL COMMENT '작업시작요청일시',
	`exectype` SMALLINT(5) UNSIGNED NOT NULL COMMENT '0.일괄실행  1.송신시간에 맞추어',
	`resultstat` SMALLINT(5) UNSIGNED NOT NULL COMMENT '0. 미실행 1.수행중  2.완료 3.실행오류',
	`reqnum` SMALLINT(5) UNSIGNED NOT NULL COMMENT '재요청횟수',
	`startDt` DATETIME NULL COMMENT '작업시작시간',
	`endDt` DATETIME NULL COMMENT '작업종료시간',
	`msg` TEXT(65535) NULL COMMENT '작업메세지' COLLATE 'utf8_general_ci'
) ;

-- 뷰 aqtdb2.tloaddata 구조 내보내기
-- VIEW 종속성 오류를 극복하기 위해 임시 테이블을 생성합니다.
CREATE TABLE `tloaddata` (
	`pkey` INT(10) UNSIGNED NOT NULL,
	`uuid` CHAR(32) NOT NULL COMMENT 'uuid' COLLATE 'utf8_general_ci',
	`svrnm` VARCHAR(50) NULL COMMENT '서버명' COLLATE 'utf8_general_ci',
	`svcid` VARCHAR(20) NULL COMMENT '서비스id' COLLATE 'utf8_general_ci',
	`o_stime` DATETIME(6) NULL COMMENT 'asis송신시간',
	`stime` DATETIME(6) NULL COMMENT '송신시간',
	`rtime` DATETIME(6) NULL COMMENT '수신시간',
	`userid` VARCHAR(20) NULL COMMENT '사용자id' COLLATE 'utf8_general_ci',
	`clientIp` VARCHAR(40) NULL COMMENT '사용자ip' COLLATE 'utf8_general_ci',
	`scrno` VARCHAR(20) NULL COMMENT '화면ID' COLLATE 'utf8_general_ci',
	`msgcd` VARCHAR(10) NULL COMMENT '수신메세지코드' COLLATE 'utf8_general_ci',
	`rcvmsg` TINYBLOB NULL COMMENT '수신메세지',
	`errinfo` TINYBLOB NULL,
	`sflag` CHAR(1) NULL COMMENT '1.성공 2.실패' COLLATE 'utf8_general_ci',
	`async` CHAR(1) NULL COMMENT '0.tpacall  1.tpcall' COLLATE 'utf8_general_ci',
	`elapsed` DOUBLE(22,0) NULL COMMENT '총소요시간',
	`svctime` DOUBLE(22,0) NULL COMMENT '순수서비스소요시간',
	`slen` INT(10) UNSIGNED NULL COMMENT '송신데이터길이',
	`rlen` INT(10) UNSIGNED NULL COMMENT '수신데이터길이',
	`sdata` MEDIUMBLOB NULL COMMENT '송신데이터',
	`rdata` MEDIUMBLOB NULL COMMENT '수신데이터',
	`cdate` DATETIME NULL COMMENT '생성시간'
) ;

-- 뷰 aqtdb2.tmaster 구조 내보내기
-- VIEW 종속성 오류를 극복하기 위해 임시 테이블을 생성합니다.
CREATE TABLE `tmaster` (
	`tcode` INT(10) UNSIGNED NOT NULL,
	`code` VARCHAR(20) NULL COLLATE 'utf8_general_ci',
	`type` CHAR(1) NULL COMMENT '1.tuxedo 2.tmax  3.http' COLLATE 'utf8_general_ci',
	`lvl` CHAR(1) NULL COMMENT '0.대상아님 1.단위  2.통합테스트' COLLATE 'utf8_general_ci',
	`desc1` VARCHAR(50) NULL COLLATE 'utf8_general_ci',
	`cmpCode` VARCHAR(20) NULL COMMENT '주비교테스트' COLLATE 'utf8_general_ci',
	`tdate` VARCHAR(10) NULL COMMENT '테스트시작일' COLLATE 'utf8_general_ci',
	`endDate` VARCHAR(10) NULL COMMENT '테스트종료일' COLLATE 'utf8_general_ci',
	`tdir` VARCHAR(80) NULL COLLATE 'utf8_general_ci',
	`tuser` VARCHAR(20) NULL COLLATE 'utf8_general_ci',
	`thost` VARCHAR(200) NULL COLLATE 'utf8_general_ci',
	`tport` VARCHAR(5) NULL COLLATE 'utf8_general_ci',
	`tenv` VARCHAR(50) NULL COMMENT '별도환경파일위치' COLLATE 'utf8_general_ci'
) ;

-- 뷰 aqtdb2.trequest 구조 내보내기
-- VIEW 종속성 오류를 극복하기 위해 임시 테이블을 생성합니다.
CREATE TABLE `trequest` (
	`pkey` INT(10) UNSIGNED NOT NULL,
	`tcode` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`uuid` CHAR(32) NOT NULL COLLATE 'utf8_general_ci',
	`reqUser` VARCHAR(50) NOT NULL COMMENT '요청자' COLLATE 'utf8_general_ci',
	`reqDt` TIMESTAMP NULL COMMENT '요청일시'
) ;

-- 뷰 aqtdb2.tservice 구조 내보내기
-- VIEW 종속성 오류를 극복하기 위해 임시 테이블을 생성합니다.
CREATE TABLE `tservice` (
	`svcid` VARCHAR(40) NOT NULL COMMENT '서비스id' COLLATE 'utf8_general_ci',
	`svckor` VARCHAR(60) NULL COMMENT '한글서비스명' COLLATE 'utf8_general_ci',
	`svceng` VARCHAR(60) NULL COMMENT '영문서비스명' COLLATE 'utf8_general_ci',
	`svckind` CHAR(1) NULL COMMENT '서비스종류' COLLATE 'utf8_general_ci',
	`cumcnt` INT(10) UNSIGNED NULL COMMENT '누적건수'
) ;

-- 뷰 aqtdb2.ttcppacket 구조 내보내기
-- VIEW 종속성 오류를 극복하기 위해 임시 테이블을 생성합니다.
CREATE TABLE `ttcppacket` (
	`pkey` INT(10) UNSIGNED NOT NULL,
	`tcode` VARCHAR(50) NOT NULL COLLATE 'utf8_general_ci',
	`o_stime` DATETIME(6) NOT NULL COMMENT 'org 송신시간',
	`stime` DATETIME(6) NOT NULL COMMENT '송신시간',
	`rtime` DATETIME(6) NOT NULL COMMENT '수신시간',
	`svctime` DOUBLE(22,0) NOT NULL COMMENT '순수서비스소요시간',
	`elapsed` DOUBLE(22,0) NULL,
	`srcip` VARCHAR(30) NULL COMMENT '소스ip' COLLATE 'utf8_general_ci',
	`srcport` INT(10) UNSIGNED NULL COMMENT '소스port',
	`dstip` VARCHAR(30) NULL COMMENT '목적지ip' COLLATE 'utf8_general_ci',
	`dstport` INT(10) UNSIGNED NULL COMMENT '목적지port',
	`proto` CHAR(1) NULL COMMENT '0.tcp 1.http 2.https' COLLATE 'utf8_general_ci',
	`method` VARCHAR(20) NULL COMMENT 'method' COLLATE 'utf8_general_ci',
	`uri` VARCHAR(4096) NULL COLLATE 'utf8_general_ci',
	`seqno` INT(10) UNSIGNED NULL,
	`ackno` INT(10) UNSIGNED NULL,
	`rcode` VARCHAR(10) NULL COMMENT 'return code' COLLATE 'utf8_general_ci',
	`slen` INT(10) UNSIGNED NULL COMMENT '송신데이터길이',
	`rlen` INT(10) UNSIGNED NULL COMMENT '수신데이터길이',
	`sdata` MEDIUMBLOB NULL COMMENT '송신데이터',
	`rdata` MEDIUMBLOB NULL COMMENT '수신데이터',
	`cdate` DATETIME NULL COMMENT '생성일시'
) ;

-- 뷰 aqtdb2.ttransaction 구조 내보내기
-- VIEW 종속성 오류를 극복하기 위해 임시 테이블을 생성합니다.
CREATE TABLE `ttransaction` (
	`pkey` INT(10) UNSIGNED NOT NULL,
	`uuid` CHAR(32) NOT NULL COMMENT 'uuid' COLLATE 'utf8_general_ci',
	`tcode` VARCHAR(50) NOT NULL COMMENT '테스트코드' COLLATE 'utf8_general_ci',
	`svrnm` VARCHAR(50) NULL COMMENT '서버명' COLLATE 'utf8_general_ci',
	`svcid` VARCHAR(20) NULL COMMENT '서비스id' COLLATE 'utf8_general_ci',
	`o_stime` DATETIME(6) NULL COMMENT 'asis송신시간',
	`stime` DATETIME(6) NULL COMMENT '송신시간',
	`rtime` DATETIME(6) NULL COMMENT '수신시간',
	`userid` VARCHAR(20) NULL COMMENT '사용자id' COLLATE 'utf8_general_ci',
	`clientIp` VARCHAR(40) NULL COMMENT '사용자ip' COLLATE 'utf8_general_ci',
	`scrno` VARCHAR(20) NULL COMMENT '화면ID' COLLATE 'utf8_general_ci',
	`msgcd` VARCHAR(10) NULL COMMENT '수신메세지코드' COLLATE 'utf8_general_ci',
	`rcvmsg` TINYBLOB NULL COMMENT '수신메세지',
	`errinfo` TINYBLOB NULL,
	`sflag` CHAR(1) NULL COMMENT '1.성공 2.실패' COLLATE 'utf8_general_ci',
	`async` CHAR(1) NULL COMMENT '0.tpacall  1.tpcall' COLLATE 'utf8_general_ci',
	`elapsed` DOUBLE(22,0) NULL COMMENT '총소요시간',
	`svctime` DOUBLE(22,0) NULL COMMENT '순수서비스소요시간',
	`slen` INT(10) UNSIGNED NULL COMMENT '송신데이터길이',
	`rlen` INT(10) UNSIGNED NULL COMMENT '수신데이터길이',
	`sdata` MEDIUMBLOB NULL COMMENT '송신데이터',
	`rdata` MEDIUMBLOB NULL COMMENT '수신데이터',
	`cdate` DATETIME NULL COMMENT '생성시간'
) ;

-- 뷰 aqtdb2.ttrxlist 구조 내보내기
-- VIEW 종속성 오류를 극복하기 위해 임시 테이블을 생성합니다.
CREATE TABLE `ttrxlist` (
	`tcode` VARCHAR(20) NOT NULL COLLATE 'utf8_general_ci',
	`svc_cnt` INT(11) NOT NULL,
	`fsvc_cnt` INT(11) NOT NULL,
	`data_cnt` INT(11) NOT NULL,
	`scnt` INT(11) NOT NULL,
	`fcnt` INT(11) NOT NULL,
	`UDATE` DATETIME NULL
) ;

-- 뷰 aqtdb2.ttrx_svc_sum 구조 내보내기
-- VIEW 종속성 오류를 극복하기 위해 임시 테이블을 생성합니다.
CREATE TABLE `ttrx_svc_sum` (
	`pkey` INT(10) UNSIGNED NOT NULL,
	`tcode` VARCHAR(50) NOT NULL COMMENT '테스트코드' COLLATE 'utf8_general_ci',
	`svcid` VARCHAR(20) NULL COMMENT '서비스id' COLLATE 'utf8_general_ci',
	`scrno` VARCHAR(20) NULL COMMENT '화면ID' COLLATE 'utf8_general_ci',
	`tcnt` INT(11) UNSIGNED NOT NULL,
	`avgt` DOUBLE(22,0) NULL,
	`scnt` INT(11) UNSIGNED NULL,
	`fcnt` INT(11) UNSIGNED NULL,
	`cumcnt` INT(11) UNSIGNED NULL,
	`udate` DATETIME NOT NULL COMMENT '등록일시'
) ;

-- 뷰 aqtdb2.vtrxdetail 구조 내보내기
-- VIEW 종속성 오류를 극복하기 위해 임시 테이블을 생성합니다.
CREATE TABLE `vtrxdetail`
) ;

-- 뷰 aqtdb2.vtrxlist 구조 내보내기
-- VIEW 종속성 오류를 극복하기 위해 임시 테이블을 생성합니다.
CREATE TABLE `vtrxlist`
) ;

-- 트리거 aqtdb2.ttcppacket_before_insert 구조 내보내기
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO';
DELIMITER //
//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- 뷰 aqtdb2.tconfig 구조 내보내기
-- 임시 테이블을 제거하고 최종 VIEW 구조를 생성
DROP TABLE IF EXISTS `tconfig`;
;

-- 뷰 aqtdb2.texecjob 구조 내보내기
-- 임시 테이블을 제거하고 최종 VIEW 구조를 생성
DROP TABLE IF EXISTS `texecjob`;
;

-- 뷰 aqtdb2.tloaddata 구조 내보내기
-- 임시 테이블을 제거하고 최종 VIEW 구조를 생성
DROP TABLE IF EXISTS `tloaddata`;
;

-- 뷰 aqtdb2.tmaster 구조 내보내기
-- 임시 테이블을 제거하고 최종 VIEW 구조를 생성
DROP TABLE IF EXISTS `tmaster`;
;

-- 뷰 aqtdb2.trequest 구조 내보내기
-- 임시 테이블을 제거하고 최종 VIEW 구조를 생성
DROP TABLE IF EXISTS `trequest`;
;

-- 뷰 aqtdb2.tservice 구조 내보내기
-- 임시 테이블을 제거하고 최종 VIEW 구조를 생성
DROP TABLE IF EXISTS `tservice`;
;

-- 뷰 aqtdb2.ttcppacket 구조 내보내기
-- 임시 테이블을 제거하고 최종 VIEW 구조를 생성
DROP TABLE IF EXISTS `ttcppacket`;
;

-- 뷰 aqtdb2.ttransaction 구조 내보내기
-- 임시 테이블을 제거하고 최종 VIEW 구조를 생성
DROP TABLE IF EXISTS `ttransaction`;
;

-- 뷰 aqtdb2.ttrxlist 구조 내보내기
-- 임시 테이블을 제거하고 최종 VIEW 구조를 생성
DROP TABLE IF EXISTS `ttrxlist`;
;

-- 뷰 aqtdb2.ttrx_svc_sum 구조 내보내기
-- 임시 테이블을 제거하고 최종 VIEW 구조를 생성
DROP TABLE IF EXISTS `ttrx_svc_sum`;
;

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
, count(case when scnt > 0 then 0 ELSE 1 end ) fsvc_cnt
, SUM(tcnt) data_cnt
, sum(scnt) scnt
, sum(fcnt) fcnt
 from  tmaster m ,ttrx_svc_sum x WHERE m.code = x.tcode AND m.lvl > '0'
 group by tcode )  t
 left join
 (select lvl, count(distinct svcid) tot_svccnt from  ttrx_svc_sum join tmaster m
   on tcode = m.code AND lvl > '0' group by lvl) tot
on t.lvl = tot.lvl ;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
