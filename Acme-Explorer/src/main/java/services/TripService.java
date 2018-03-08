
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.TripRepository;
import domain.Application;
import domain.Audit;
import domain.Explorer;
import domain.Finder;
import domain.Manager;
import domain.Note;
import domain.Sponsorship;
import domain.Stage;
import domain.Story;
import domain.SurvivalClass;
import domain.TagValue;
import domain.Trip;

@Service
@Transactional
public class TripService {

	// Managed Repository
	@Autowired
	private TripRepository		tripRepository;

	// Supporting Services
	@Autowired
	private ManagerService		managerService;

	@Autowired
	private ExplorerService		explorerService;

	@Autowired
	private MessageService		messageService;

	@Autowired
	private ApplicationService	applicationService;


	// Constructor

	public TripService() {
		super();
	}

	// Simple CRUD methods
	//Find all trips
	public Collection<Trip> findAll() {
		return this.tripRepository.findAll();
	}
	//Create a trip
	public Trip create() {

		final Collection<Stage> stages = new ArrayList<Stage>();
		final Collection<Sponsorship> sponsorships = new ArrayList<Sponsorship>();
		final Collection<Note> notes = new ArrayList<Note>();
		final Collection<Audit> audits = new ArrayList<Audit>();
		final Collection<Story> stories = new ArrayList<Story>();
		final Collection<Application> applications = new ArrayList<Application>();
		final Collection<SurvivalClass> survivalClasses = new ArrayList<SurvivalClass>();
		final Collection<TagValue> tagValues = new ArrayList<TagValue>();
		final Manager manager = this.managerService.findByPrincipal();

		//inicializar un objeto
		final Trip t = new Trip();
		t.setCancelled(false);
		//Cuando creo el trip, no existe ningun stage, por lo cual el precio es 0.0
		t.setPrice(0.0);
		//Generamos un ticker, pero este no va a ser el que persista en la BBDD
		//Esto se realiza para que no de error en el binding
		t.setTicker(this.generateTicker());
		t.setStages(stages);
		t.setSponsorships(sponsorships);
		t.setNotes(notes);
		t.setAudits(audits);
		t.setStories(stories);
		t.setApplications(applications);
		t.setSurvivalClasses(survivalClasses);
		t.setTagValues(tagValues);
		t.setManager(manager);
		return t;
	}
	//Save a trip
	public Trip save(final Trip t) {

		Assert.notNull(t);
		//La fecha de publicacion sea posterior a la actual
		Assert.isTrue(t.getPublicationDate().after(new Date()), "message.error.trip.publicationDate.future");
		//Fechas futuras
		Assert.isTrue(t.getStartMoment().before(t.getEndMoment()), "message.error.trip.endMoment.before");
		Assert.isTrue(t.getStartMoment().after(new Date()), "message.error.trip.startMoment.future");
		Assert.isTrue(t.getEndMoment().after(new Date()), "message.error.trip.endMoment.future");
		Assert.isTrue(t.getPublicationDate().before(t.getStartMoment()), "message.error.trip.publicationDate.before");
		Assert.isTrue(t.getLegalText().getIsDraft() == false, "message.error.trip.legalText.draft");

		Boolean isSuspicious;
		Manager principal;
		principal = this.managerService.findByPrincipal();

		isSuspicious = principal.getIsSuspicious();
		isSuspicious = isSuspicious || this.messageService.checkSpam(t.getTitle()) || this.messageService.checkSpam(t.getDescription()) || this.messageService.checkSpam(t.getRequirements());
		principal.setIsSuspicious(isSuspicious);

		Trip result;
		t.setTicker(this.generateTicker());
		t.getTagValues().remove(null);
		result = this.tripRepository.save(t);

		Collection<Trip> trips;
		trips = principal.getTrips();
		trips.add(result);
		principal.setTrips(trips);
		this.managerService.saveFromEdit(principal);
		return result;

	}
	public Trip saveApplication(final Trip t) {

		Assert.notNull(t);

		Trip result;
		result = this.tripRepository.saveAndFlush(t);

		return result;

	}

