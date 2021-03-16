package com.dawin.aqttool;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import aqtclient.model.Thostmap;
import aqtclient.model.Tmaster;
import aqtclient.part.AqtMain;

class HostMaptest {
	
	static EntityManagerFactory emf  ;
	
	@BeforeAll
	static void init() {
		emf = AqtMain.getCreateEmf() ;
	}
   


	@Test
	void test() {
		EntityManager em = emf.createEntityManager() ;
		em.getTransaction().begin();
		Tmaster tmaster =  em.find(Tmaster.class, "TH01") ;
		assertNotNull(tmaster);
		Thostmap thostmap = new Thostmap() ;
		thostmap.setTcode(tmaster.getCode());
		thostmap.setThost(tmaster.getThost());
		thostmap.setTport(tmaster.getTport());
		em.persist(thostmap);
		em.getTransaction().commit();
//		System.out.println(thostmap.getTmaster().getCode());
		em.close();
		
	}
	

}
