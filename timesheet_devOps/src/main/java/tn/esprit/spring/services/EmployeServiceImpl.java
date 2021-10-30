package tn.esprit.spring.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.spring.entities.Contrat;
import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.entities.Timesheet;
import tn.esprit.spring.repository.ContratRepository;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.repository.TimesheetRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class EmployeServiceImpl implements IEmployeService {

	@Autowired
	EmployeRepository employeRepository;
	@Autowired
	DepartementRepository deptRepoistory;
	@Autowired
	ContratRepository contratRepoistory;
	@Autowired
	TimesheetRepository timesheetRepository;

	private static final Logger l = LogManager.getLogger(EmployeServiceImpl.class);
	
	public int ajouterEmploye(Employe employe) {
		l.info("début ajout employee");
		employeRepository.save(employe);
		l.info("fin ajout employee");
		return employe.getId();
	}

	public void mettreAjourEmailByEmployeId(String email, int employeId) {
		l.info("début mettre à jour email d'employee");
		Employe employe = employeRepository.findById(employeId).get();
		employe.setEmail(email);
		l.info("email de l'employee mis à jour");
		employeRepository.save(employe);
		l.info("fin mettre à jour email d'employee");
	}

	@Transactional	
	public void affecterEmployeADepartement(int employeId, int depId) {
		l.info("début affectation employee à departement");

		Departement depManagedEntity = deptRepoistory.findById(depId).get();
		Employe employeManagedEntity = employeRepository.findById(employeId).get();

		if(depManagedEntity.getEmployes() == null){
			l.info("list employee dans le departement est null");
			List<Employe> employes = new ArrayList<>();
			employes.add(employeManagedEntity);
			depManagedEntity.setEmployes(employes);
			l.info("employee affecté à departement");
		}else{
			l.info("list employee dans le departement non null");
			depManagedEntity.getEmployes().add(employeManagedEntity);
			l.info("employee affecté à departement");

		}
		l.info("fin affectation employee à departement");

	}
	@Transactional
	public void desaffecterEmployeDuDepartement(int employeId, int depId)
	{			l.info("début desaffectation employee à departement");

		Departement dep = deptRepoistory.findById(depId).get();

		int employeNb = dep.getEmployes().size();
		for(int index = 0; index < employeNb; index++){
			if(dep.getEmployes().get(index).getId() == employeId){
				dep.getEmployes().remove(index);
				l.info("employe desafecté du departement");
				break;//a revoir
			}
		}
		l.info("fin desaffectation employee à departement");
	}

	public int ajouterContrat(Contrat contrat) {
		l.info("début ajout contrat");
		contratRepoistory.save(contrat);
		l.info("fin ajout contrat");

		return contrat.getReference();

	}

	public void affecterContratAEmploye(int contratId, int employeId) {
		l.info("début affectation contrat a employe");

		Contrat contratManagedEntity = contratRepoistory.findById(contratId).get();
		Employe employeManagedEntity = employeRepository.findById(employeId).get();

		contratManagedEntity.setEmploye(employeManagedEntity);
		l.info("contrat affecte a employe");

		contratRepoistory.save(contratManagedEntity);
		l.info("fin affectation contrat a employe");

	}

	public String getEmployePrenomById(int employeId) {
		l.info("début get employePrenom by id");

		Employe employeManagedEntity = employeRepository.findById(employeId).get();
		l.info("fin get employePrenom by id");

		return employeManagedEntity.getPrenom();
		
	}
	public void deleteEmployeById(int employeId)
	{		l.info("début delete employe by id");

		Employe employe = employeRepository.findById(employeId).get();

		//Desaffecter l'employe de tous les departements
		//c'est le bout master qui permet de mettre a jour
		//la table d'association
		for(Departement dep : employe.getDepartements()){
			dep.getEmployes().remove(employe);
			l.info("remove employe "+employe.toString());
		}

		employeRepository.delete(employe);
		l.info("fin delete employe by id");
	}

	public void deleteContratById(int contratId) {
		l.info("début delete contrat by id");
		Contrat contratManagedEntity = contratRepoistory.findById(contratId).get();
		contratRepoistory.delete(contratManagedEntity);
		l.info("contrat deleted");

		l.info("fin delete contrat by id");

	}
	public int getNombreEmployeJPQL() {
		return employeRepository.countemp();
	}
	
	public List<String> getAllEmployeNamesJPQL() {
		return employeRepository.employeNames();

	}
	
	public List<Employe> getAllEmployeByEntreprise(Entreprise entreprise) {
		return employeRepository.getAllEmployeByEntreprisec(entreprise);
	}

	public void mettreAjourEmailByEmployeIdJPQL(String email, int employeId) {
		employeRepository.mettreAjourEmailByEmployeIdJPQL(email, employeId);

	}
	public void deleteAllContratJPQL() {
         employeRepository.deleteAllContratJPQL();
	}
	
	public float getSalaireByEmployeIdJPQL(int employeId) {
		return employeRepository.getSalaireByEmployeIdJPQL(employeId);
	}

	public Double getSalaireMoyenByDepartementId(int departementId) {
		return employeRepository.getSalaireMoyenByDepartementId(departementId);
	}
	
	public List<Timesheet> getTimesheetsByMissionAndDate(Employe employe, Mission mission, Date dateDebut,
			Date dateFin) {
		return timesheetRepository.getTimesheetsByMissionAndDate(employe, mission, dateDebut, dateFin);
	}

	public List<Employe> getAllEmployes() {
				return (List<Employe>) employeRepository.findAll();
	}



}