	public Trip saveByOtherActors(final Trip t) {

		Assert.notNull(t);
		//La fecha de publicacion sea posterior a la actual

		//Fechas futuras
		Assert.isTrue(t.getStartMoment().before(t.getEndMoment()));
		Assert.isTrue(t.getStartMoment().after(new Date()));
		Assert.isTrue(t.getEndMoment().after(new Date()));

		Trip result;

		result = this.tripRepository.save(t);

		return result;

	}

	//Update a trip
	public void update(final Trip trip) {

		//Comprueba que no sea nulo, que exista
		Assert.notNull(this.tripRepository.findOne(trip.getId()), "message.error.trip.notExist");
		//Comprobación que el manager es el logueado
		Assert.isTrue(trip.getManager().equals(this.managerService.findByPrincipal()), "message.error.trip.notManager");
		//Compruebo que la fecha de publicacion sea despues de la fecha actual
		Assert.isTrue(trip.getPublicationDate().after(new Date()), "message.error.trip.publicationDate.future");
		//Fechas futuras
		Assert.isTrue(trip.getStartMoment().before(trip.getEndMoment()), "message.error.trip.endMoment.before");
		Assert.isTrue(trip.getStartMoment().after(new Date()), "message.error.trip.startMoment.future");
		Assert.isTrue(trip.getEndMoment().after(new Date()), "message.error.trip.endMoment.future");
		Assert.isTrue(trip.getPublicationDate().before(trip.getStartMoment()), "message.error.trip.publicationDate.before");
		Assert.isTrue(trip.getLegalText().getIsDraft() == false, "message.error.trip.legalText.draft");

		Boolean isSuspicious;
		Manager principal;
		principal = this.managerService.findByPrincipal();

		isSuspicious = principal.getIsSuspicious();
		isSuspicious = isSuspicious || this.messageService.checkSpam(trip.getTitle()) || this.messageService.checkSpam(trip.getDescription()) || this.messageService.checkSpam(trip.getRequirements());
		principal.setIsSuspicious(isSuspicious);
		this.managerService.saveFromEdit(principal);

		//Lo guardo
		this.tripRepository.save(trip);
	}
	//Delete a trip
	public void delete(final int idTrip) {

		//Comprueba que no sea nulo, que exista
		Assert.notNull(this.tripRepository.findOne(idTrip), "message.error.trip.notExist");
		//Creo el objeto
		final Trip t = this.tripRepository.findOne(idTrip);
		//Comprobación que el manager es el logueado
		Assert.isTrue(t.getManager().equals(this.managerService.findByPrincipal()), "message.error.trip.notManager");
		//Compruebo que la fecha de publicacion sea despues de la fecha actual
		Assert.isTrue(t.getPublicationDate().after(new Date()), "message.error.trip.publicationDate.future");
		//Lo borro
		this.tripRepository.delete(t);

		Manager principal;
		principal = this.managerService.findByPrincipal();
		Collection<Trip> trips;
		trips = principal.getTrips();
		trips.remove(t);
		principal.setTrips(trips);
		this.managerService.saveFromEdit(principal);
	}

	// Other business methods

	public Collection<Trip> findTripsWithNotPrincipalApply(final int idExplorer) {
		Collection<Trip> result;
		Explorer principal;
		principal = this.explorerService.findByPrincipal();
		result = this.tripRepository.findTripsWithNotPrincipalApply(principal.getId());
		return result;
	}

	//C- 10.3 Search for trips using a single key word that must be contained
	//either in their tick-ers, titles, or descriptions
	public Collection<Trip> findTripByKeyWord(final String keyWord) {
		return this.tripRepository.findTripByKeyWord(keyWord);
	}

	//12.1 Find a trip by a ticker
	public Trip findTripByTicker(final String ticker) {
		Assert.notNull(ticker);
		return this.tripRepository.findTripByTicker(ticker);
	}

