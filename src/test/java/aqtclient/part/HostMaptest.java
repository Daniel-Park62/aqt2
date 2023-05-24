package aqtclient.part;

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

		Tmaster tmaster =  em.find(Tmaster.class, "TH01") ;
		assertNotNull(tmaster);
		Thostmap thostmap = new Thostmap() ;
		thostmap.setTcode(tmaster.getCode());
		thostmap.setThost(tmaster.getThost());
		thostmap.setTport(tmaster.getTport());
		em.persist(thostmap);
//		System.out.println(thostmap.getTmaster().getCode());
		em.close();
		
	}
	
	@Test
	void test2() {
		System.out.println("AAA".split(" ")[0]);
	}

}
