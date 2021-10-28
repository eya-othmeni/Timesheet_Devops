package tn.esprit.spring.services;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EntrepriseRepository;

@Service
public class EntrepriseServiceImpl implements IEntrepriseService {

	/*mvn clean package -Dmaven.test.skip=true deploy:deploy-file 
	 * -DgroupId=tn.esprit.spring -DartifactId=timesheet_devOps -Dversion=1.0 
	 * -DgeneratePom=true -Dpackaging=jar -DrepositoryId=deploymentRepo -Durl=http://localhost:8081/repository/maven-releases/ 
	 * -Dfile=target/timesheet_devOps-1.0.jar
	 */
	@Autowired
    EntrepriseRepository entrepriseRepoistory;
	@Autowired
	DepartementRepository deptRepoistory;
	
	private static final Logger l = LogManager.getLogger(EntrepriseServiceImpl.class);
	
	@Override
	public Entreprise ajouterEntreprise(Entreprise entreprise) {
		l.info("In Ajouter Entreprise");
		entrepriseRepoistory.save(entreprise);
		l.info("Out Ajouter Entreprise");
		return entreprise;
	
	}

	@Override
	public int ajouterDepartement(Departement dep) {
		l.info("In Ajouter Departement");
		deptRepoistory.save(dep);
		l.info("Out Ajouter Departement");
		return dep.getId();
	}
	@Override
	public void affecterDepartementAEntreprise(int depId, int entrepriseId) {
		//Le bout Master de cette relation N:1 est departement  
				//donc il faut rajouter l'entreprise a departement 
				// ==> c'est l'objet departement(le master) qui va mettre a jour l'association
				//Rappel : la classe qui contient mappedBy represente le bout Slave
				//Rappel : Dans une relation oneToMany le mappedBy doit etre du cote one.
				
		       l.info("In Affecter Departement à Entreprise");
				Entreprise entrepriseManagedEntity =  entrepriseRepoistory.findById(entrepriseId).orElse(null);
				Departement depManagedEntity = deptRepoistory.findById(depId).orElse(null);
				
				if(depManagedEntity != null){
					l.info("l'entreprise et le departement existent");
					depManagedEntity.setEntreprise(entrepriseManagedEntity);
				    deptRepoistory.save(depManagedEntity);}
				 l.info("Out Affecter Departement à Entreprise");
		
	}
	@Override
	public List<String> getAllDepartementsNamesByEntreprise(int entrepriseId) {
		l.info("In Get Departement à partir d'une Entreprise");
		Entreprise entrepriseManagedEntity = entrepriseRepoistory.findById(entrepriseId).orElse(null);
		
		List<String> depNames = new ArrayList<>();
		if(entrepriseManagedEntity !=null){
		for(Departement dep : entrepriseManagedEntity.getDepartements()){
			depNames.add(dep.getName());
		}}
		l.info("In Get Departement à partir d'une Entreprise");
		return depNames;
	}

	@Override
	@Transactional
	public void deleteEntrepriseById(int entrepriseId) {
		l.info("In Delete Entreprise");
		Entreprise e=entrepriseRepoistory.findById(entrepriseId).orElse(null);
		if(e!=null)
			entrepriseRepoistory.delete(e);	
		l.info("Out Delete Entreprise");
	}

	@Override
	@Transactional
	public void deleteDepartementById(int depId) {
		l.info("In Delete Departement");
		Departement d=deptRepoistory.findById(depId).orElse(null);
		if(d!=null)
			deptRepoistory.delete(d);
		l.info("Out Delete Departement");
	}


	@Override
	public Entreprise getEntrepriseById(int entrepriseId) {
		l.info("In Get entreprise par id");
		return entrepriseRepoistory.findById(entrepriseId).orElse(null);
		
	}
	@Override
	public Departement  getDepartementById(int departementId) {
		l.info("In Get Departement by id ");
		return deptRepoistory.findById(departementId).orElse(null);
		
	}


}
