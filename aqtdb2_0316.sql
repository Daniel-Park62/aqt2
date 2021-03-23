-- --------------------------------------------------------
-- 호스트:                          localhost
-- 서버 버전:                        10.5.9-MariaDB - mariadb.org binary distribution
-- 서버 OS:                        Win64
-- HeidiSQL 버전:                  11.0.0.5919
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- aqtdb2 데이터베이스 구조 내보내기
CREATE DATABASE IF NOT EXISTS `aqtdb2` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `aqtdb2`;

-- 이벤트 aqtdb2.ev_cnt_upd 구조 내보내기
DELIMITER //
CREATE EVENT `ev_cnt_upd` ON SCHEDULE EVERY 1 HOUR STARTS '2021-02-01 18:07:01' ON COMPLETION PRESERVE DISABLE COMMENT '서비스별 총누적건수 업데이트' DO BEGIN

	DECLARE done INT DEFAULT FALSE;
	DECLARE VCODE VARCHAR(50) ;
	DECLARE cur CURSOR FOR SELECT TCODE FROM ttcppacket WHERE cdate > DATE_ADD(NOW(), interval 1 HOUR) ;
	
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

  OPEN cur;

  read_loop: LOOP
    FETCH cur INTO VCODE;

    IF done THEN
      LEAVE read_loop;
    END IF;

	CALL sp_summary(vcode) ;
  END LOOP;

  CLOSE cur;
END//
DELIMITER ;

-- 프로시저 aqtdb2.sp_copytestdata 구조 내보내기
DELIMITER //
CREATE PROCEDURE `sp_copytestdata`(
	IN `src_code` VARCHAR(50),
	IN `dst_code` VARCHAR(50),
	IN `cond` VARCHAR(100)
)
main: BEGIN

