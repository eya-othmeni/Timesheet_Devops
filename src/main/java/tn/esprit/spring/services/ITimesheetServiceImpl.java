package tn.esprit.spring.services;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.entities.Role;
import tn.esprit.spring.entities.Timesheet;
import tn.esprit.spring.entities.TimesheetPK;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.repository.MissionRepository;
import tn.esprit.spring.repository.TimesheetRepository;

@Service
public class ITimesheetServiceImpl implements ITimesheetService {
   
	Logger logger = LoggerFactory.getLogger(ITimesheetServiceImpl.class);

	@Autowired
	MissionRepository missionRepository;
	@Autowired
	DepartementRepository deptRepoistory;
	@Autowired
	TimesheetRepository timesheetRepository;
	@Autowired
	EmployeRepository employeRepository;
	
	public int ajouterMission(Mission mission) {
		logger.info("Adding Mission");
		if (mission != null) {
		try {
			logger.info("add Mission");
			logger.debug("Je vais ajouter un contrat.");
			missionRepository.save(mission);
		}catch(Exception e) {
			logger.error(e.toString());
		}
		}else {
			logger.warn("Mission is Empty ! ");
		}
		return mission.getId();

	}
    
	public void affecterMissionADepartement(int missionId, int depId) {
		if (Integer.toString(depId).equals("")) {
			logger.warn("No Departement Assigned !");
		}
		else if (Integer.toString(missionId).equals("")) {
			logger.warn("No Mission to Assign to the Departement !");
		}else {
			
			try {
				                       
		Mission missionManagedEntity = missionRepository.findById(missionId).get();
		Departement departementManagedEntity = deptRepoistory.findById(depId).get();
		

		missionManagedEntity.setDepartement(departementManagedEntity);
		missionRepository.save(missionManagedEntity);
		
		logger.info("Mission Binded Succefully ! ");
			}catch(Exception e) {
				logger.error(e.toString());
			}
		}

		
		
	}

	public void ajouterTimesheet(int missionId, int employeId, Date dateDebut, Date dateFin) {
		TimesheetPK timesheetPK = new TimesheetPK();
		timesheetPK.setDateDebut(dateDebut);
		timesheetPK.setDateFin(dateFin);
		timesheetPK.setIdEmploye(employeId);
		timesheetPK.setIdMission(missionId);
		
		Timesheet timesheet = new Timesheet();
		timesheet.setTimesheetPK(timesheetPK);
		timesheet.setValide(false); //par defaut non valide
		timesheetRepository.save(timesheet);
		
	}

	
	public void validerTimesheet(int missionId, int employeId, Date dateDebut, Date dateFin, int validateurId) {
		Employe validateur = employeRepository.findById(validateurId).get();
		Mission mission = missionRepository.findById(missionId).get();
		//verifier s'il est un chef de departement (interet des enum)
		if(!validateur.getRole().equals(Role.CHEF_DEPARTEMENT)){
			return;
		}
		//verifier s'il est le chef de departement de la mission en question
		boolean chefDeLaMission = false;
		for(Departement dep : validateur.getDepartements()){
			if(dep.getId() == mission.getDepartement().getId()){
				chefDeLaMission = true;
				break;
			}
		}
		if(!chefDeLaMission){
			return;
		}
//
		TimesheetPK timesheetPK = new TimesheetPK(missionId, employeId, dateDebut, dateFin);
		Timesheet timesheet =timesheetRepository.findBytimesheetPK(timesheetPK);
		timesheet.setValide(true);
		
		//Comment Lire une date de la base de données
		
	}

	
	public List<Mission> findAllMissionByEmployeJPQL(int employeId) {
		
		return timesheetRepository.findAllMissionByEmployeJPQL(employeId);
	}

	
	public List<Employe> getAllEmployeByMission(int missionId) {
		List<Employe> employe = new ArrayList<>();
		try {
			logger.info("getting employe");
			logger.debug("Je vais afficher les employes.");
			employe = (List<Employe>) employeRepository.findAll();
			logger.debug("Je viens de finir l'opération X.");
			logger.info("Out getAllEmployeByMission() without errors.");			
		
		}
		catch (Exception e)
		{ logger.error("Erreur dans getAllEmployeByMission() : " +e); }
		
		return employe ;
	}

}
