package tn.esprit.spring;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.services.IEntrepriseService;

@SpringBootTest
public class EntrepriseServiceImplTest {
	
	@Autowired
	IEntrepriseService ent;
	private static final Logger l = LogManager.getLogger(EntrepriseServiceImplTest.class);

	
	@Test
	public void testAjouterEntreprise(){
		
		Entreprise etrp = new Entreprise("test2", "5");
		ent.ajouterEntreprise(etrp) ;
		
	}

	@Test
	public void testAjouterDepartement(){
		
		Departement dep =new Departement("ING");
		ent.ajouterDepartement(dep);
	}
	
	@Test
	public void testAffecterDepartementAEntreprise(){
		ent.affecterDepartementAEntreprise(5, 13);
	}
	
	@Test
	public void testGetAllDepartementsNamesByEntreprise(){
		 ent.getAllDepartementsNamesByEntreprise(3);
		
	}
	
	@Test
	public void testDeleteEntrepriseById(){
		ent.deleteEntrepriseById(12);
	}
	
	@Test
	public void testDeleteDepartementById(){
		ent.deleteDepartementById(11);
	}
	
	@Test
	public void testGetEntrepriseById(){
		Entreprise e= ent.getEntrepriseById(5);
		
		l.info(e);
	}
}
