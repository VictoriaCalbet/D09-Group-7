
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ApplicationRepository;
import domain.Application;
import domain.CreditCard;
import domain.Explorer;
import domain.Manager;
import domain.Trip;

@Service
@Transactional
public class ApplicationService {

	// Managed Repository
	@Autowired
	private ApplicationRepository	applicationRepository;

	// Supporting Services
	@Autowired
	private ManagerService			managerService;

	@Autowired
	private ExplorerService			explorerService;

	@Autowired
	private TripService				tripService;

	@Autowired
	private MessageService			messageService;


	// Constructor

	public ApplicationService() {
		super();
	}

	// Simple CRUD methods
	public Application create(final int tripId) {
		final Explorer principal;
		principal = this.explorerService.findByPrincipal();
		Assert.isTrue(this.explorerService.checkExplorer(principal));

		final Trip trip = this.tripService.findOne(tripId);
		Assert.notNull(trip);

		final Application result = new Application();
		result.setMomentMade(new Date(System.currentTimeMillis() - 1));
		result.setStatus("PENDING");
		result.setTrip(trip);

		final Collection<CreditCard> creditCards = new ArrayList<CreditCard>();
		result.setCreditCards(creditCards);

		return result;

	}

	public Application save(final Application application) {
		Assert.notNull(application);

		Application result;

		result = this.applicationRepository.save(application);

		return result;
	}

	public Application saveFromCreate(final Application application) {
		Assert.notNull(application, "message.error.application.null");
		Assert.notNull(application.getMomentMade(), "message.error.application.moment.null");
		Assert.isTrue(application.getStatus().equals("PENDING"), "message.error.application.status.pending");
		Assert.isTrue(application.getCreditCards().isEmpty(), "message.error.application.creditCards.must.empty");

		Assert.notNull(application.getTrip(), "message.error.application.trip.null");

		Application result;
		Explorer principal;
		Trip trip;
		Collection<Application> tripApplications;
		Collection<Application> explorerApplications;

		// Check trip dates.
		trip = application.getTrip();
		Assert.isTrue(trip.getPublicationDate().before(new Date()), "message.error.application.trip.publicationDate.past");
		Assert.isTrue(trip.getStartMoment().after(new Date()), "message.error.application.trip.startMoment.future");
		Assert.isTrue(!trip.getCancelled(), "message.error.application.trip.cancelled");

		// Check that the explorer has not an Application for its trip yet.
		principal = this.explorerService.findByPrincipal();
		Assert.isTrue(!this.checkExplorerWithApplicationToTheSameTrip(principal, application), "message.error.application.trip.repeated");

		// Update attributes
		application.setMomentMade(new Date(System.currentTimeMillis() - 1));
		application.setStatus("PENDING");

		result = this.save(application);

		// Add the Application to the Trip.
		trip = result.getTrip();
		tripApplications = trip.getApplications();
		tripApplications.add(result);
		trip.setApplications(tripApplications);
		this.tripService.saveByOtherActors(trip);

		// Add the Application to the Explorer.
		principal = this.explorerService.findByPrincipal();
		explorerApplications = principal.getApplications();
		explorerApplications.add(result);
		principal.setApplications(explorerApplications);

		// Set suspicious
		boolean isSuspicious = principal.getIsSuspicious();
		if (result.getComment() != null) {
			isSuspicious = isSuspicious || this.messageService.checkSpam(result.getComment());
			principal.setIsSuspicious(isSuspicious);
		}

		this.explorerService.saveFromEdit(principal);

		// Updates message.
		final String subject = "The application has been created.";
		final String body = "The application has been created with PENDING status.";
		final String priority = "NEUTRAL";

		this.messageService.updateMessageToActorsInvolvedInApplication(result.getId(), subject, body, priority);

		return result;
	}