/*
	DECLARE cnt1 INT ;
	SELECT COUNT(1) INTO cnt1 FROM ttcppacket WHERE TCODE = dst_code ;
	
	if cnt1 > 0 then
		SELECT '이전 복사작업된 데이터가 있습니다.' ;
		leave main;
	END if ;
*/	
	DECLARE v_pkey INT ;
	DECLARE v_msg VARCHAR(100) ;
	
   DECLARE exit handler for SQLEXCEPTION
    BEGIN
        ROLLBACK;        
        SELECT  CONCAT('SQL 실행중 오류발생!! ',CHAR(10),  cond) ;
        UPDATE texecjob SET resultStat = 3, msg = 'SQL 실행중 오류발생!!', enddt = NOW() WHERE pkey = v_pkey ;
    END;


	SET @NN = 0 ;
	SET @SRC = src_code ;
	SET @DST = dst_code ;
	SET @SQLT = CONCAT ( 
	' SELECT count(1) into @NN 
	FROM ttcppacket A JOIN ( SELECT CMPID FROM ttcppacket WHERE TCODE = ? ' ,cond  ,
	 ') B ON (A.CMPID = B.CMPID ) WHERE TCODE = ? ' ) ;
	 
	INSERT INTO texecjob (jobkind, tdesc, tcode,  in_file, resultStat, etc, tnum , startdt)
	 VALUES ( 3, '전문복제작업', @DST, @SRC,  1, cond, 1, NOW() ) ;
	
	SELECT LAST_INSERT_ID() INTO v_pkey ;
	
	EXECUTE IMMEDIATE @SQLT USING @SRC, @DST  ;
	if @NN > 0 then
		SELECT '이전 복사작업된 데이터가 있습니다.' ;
		UPDATE texecjob SET resultStat = 3, msg = '이전 복사작업된 데이터가 있습니다.', enddt = NOW() WHERE pkey = v_pkey ;
		leave main;
	END if ;


	SET @SQLT = CONCAT ( 
	' INSERT into ttcppacket 
	( tcode, cmpid, o_stime, stime, rtime,  elapsed, srcip, srcport, dstip, dstport, proto, method, uri, seqno, ackno, rcode,rhead, slen, rlen, sdata )
	SELECT ? ,cmpid, o_stime, stime, rtime,  elapsed, srcip, srcport, dstip, dstport, proto, method, uri, seqno, ackno, 0, "미수행",slen, 0, sdata
	FROM ttcppacket WHERE TCODE = ? ' , cond ) ;
	
	EXECUTE IMMEDIATE @SQLT USING @DST, @SRC  ;

	SELECT CONCAT( ROW_COUNT(), ' 건 복제되었음') INTO v_msg ;
	
	UPDATE texecjob SET resultStat = 2, msg = v_msg , enddt = NOW() WHERE pkey = v_pkey ;
	
	SELECT v_msg ;
	
	
END//
DELIMITER ;

-- 프로시저 aqtdb2.sp_insService 구조 내보내기
DELIMITER //
CREATE PROCEDURE `sp_insService`(
	IN `p_tcode` VARCHAR(50)
)
COMMENT 'packet데이터로 부터 uri 가져옴'
BEGIN

INSERT INTO tservice (svcid, svckor, svceng, svckind, task, manager, cumcnt )
SELECT uri, regexp_replace(uri,'.*/','') nn,regexp_replace(uri,'.*/',''),
       '0', min(tcode) ,'', COUNT(URI)
 FROM ttcppacket X
  WHERE tcode LIKE p_tcode
  AND NOT EXISTS (SELECT 1 FROM tservice WHERE svcid = x.uri)
 GROUP BY URI
 ORDER BY uri ;


END//
DELIMITER ;

-- 프로시저 aqtdb2.sp_summary 구조 내보내기
DELIMITER //
CREATE PROCEDURE `sp_summary`(
	IN `in_tcode` VARCHAR(50)
)
    COMMENT '데이터통계수집'
BEGIN

	UPDATE tmaster T, (
		SELECT      tcode, count(distinct URI ) svc_cnt
		, count(distinct case when sflag = '2' then URI end ) fsvc_cnt
		, count(1) data_cnt
		, sum(case when sflag = '1' then 1 else 0 end) scnt
		, sum(case when sflag = '2' then 1 else 0 end) fcnt
		 from  ttcppacket 
		 WHERE TCODE like in_tcode 
		 GROUP BY TCODE
		) SUMM
	  SET T.svc_cnt = summ.svc_cnt,
								T.fsvc_cnt = summ.fsvc_cnt,
								T.data_cnt = summ.data_cnt,
								T.scnt = summ.scnt,
								T.fcnt = summ.fcnt
		WHERE t.code like in_tcode and t.code = summ.tcode ;
	
	
	UPDATE tlevel l, ( 
		SELECT lvl, COUNT(DISTINCT URI ) svc_cnt, COUNT(1)  data_cnt
		 from  ttcppacket , tmaster 
		 WHERE TCODE =  CODE AND lvl > '0'
		 GROUP BY LVL  
	) s
	SET l.svc_cnt  = s.svc_cnt, l.data_cnt = s.data_cnt 
	WHERE l.lvl = s.lvl ;
	
	INSERT INTO tservice (svcid, svckor, svceng, svckind, task, manager, cumcnt )
			SELECT uri, regexp_replace(uri,'.*/','') nn,regexp_replace(uri,'.*/',''),
			       '0', min(tcode),'', COUNT(URI)
			 FROM ttcppacket X WHERE tcode = in_tcode
			 AND NOT EXISTS (SELECT 1 FROM tservice WHERE SVCID = X.uri)
			 GROUP BY URI;
 
	UPDATE tservice l, ( 
		SELECT uri, COUNT(1 ) cnt
		 from  ttcppacket , tmaster 
		 WHERE TCODE =  CODE 
		 GROUP BY uri  
	) s
	SET l.cumcnt  = s.cnt
	WHERE l.svcid = s.uri ;

	
END//
DELIMITER ;

-- 프로시저 aqtdb2.sp_summtask 구조 내보내기
DELIMITER //
CREATE PROCEDURE `sp_summtask`(
	IN `in_task` VARCHAR(50)
)
BEGIN
	
	INSERT INTO ttasksum ( task, lvl, svc_cnt, fsvc_cnt, data_cnt, scnt, fcnt, udate )
	 SELECT task, lvl, svc_cnt, fsvc_cnt, data_cnt, scnt, fcnt , NOW() FROM 
	 ( 		SELECT      task, lvl, count(distinct URI ) svc_cnt
		, count(distinct case when sflag = '2' then URI end ) fsvc_cnt
		, count(1) data_cnt
		, sum(case when sflag = '1' then 1 else 0 end) scnt
		, sum(case when sflag = '2' then 1 else 0 end) fcnt
		 from  ttcppacket a JOIN tservice b ON (a.uri = b.svcid ) JOIN tmaster m ON (a.tcode = m.code)
		 WHERE b.task like in_task
		 GROUP BY b.task, lvl
	 ) summ
	 ON DUPLICATE KEY 
	UPDATE lvl = summ.lvl,
			 svc_cnt = summ.svc_cnt,
			 fsvc_cnt = summ.fsvc_cnt,
			 data_cnt = summ.data_cnt,
			 scnt = summ.scnt,
			 fcnt = summ.fcnt,
			 udate = NOW()
		;
	
	SELECT ROW_COUNT() ;		
	
END//
DELIMITER ;

-- 테이블 aqtdb2.tconfig 구조 내보내기
CREATE TABLE IF NOT EXISTS `tconfig` (
  `id` int(11) NOT NULL DEFAULT 1,
  `pass1` varchar(50) DEFAULT NULL COMMENT '테스트admin passwd',
  `TCODE` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into tconfig (id,pass1) values(1,'testadmin') ;

-- 테이블 aqtdb2.texecjob 구조 내보내기
CREATE TABLE IF NOT EXISTS `texecjob` (
  `pkey` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `jobkind` smallint(5) unsigned NOT NULL DEFAULT 9 COMMENT '0.패킷캡쳐 1.패킷파일import 3.패킷복제 9.테스트수행',
  `tcode` varchar(50) NOT NULL DEFAULT '',
  `tdesc` varchar(80) NOT NULL DEFAULT '' COMMENT '테스트설명',
  `tnum` smallint(5) unsigned NOT NULL DEFAULT 10 COMMENT '쓰레드 수',
  `dbskip` char(1) NOT NULL DEFAULT '0' COMMENT '1. dbupdate skip',
  `etc` varchar(256) NOT NULL DEFAULT '' COMMENT '기타 선택조건',
  `in_file` varchar(100) NOT NULL DEFAULT '' COMMENT '입력파일 or src Tcode',
  `outlogdir` varchar(50) NOT NULL DEFAULT '' COMMENT 'out로그위치',
  `tuser` varchar(50) NOT NULL DEFAULT '',
  `tdir` varchar(50) NOT NULL DEFAULT '',
  `tenv` varchar(50) NOT NULL DEFAULT '',
  `reqstartDt` datetime NOT NULL DEFAULT current_timestamp() COMMENT '작업시작요청일시',
  `exectype` smallint(5) unsigned NOT NULL DEFAULT 0 COMMENT '0.일괄실행  1.송신시간에 맞추어',
  `resultstat` smallint(5) unsigned NOT NULL DEFAULT 0 COMMENT '0. 미실행 1.수행중  2.완료 3.실행오류',
  `reqnum` smallint(5) unsigned NOT NULL DEFAULT 0 COMMENT '재요청횟수',
  `startDt` datetime DEFAULT NULL COMMENT '작업시작시간',
  `endDt` datetime DEFAULT NULL COMMENT '작업종료시간',
  `msg` text DEFAULT NULL COMMENT '작업메세지',
  PRIMARY KEY (`pkey`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='테스트작업요청\r\njobkind :\r\n0. tcode 에  etc의 정보를 이용하여 캡쳐수행\r\n1. tcode 에  infile 을 etc 조건적용하여 import\r\n3. tcode 애 infile 의 테스트 id를 복사해옴  infil -> tcode ( etc 조건적용 )\r\n9. 테스트송신';

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 aqtdb2.thostmap 구조 내보내기
CREATE TABLE IF NOT EXISTS `thostmap` (
  `pkey` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `tcode` varchar(50) NOT NULL DEFAULT '',
  `thost` varchar(50) DEFAULT NULL,
  `tport` int(11) unsigned DEFAULT NULL,
  `thost2` varchar(50) DEFAULT NULL,
  `tport2` int(11) unsigned DEFAULT NULL,
  PRIMARY KEY (`pkey`),
  KEY `tcode` (`tcode`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 aqtdb2.tlevel 구조 내보내기
CREATE TABLE IF NOT EXISTS `tlevel` (
  `lvl` char(1) NOT NULL DEFAULT '0',
  `lvl_nm` varchar(50) NOT NULL DEFAULT '0',
  `svc_cnt` int(10) unsigned NOT NULL DEFAULT 0,
  `data_cnt` int(10) unsigned NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='테스트 level 단위, 통합, 실시간 ';

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 aqtdb2.tmaster 구조 내보내기
CREATE TABLE IF NOT EXISTS `tmaster` (
  `code` varchar(20) NOT NULL,
  `type` char(1) DEFAULT '1' COMMENT '1.배치테스트 2.실시간',
  `lvl` char(1) DEFAULT '0' COMMENT '0.ORIGIN 1.단위  2.통합테스트',
  `desc1` varchar(50) DEFAULT NULL,
  `cmpCode` varchar(20) DEFAULT NULL COMMENT '주비교테스트',
  `tdate` date DEFAULT current_timestamp() COMMENT '테스트시작일',
  `endDate` date DEFAULT NULL COMMENT '테스트종료일',
  `tdir` varchar(80) DEFAULT NULL,
  `tuser` varchar(20) DEFAULT NULL,
  `thost` varchar(50) DEFAULT NULL,
  `tport` int(10) unsigned NOT NULL DEFAULT 0,
  `tenv` varchar(50) DEFAULT NULL COMMENT '별도환경파일위치',
  `svc_cnt` int(10) unsigned DEFAULT 0,
  `fsvc_cnt` int(10) unsigned DEFAULT 0,
  `data_cnt` int(10) unsigned DEFAULT 0,
  `scnt` int(10) unsigned DEFAULT 0,
  `fcnt` int(10) unsigned DEFAULT 0,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='테스트 기본정보';

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 aqtdb2.trequest 구조 내보내기
CREATE TABLE IF NOT EXISTS `trequest` (
  `pkey` int(10) unsigned NOT NULL,
  `cmpid` int(10) unsigned NOT NULL,
  `tcode` varchar(50) NOT NULL DEFAULT '',
  `uuid` char(32) DEFAULT '',
  `reqUser` varchar(50) NOT NULL DEFAULT '' COMMENT '요청자',
  `reqDt` timestamp NULL DEFAULT current_timestamp() COMMENT '요청일시',
  PRIMARY KEY (`pkey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='tr재전송요청';

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 aqtdb2.tservice 구조 내보내기
CREATE TABLE IF NOT EXISTS `tservice` (
  `svcid` varchar(256) NOT NULL COMMENT '서비스id or uri',
  `svckor` varchar(60) DEFAULT NULL COMMENT '한글서비스명',
  `svceng` varchar(60) DEFAULT NULL COMMENT '영문서비스명',
  `svckind` char(1) DEFAULT NULL COMMENT '서비스종류',
  `task` varchar(50) DEFAULT NULL COMMENT '업무명',
  `manager` varchar(50) DEFAULT NULL COMMENT '담당자',
  `cumcnt` int(10) unsigned DEFAULT 0 COMMENT '누적건수',
  PRIMARY KEY (`svcid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='서비스 id / name';

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 aqtdb2.ttasksum 구조 내보내기
CREATE TABLE IF NOT EXISTS `ttasksum` (
  `task` varchar(50) NOT NULL,
  `lvl` char(1) NOT NULL DEFAULT '0' COMMENT '0.ORIGIN 1.단위  2.통합테스트',
  `svc_cnt` int(10) unsigned DEFAULT 0,
  `fsvc_cnt` int(10) unsigned DEFAULT 0,
  `data_cnt` int(10) unsigned DEFAULT 0,
  `scnt` int(10) unsigned DEFAULT 0,
  `fcnt` int(10) unsigned DEFAULT 0,
  `udate` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`task`,`lvl`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 aqtdb2.ttcppacket 구조 내보내기
CREATE TABLE IF NOT EXISTS `ttcppacket` (
  `pkey` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `cmpid` int(10) unsigned NOT NULL DEFAULT 0,
  `tcode` varchar(50) NOT NULL,
  `o_stime` datetime(6) NOT NULL COMMENT 'org 송신시간',
  `stime` datetime(6) NOT NULL COMMENT '송신시간',
  `rtime` datetime(6) NOT NULL COMMENT '수신시간',
  `svctime` double(22,6) GENERATED ALWAYS AS (`rtime` - `stime`) VIRTUAL,
  `elapsed` double(22,6) NOT NULL DEFAULT (`rtime` - `stime`) COMMENT '소요시간',
  `srcip` varchar(30) DEFAULT NULL COMMENT '소스ip',
  `srcport` int(10) unsigned DEFAULT NULL COMMENT '소스port',
  `dstip` varchar(30) DEFAULT NULL COMMENT '목적지ip',
  `dstport` int(10) unsigned DEFAULT NULL COMMENT '목적지port',
  `proto` char(1) DEFAULT NULL COMMENT '0.tcp 1.http 2.https',
  `method` varchar(20) DEFAULT NULL COMMENT 'method',
  `uri` varchar(4096) DEFAULT NULL,
  `seqno` int(10) unsigned DEFAULT NULL,
  `ackno` int(10) unsigned DEFAULT NULL,
  `rcode` int(10) unsigned DEFAULT 0 COMMENT 'return code',
  `sflag` char(1) GENERATED ALWAYS AS (if(`rcode` > 399,'2',if(`rcode` > 199,'1','0'))) VIRTUAL,
  `rhead` varchar(8192) DEFAULT NULL COMMENT 'response header',
  `slen` int(10) unsigned DEFAULT NULL COMMENT '송신데이터길이',
  `rlen` int(10) unsigned DEFAULT NULL COMMENT '수신데이터길이',
  `sdata` mediumblob DEFAULT NULL COMMENT '송신데이터',
  `rdata` mediumblob DEFAULT NULL COMMENT '수신데이터',
  `cdate` datetime(6) DEFAULT current_timestamp(6) COMMENT '생성일시',
  PRIMARY KEY (`pkey`),
  KEY `cmpid` (`cmpid`)
) ENGINE=InnoDB AUTO_INCREMENT=33938 DEFAULT CHARSET=utf8;

-- 내보낼 데이터가 선택되어 있지 않습니다.

CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `vtrxdetail` AS select uuid_short() pkey, a.tcode, a.svcid,  s.svckor svckor, a.tcnt, a.avgt ,a.scnt ,a.fcnt,
       sum(tcnt) OVER (PARTITION BY a.svcid) cumcnt
from   (
select t.tcode, t.uri svcid,  count(1) tcnt, avg(t.svctime) avgt, sum(case when t.sflag = '1' then 1 else 0 end) scnt
, sum(case when t.sflag = '2' then 1 else 0 end) fcnt
from   ttcppacket t, tmaster m where m.code = t.tcode and m.lvl > '0'
group by t.tcode, t.uri
) as a
left outer join Tservice s on a.svcid = s.svcid ;

-- 뷰 aqtdb2.vtrxlist 구조 내보내기

CREATE  VIEW `vtrxlist` AS SELECT      t.code, `type`, t.lvl, desc1, cmpCode, tdate, endDate, tdir, tuser, thost, tport, tenv,
				ifnull(t.svc_cnt, 0) svc_cnt,
				ifnull(t.fsvc_cnt, 0) fsvc_cnt,
            ifnull(t.data_cnt, 0) data_cnt,
            ifnull(t.scnt, 0) scnt,
            ifnull(t.fcnt, 0) fcnt,
            ifnull(scnt * 100 / (scnt+fcnt) ,0.0)  spct,
            IFNULL(l.svc_cnt,0) tot_svccnt
from tmaster t left JOIN tlevel l ON (t.lvl = l.lvl) ;

-- WHERE t.lvl > '0' ;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;

GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'dawinit1';

CREATE USER 'aqtdb'@'%' IDENTIFIED BY 'Dawinit1!';
GRANT EXECUTE,SELECT, DELETE, INSERT, EVENT, UPDATE, TRIGGER  ON `aqtdb2`.* TO 'aqtdb'@'%';
GRANT EXECUTE,SELECT, DELETE, INSERT, EVENT, UPDATE, TRIGGER  ON `aqtdb2`.* TO 'aqtdb'@'localhost';