	//Find trip by audit
	public Trip findTripByAudit(final int auditId) {
		return this.tripRepository.findTripByAudit(auditId);
	}
	//Sum the prices of the stages of a trip
	public Double sumByPriceStage(final int idTrip) {

		final Trip t = this.tripRepository.findOne(idTrip);
		Assert.notNull(t);
		return this.tripRepository.sumByPriceStage(t.getId());
	}
	// Cancel a trip
	public void cancelledTrip(final int idTrip, final String reasonCancelled) {

		//Comprobación para saber si existe el trip recibido
		Assert.notNull(this.tripRepository.findOne(idTrip), "message.error.trip.notExist");

		//Creo el objeto
		final Trip t = this.tripRepository.findOne(idTrip);
		//Comprobación que el manager es el logueado
		Assert.isTrue(t.getManager().equals(this.managerService.findByPrincipal()), "message.error.trip.notManager");
		//Comprobacion para saber si ese trip no esta cancelado ya
		Assert.isTrue(!t.getCancelled(), "message.error.trip.cancelled");
		//Comprobacion que la razon no esta vacia
		Assert.isTrue(!reasonCancelled.isEmpty(), "message.error.trip.reasonEmpty");

		//Compruebo si la fecha de comienzo es posterior de la fecha actual
		Assert.isTrue(t.getStartMoment().after(new Date()), "message.error.trip.startMoment.started");
		//Cancelo el trip
		t.setCancelled(true);
		t.setReason(reasonCancelled);

		Boolean isSuspicious;
		Manager principal;
		principal = this.managerService.findByPrincipal();

		isSuspicious = principal.getIsSuspicious();
		isSuspicious = isSuspicious || this.messageService.checkSpam(t.getReason());
		principal.setIsSuspicious(isSuspicious);
		this.managerService.saveFromEdit(principal);

		//Persiste en la bbdd el cambio
		this.tripRepository.save(t);

		for (final Application a : t.getApplications()) {

			a.setStatus("CANCELLED");

			this.applicationService.save(a);

			// Update message.
			final String subject = "The application status has changed";
			final String body = "The application status has been changed to CANCELLED";
			final String priority = "NEUTRAL";
			this.messageService.updateMessageToActorsInvolvedInApplication(a.getId(), subject, body, priority);

		}

	}
	//Generate an unic ticker
	public String generateTicker() {

		String ticker = "";
		// YYMMDD
		final Calendar date = Calendar.getInstance();
		ticker += date.get(Calendar.YEAR);
		ticker = ticker.substring(2, 4);
		ticker += date.get(Calendar.MONTH) + 1;
		if (date.get(Calendar.DATE) < 10)
			ticker += "0" + date.get(Calendar.DATE);
		else
			ticker += date.get(Calendar.DATE);

		ticker += "-";

		//WWWW
		final char[] chr = {
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
		};
		for (int i = 0; i <= 3; i++)
			ticker += chr[(int) (Math.random() * 26)];

		//Recursivo para ver si existe ya este ticker
		final Trip tripsWithSameTicker = this.findTripByTicker(ticker);
		if (tripsWithSameTicker == null)
			return ticker;
		else
			return this.generateTicker();
	}
	//Find a trip by its id
	public Trip findOne(final int tripId) {
		final Trip trip = this.tripRepository.findOne(tripId);
		return trip;
	}
	//For sponsoships
	public Trip findOneBySponsorshipId(final int sponsorshipId) {
		Trip trip;
		trip = this.tripRepository.findTripBySponsorship(sponsorshipId);
		return trip;
	}
	// Search the trips by a finder
	public Collection<Trip> search(final Finder finder) {
		Collection<Trip> found;
		found = this.tripRepository.search(finder.getMinPrice(), finder.getMaxPrice(), finder.getStartDate(), finder.getEndDate(), finder.getKeyWord());
		return found;
	}
	public Collection<Trip> search2(final Finder finder) {
		Collection<Trip> found;
		found = this.tripRepository.search(finder.getMinPrice(), finder.getMaxPrice(), finder.getStartDate(), finder.getEndDate(), finder.getKeyWord());
		return found;
	}
	public Collection<Trip> findTripPublished() {

		return this.tripRepository.findTripPublished();
	}

	public Collection<Trip> findTripsNotPublished() {

		return this.tripRepository.findTripsNotPublished();
	}

	public Collection<Trip> findAllNotPublishedByManagerId(final int managerId) {
		Collection<Trip> result;

		result = this.tripRepository.findAllNotPublishedByManagerId(managerId);

		return result;

	}

	public Collection<Trip> findTripsNotEnded() {

		return this.tripRepository.findTripsNotEnded();
	}