	public Application rejectApplication(final Application application) {
		Assert.notNull(application, "message.error.application.null");
		Assert.isTrue(application.getStatus().equals("PENDING"), "message.error.application.status.pending");
		Assert.isTrue(application.getReasonDenied() != "", "message.error.application.reasonDenied.null");

		// Comprobar que el application es de un trip del manager logueado
		final Manager m = this.managerService.findByPrincipal();
		final Trip t = application.getTrip();
		Assert.isTrue(m.getTrips().contains(t), "message.error.application.manager.trip");

		Application result;

		application.setStatus("REJECTED");
		result = this.save(application);

		// Set suspicious
		boolean isSuspicious = m.getIsSuspicious();
		if (result.getReasonDenied() != null) {
			isSuspicious = isSuspicious || this.messageService.checkSpam(result.getReasonDenied());
			m.setIsSuspicious(isSuspicious);
		}

		this.managerService.saveFromEdit(m);

		// Update message.
		final String subject = "The application status has changed";
		final String body = "The application status has been changed to REJECTED";
		final String priority = "NEUTRAL";
		this.messageService.updateMessageToActorsInvolvedInApplication(result.getId(), subject, body, priority);

		return result;

	}

	public Application dueApplication(final Application application) {
		Assert.notNull(application, "message.error.application.null");
		Assert.isTrue(application.getStatus().equals("PENDING"), "message.error.application.status.pending");

		// Comprobar que el application es de un trip del manager logueado
		final Manager m = this.managerService.findByPrincipal();
		final Trip t = application.getTrip();
		Assert.isTrue(m.getTrips().contains(t), "message.error.application.manager.trip");
		Assert.isTrue(t.getStartMoment().after(new Date()), "message.error.application.trip.startMoment.future");
		Assert.isTrue(!t.getCancelled(), "message.error.application.trip.cancelled");

		Application result;

		application.setStatus("DUE");
		result = this.applicationRepository.save(application);

		// Update message.
		final String subject = "The application status has changed";
		final String body = "The application status has been changed to DUE";
		final String priority = "NEUTRAL";
		this.messageService.updateMessageToActorsInvolvedInApplication(application.getId(), subject, body, priority);

		return result;

	}

	public void acceptApplication(final Application application) {
		Assert.notNull(application, "message.error.application.null");
		Assert.isTrue(application.getCreditCards().isEmpty(), "message.error.application.creditCards.must.empty");

		final Explorer principal;
		principal = this.explorerService.findByPrincipal();
		Assert.isTrue(this.explorerService.checkExplorer(principal), "message.error.application.explorer.authority");

		Assert.isTrue(principal.getApplications().contains(application), "message.error.application.explorer.own");
		Assert.isTrue(application.getStatus().equals("DUE"), "message.error.application.status.due");
		Assert.isTrue(application.getTrip().getStartMoment().after(new Date()), "message.error.application.trip.startMoment.future");
		Assert.isTrue(!application.getTrip().getCancelled(), "message.error.application.trip.cancelled");

		final Collection<CreditCard> creditCards = application.getCreditCards();
		creditCards.add(principal.getCreditCard());
		application.setCreditCards(creditCards);

		application.setStatus("ACCEPTED");

		this.save(application);

		// Update message.
		final String subject = "The application status has changed";
		final String body = "The application status has been changed to ACCEPTED";
		final String priority = "NEUTRAL";
		this.messageService.updateMessageToActorsInvolvedInApplication(application.getId(), subject, body, priority);

	}

	public void cancelApplication(final Application application) {
		Assert.notNull(application, "message.error.application.null");

		final Explorer principal;
		principal = this.explorerService.findByPrincipal();
		Assert.isTrue(this.explorerService.checkExplorer(principal), "message.error.application.explorer.authority");

		Assert.isTrue(principal.getApplications().contains(application), "message.error.application.explorer.own");
		Assert.isTrue(application.getStatus().equals("ACCEPTED"), "message.error.application.status.accepted");

		Assert.isTrue(application.getTrip().getStartMoment().after(new Date()), "message.error.application.trip.startMoment.future");

		application.setStatus("CANCELLED");

		this.save(application);

		// Update message.
		final String subject = "The application status has changed";
		final String body = "The application status has been changed to CANCELLED";
		final String priority = "NEUTRAL";
		this.messageService.updateMessageToActorsInvolvedInApplication(application.getId(), subject, body, priority);
	}

