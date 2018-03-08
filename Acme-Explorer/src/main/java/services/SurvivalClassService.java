
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.SurvivalClassRepository;
import domain.Application;
import domain.Explorer;
import domain.Manager;
import domain.SurvivalClass;
import domain.Trip;

@Service
@Transactional
public class SurvivalClassService {

	// Managed Repository
	@Autowired
	private SurvivalClassRepository	survivalClassRepository;
	@Autowired
	private ManagerService			managerService;
	@Autowired
	private ExplorerService			explorerService;
	@Autowired
	private TripService				tripService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private LocationService			locationService;


	// Supporting Services

	// Constructor

	public SurvivalClassService() {
		super();
	}

	// Simple CRUD methods
	//Create a SurvivalClass
	public SurvivalClass create() {

		SurvivalClass result;
		result = new SurvivalClass();

		result.setMomentOrganized(new Date(System.currentTimeMillis() - 1));

		return result;

	}
	//Find one SurvivalClass
	public SurvivalClass findOne(final SurvivalClass a) {

		return this.survivalClassRepository.findOne(a.getId());
	}
	public SurvivalClass findOne(final int scId) {
		return this.survivalClassRepository.findOne(scId);
	}
	//List all SurvivalClasses

	public Collection<SurvivalClass> listAll() {
		return this.survivalClassRepository.findAll();
	}
	public void save(final SurvivalClass sc) {
		Assert.notNull(sc);
		this.survivalClassRepository.save(sc);
	}
	//Save a SurvivalClass
	public SurvivalClass saveFromCreate(final SurvivalClass sc) {
		Assert.notNull(sc);

		SurvivalClass result;
		Manager principal;

		principal = this.managerService.findByPrincipal();
		Assert.isTrue(sc.getTrip().getManager().getId() == principal.getId());
		Assert.notNull(principal, "message.error.audit.login");

		sc.setMomentOrganized(new Date(System.currentTimeMillis() - 1));
		result = this.survivalClassRepository.save(sc);
		Assert.notNull(result);
		final Collection<Trip> principalTrips = principal.getTrips();
		principalTrips.add(result.getTrip());

		//Check suspicious
		Boolean isSuspicious;

		isSuspicious = principal.getIsSuspicious();
		isSuspicious = isSuspicious || this.messageService.checkSpam(sc.getTitle()) || this.messageService.checkSpam(sc.getDescription());
		principal.setIsSuspicious(isSuspicious);
		this.managerService.saveFromEdit(principal);

		return result;
	}
	public SurvivalClass saveFromEdit(final SurvivalClass sc) {
		Assert.notNull(sc);

		final SurvivalClass result = this.survivalClassRepository.save(sc);
		final Manager principal = this.managerService.findByPrincipal();
		Assert.notNull(result);

		Boolean isSuspicious;

		isSuspicious = principal.getIsSuspicious();
		isSuspicious = isSuspicious || this.messageService.checkSpam(sc.getTitle()) || this.messageService.checkSpam(sc.getDescription()) || this.messageService.checkSpam(sc.getLocation().getName());
		principal.setIsSuspicious(isSuspicious);
		this.managerService.saveFromEdit(principal);
		return result;

	}
	//Save from edit explorer

	public SurvivalClass saveFromEditExplorer(final SurvivalClass sc) {
		Assert.notNull(sc);
		final SurvivalClass result = this.survivalClassRepository.save(sc);
		final Explorer principal = this.explorerService.findByPrincipal();

		this.explorerService.saveFromEdit(principal);
		Assert.notNull(result);
		return result;

	}

	//Delete a SurvivalClass

	public void delete(final SurvivalClass sc) {
		Assert.notNull(sc);
		final Trip t = sc.getTrip();
		sc.setMomentOrganized(new Date(System.currentTimeMillis() - 1));
		this.survivalClassRepository.save(sc);
		this.locationService.delete(sc.getLocation());
		this.survivalClassRepository.delete(sc);

		t.getSurvivalClasses().remove(sc);
	}

	//25.1 Enrol a Survival Class
	public void enrolASurvivalClass(final int scId) {

		final Explorer principal = this.explorerService.findByPrincipal();
		final SurvivalClass sc = this.survivalClassRepository.findOne(scId);
		final Collection<SurvivalClass> survivalClasses = principal.getSurvivalClasses();

		final Collection<Trip> tripsExplorerApplicationAccepted = new ArrayList<Trip>();
		final Collection<Trip> tripsExplorerApplicationAcceptedNotEnded = new ArrayList<Trip>();
		final Collection<Trip> tripsNotEnded = this.tripService.findTripsNotEnded();
		Collection<Application> applicationsFiltered = new ArrayList<Application>();
		final Collection<SurvivalClass> tripSurvivalClasses = new ArrayList<SurvivalClass>();
		final Collection<Integer> enrolableSurvivalClasses = new ArrayList<Integer>();

		applicationsFiltered = principal.getApplications();
		//Trips de las aplicaciones de explorer aceptadas
		for (final Application a : applicationsFiltered)
			if (a.getStatus().contains("ACCEPTED"))
				tripsExplorerApplicationAccepted.add(a.getTrip());

		//Trips que aún no han terminado a los que pertenecen las aplicaciones de explorer aceptadas
		for (final Trip t : tripsExplorerApplicationAccepted)
			if (tripsNotEnded.contains(t))
				tripsExplorerApplicationAcceptedNotEnded.add(t);

		for (final Trip t : tripsExplorerApplicationAcceptedNotEnded)
			tripSurvivalClasses.addAll(t.getSurvivalClasses());

		for (final SurvivalClass survivalc : tripSurvivalClasses)
			if (survivalClasses.contains(survivalc))
				enrolableSurvivalClasses.add(survivalc.getId());

		final Collection<Explorer> explorers = sc.getExplorers();
		explorers.add(principal);
		sc.setExplorers(explorers);

		this.saveFromEditExplorer(sc);

		final SurvivalClass scSave = this.survivalClassRepository.save(sc);
		this.survivalClassRepository.save(scSave);

		this.explorerService.saveFromEdit(principal);
		Assert.isTrue(sc.getExplorers().contains(principal));
	}
	// Other business methods
	public Collection<SurvivalClass> findSurvivalClassesFromExplorerApplicationsId(final int applicationId) {
		return this.survivalClassRepository.findSurvivalClassesFromExplorerApplicationsId(applicationId);
	}
}
