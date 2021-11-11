package tn.esprit.spring;


import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.entities.Role;

import tn.esprit.spring.services.ITimesheetService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TimesheetServiceImplTest {
	
	
	@Autowired
	ITimesheetService timesheetService ;
	
	
	Date currentDate = new Date(System.currentTimeMillis());
	
	Employe employee = new Employe("test", "junit", "test@junit.com", true, Role.TECHNICIEN);
	Departement departement= new Departement();
	
	int idDep = 0 ; 
	int idMission = 0 ;
	
	
	@Test	
	public void testAjouterMission() {
		Mission mission = new Mission("ajout", "ajout mission");
		timesheetService.ajouterMission(mission); 
	}
	@Test 
	public void testgetAllEmployeByMission() {
		timesheetService.getAllEmployeByMission(idMission);
	}
	
	}