	// Other business methods

	public Collection<Application> findAll() {
		Collection<Application> result;

		result = this.applicationRepository.findAll();

		return result;
	}

	public Application findOne(final int applicationId) {
		Application result;

		result = this.applicationRepository.findOne(applicationId);
		Assert.notNull(result);

		return result;
	}

	//listar los applications del manager que esta actualmente "logueado"
	public Collection<Application> listApplicationsOfManager() {

		//Obtengo el manager logueado
		final Manager m = this.managerService.findByPrincipal();
		//Busco sus applications por la id
		return this.applicationRepository.listApplicationsOfManager(m.getId());
	}

	public Collection<Application> groupPrincipalApplicationsByStatus(final int explorerId) {
		return this.applicationRepository.groupPrincipalApplicationsByStatus(explorerId);
	}
	//C-14.6.1.1 The average number of applications per trip
	public Double findAverageApplicationPerTrip() {
		return this.applicationRepository.findAverageApplicationPerTrip();
	}
	//C-14.6.1.2 The minimum number of applications per trip
	public Integer findMinApplicationPerTrip() {
		return this.applicationRepository.findMinApplicationPerTrip();
	}
	//C-14.6.1.3 The maximum number of applications per trip
	public Integer findMaxApplicationPerTrip() {
		return this.applicationRepository.findMaxApplicationPerTrip();
	}
	//C-14.6.1.4 The standard deviation of the number of applications per trip
	public Double findStandardDeviationOfApplicationPerTrip() {
		return this.applicationRepository.findStandardDeviationOfApplicationPerTrip();
	}

	//C-14.6.5 The ratio of applications with status "PENDING"
	public Double findRatioOfApplicationsWithPending() {
		return this.applicationRepository.findRatioOfApplicationsWithPending();
	}
	//C-14.6.6 The ratio of applications with status "DUE"
	public Double findRatioOfApplicationsWithDue() {
		return this.applicationRepository.findRatioOfApplicationsWithDue();
	}

	//C-14.6.7 The ratio of applications with status "ACCEPTED"
	public Double findRatioOfApplicationsWithAccepted() {
		return this.applicationRepository.findRatioOfApplicationsWithAccepted();
	}
	//C-14.6.8 The ratio of applications with status "CANCELLED"
	public Double findRatioOfApplicationsWithCancelled() {
		return this.applicationRepository.findRatioOfApplicationsWithCancelled();
	}

	public Collection<Application> findAllByPrincipal() {
		final Explorer principal;
		Collection<Application> applications;

		principal = this.explorerService.findByPrincipal();
		applications = this.applicationRepository.findApplicationsByExplorer(principal.getId());
		return applications;
	}

	public Collection<Application> findApplicationsByTripIdAndApplicationAccepted(final int tripId) {
		return this.findApplicationsByTripIdAndApplicationAccepted(tripId);
	}

	public boolean checkEqualsCreditCard(final Collection<CreditCard> creditCards, final CreditCard creditCard) {
		Assert.notNull(creditCards);
		Assert.notEmpty(creditCards);
		Assert.notNull(creditCard);

		boolean result = false;

		for (final CreditCard cc : creditCards)
			if (cc.getHolderName().equals(creditCard.getHolderName()) && cc.getBrandName().equals(creditCard.getBrandName()) && cc.getNumber().equals(creditCard.getNumber()) && cc.getExpirationMonth() == creditCard.getExpirationMonth()
				&& cc.getExpirationYear() == creditCard.getExpirationYear() && cc.getCvv() == creditCard.getCvv()) {
				result = true;
				break;
			}

		return result;
	}

	public boolean checkExplorerWithApplicationToTheSameTrip(final Explorer explorer, final Application application) {
		Assert.notNull(explorer);
		Assert.notNull(application);

		boolean result = false;

		Collection<Application> principalApplications;
		principalApplications = explorer.getApplications();

		for (final Application a : principalApplications)
			if (a.getTrip().getId() == application.getTrip().getId()) {
				result = true;
				break;
			}

		return result;
	}

}