	public Collection<Trip> findAllPublishedAndNotStarted() {
		Collection<Trip> result;

		result = this.tripRepository.findAllPublishedAndNotStarted();

		return result;
	}

	public Collection<Trip> findAllPublishedNotStartedAndNotCancelled() {
		Collection<Trip> result;

		result = this.tripRepository.findAllPublishedNotStartedAndNotCancelled();

		return result;
	}

	public Boolean explorerTripsLessThanAMonth(final Trip trip) {
		final Boolean result = null;
		Collection<Trip> trips;
		final Explorer principal = this.explorerService.findByPrincipal();
		Date nowPlusAMonth;
		Date now;
		Calendar cal;

		now = new Date(System.currentTimeMillis() - 1);
		cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.MONTH, +1);
		nowPlusAMonth = cal.getTime();

		trips = this.tripRepository.findTripsExplorerInLessThanAMonth(principal.getId(), nowPlusAMonth);
		if (trips.contains(trip)) {

		}
		return result;

	}

	// TODO Descomentar cuando se arregle la query del finder.
	//	public Collection<Trip> getLimitFound(final Finder finder) {
	//		return this.tripRepository.getLimitFound(finder);
	//	}

	//C-14.6.3.1 The average price of the trips
	public Double findAveragePriceOfTrips() {
		return this.tripRepository.findAveragePriceOfTrips();
	}
	//C-14.6.3.2 The minimum price of the trips
	public Double findMinPriceOfTrips() {
		return this.tripRepository.findMinPriceOfTrips();
	}
	//C-14.6.3.3 The maximum price of the trips
	public Double findMaxPriceOfTrips() {
		return this.tripRepository.findMaxPriceOfTrips();
	}
	//C-14.6.3.4 The standard deviation of the price of the trips
	public Double findStantardDeviationOfPriceOfTrips() {
		return this.tripRepository.findStandardDeviationOfPriceOfTrips();
	}
	//C-14.6.9 The ratio of trips that have been cancelled versus the total number of trips that have been organized
	public Double findRatioOfTripsVersusNumberOfTrips() {
		return this.tripRepository.findRatioOfTripsVersusNumberOfTrips();
	}
	//C-14.6.10 The listing of trips that have got at least 10% more applications than the average, ordered by number of applications
	public Collection<Trip> findTripsWithAverageHigherThan10PerCentOrderedByApplicationNumber() {
		return this.tripRepository.findTripsWithAverageHigherThan10PerCentOrderedByApplicationNumber();

	}

	public Double findRatioOfTripsWithAuditRecord() {
		return this.tripRepository.findRatioOfTripsWithAuditRecord();
	}

	public Collection<Trip> findEndedTripsWithAcceptedApplication(final int explorerId) {

		return this.tripRepository.findEndedTripsWithAcceptedApplication(explorerId);
	}

	public Collection<Trip> findAllApplicableTripsByExplorer(final Explorer explorer) {
		Collection<Trip> publishedNotStartedAndNotCancelledTrips = new HashSet<>();
		final Collection<Trip> explorerApplicationsTrips = new HashSet<>();

		publishedNotStartedAndNotCancelledTrips = this.findAllPublishedNotStartedAndNotCancelled();

		for (final Application a : explorer.getApplications()) {
			final Trip trip = a.getTrip();
			explorerApplicationsTrips.add(trip);
		}

		publishedNotStartedAndNotCancelledTrips.removeAll(explorerApplicationsTrips);

		return publishedNotStartedAndNotCancelledTrips;
	}

	public Map<Integer, Collection<Integer>> findExplorersByTrip(final Collection<Trip> trips) {
		Assert.notNull(trips);

		final Map<Integer, Collection<Integer>> result = new HashMap<>();

		for (final Trip t : trips)
			for (final Application a : t.getApplications()) {
				final Explorer explorer = this.explorerService.findOneByApplicationId(a.getId());
				if (result.get(t) == null) {
					final Collection<Integer> userAccounts = new HashSet<>();
					userAccounts.add(explorer.getUserAccount().getId());
					result.put(t.getId(), userAccounts);
				} else {
					final Collection<Integer> value = result.get(t.getId());
					value.add(explorer.getUserAccount().getId());
					result.put(t.getId(), value);
				}
			}

		return result;
	}

}
