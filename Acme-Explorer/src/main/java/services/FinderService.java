
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.FinderRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Explorer;
import domain.Finder;
import domain.SystemConfiguration;
import domain.Trip;

@Service
@Transactional
public class FinderService {

	// Managed Repository
	@Autowired
	private FinderRepository			finderRepository;

	// Supporting Services
	@Autowired
	private ExplorerService				explorerService;

	@Autowired
	private SystemConfigurationService	systemcConfigurationService;

	@Autowired
	private TripService					tripService;
	@Autowired
	private MessageService				messageService;


	// Constructor

	public FinderService() {
		super();
	}

	// Simple CRUD methods

	public Finder create() {

		Finder created;
		final Collection<Trip> trips = new ArrayList<Trip>();
		created = new Finder();
		created.setKeyWord("");
		created.setFound(trips);

		return created;
	}
	public Collection<Finder> findAll() {
		Collection<Finder> result;

		result = this.finderRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	private Finder findOne(final int finderId) {

		Finder result;

		result = this.finderRepository.findOne(finderId);

		return result;
	}

	public Finder save(final Finder finder) {
		Finder finderResult;
		if (finder.getId() != 0) {
			final Finder finderInDB = this.finderRepository.findOne(finder.getId());
			if (this.checkCache(finder, finderInDB))
				finderResult = this.save(finder, finderInDB.getMoment(), finderInDB.getFound());
			else {
				Collection<Trip> tripsFounds;
				tripsFounds = new ArrayList<Trip>();
				finderResult = this.save(finder, null, tripsFounds);
			}
		} else {
			Collection<Trip> tripsFounds;
			tripsFounds = new ArrayList<Trip>();
			finderResult = this.save(finder, null, tripsFounds);
		}
		return finderResult;
	}
	private Finder save(final Finder finder, final Date moment, final Collection<Trip> tripsFounds) {

		Finder saved;

		Assert.notNull(finder);

		Assert.notNull(finder.getExplorer());
		if (finder.getId() != 0) {
			Finder finderInDB;

			this.isExplorerAuthenticated();
			Assert.isTrue(this.checkPrincipal(finder));
			this.isSuspicious(finder.getKeyWord());
			finderInDB = this.findOne(finder.getId());
			Assert.isTrue(finderInDB.getExplorer().getId() == finder.getExplorer().getId());

		}

		finder.setMoment(moment);
		finder.setFound(tripsFounds);

		saved = this.finderRepository.save(finder);
		return saved;
	}
	public void resetCacheAndSave(final Finder finder) {

		final Date lastUpdate = new Date(System.currentTimeMillis() - 1);
		finder.setMoment(lastUpdate);
		this.finderRepository.save(finder);

	}

	private boolean checkPrincipal(final Finder finder) {

		Boolean result = false;
		final UserAccount explorer = finder.getExplorer().getUserAccount();
		final UserAccount principal = LoginService.getPrincipal();
		if (explorer.equals(principal))
			result = true;
		return result;
	}

	public Boolean checkCache(final Finder finder, final Finder finderDB) {

		Boolean areEquals = false;
		if (finder.getId() != 0) {
			final String keyWord1 = finder.getKeyWord();
			final String keyWord2 = finderDB.getKeyWord();
			final Boolean keyWord = (keyWord1 == null || keyWord2 == null) ? keyWord1 == null && keyWord2 == null : keyWord1.equals(keyWord2);
			final Date start1 = finder.getStartDate();
			final Date start2 = finderDB.getStartDate();
			final Boolean start = (start1 == null || start2 == null) ? start1 == null && start2 == null : start1.equals(start2);
			final Date end1 = finder.getEndDate();
			final Date end2 = finderDB.getEndDate();
			final Boolean end = (end1 == null || end2 == null) ? end1 == null && end2 == null : end1.equals(end2);
			final Double max1 = finder.getMaxPrice();
			final Double max2 = finderDB.getMaxPrice();
			final Boolean max = (max1 == null || max2 == null) ? max1 == null && max2 == null : max1.equals(max2);
			final Double min1 = finder.getMinPrice();
			final Double min2 = finderDB.getMinPrice();
			final Boolean min = (min1 == null || min2 == null) ? min1 == null && min2 == null : min1.equals(min2);

			areEquals = keyWord && max && min && start && end;
		}
		return areEquals;
	}
	public Boolean checkCacheTime(final Finder finder) {
		Boolean result = false;
		if (finder.getId() != 0) {
			final SystemConfiguration system = this.systemcConfigurationService.findMain();
			final DateTime last = new DateTime(finder.getMoment());
			final DateTime now = DateTime.now();

			if (now.minusHours(system.getDefaultCacheTime()).isAfter(last))
				result = true;
		}

		return result;
	}
	public Finder findFinderByExplorerPrincipal() {
		this.isExplorerAuthenticated();
		Explorer principal;
		principal = this.explorerService.findByPrincipal();
		final Finder result = this.finderRepository.findByExplorerId(principal.getId());
		return result;
	}
	// Other business methods
	public Collection<Trip> getTripsFound(final Finder finder) {
		Assert.notNull(this.finderRepository.findOne(finder.getId()));
		final List<Trip> trips = new ArrayList<Trip>();
		final Boolean needQuery = finder.getMoment() == null || this.checkCacheTime(finder);
		Date newCacheDate;
		if (((finder.getStartDate() == null && finder.getEndDate() == null && finder.getKeyWord() == null && finder.getMaxPrice() == null && finder.getMinPrice() == null) && needQuery)) {
			trips.addAll(this.getDefaultNumberOfTrips(this.tripService.findTripPublished()));
			newCacheDate = new Date(System.currentTimeMillis() - 1);

			this.save(finder, newCacheDate, trips);
		} else if (needQuery) {
			trips.addAll(this.getDefaultNumberOfTrips(this.tripService.search2(finder)));
			newCacheDate = new Date(System.currentTimeMillis() - 1);

			this.save(finder, newCacheDate, trips);
		} else
			trips.addAll(finder.getFound());
		java.util.Collections.sort(trips, new Comparator<Trip>() {

			@Override
			public int compare(final Trip t1, final Trip t2) {
				return t1.getId() - t2.getId();
			}
		});
		return trips;

	}
	private Collection<Trip> getDefaultNumberOfTrips(final Collection<Trip> trips) {
		final Integer max = this.systemcConfigurationService.findMain().getDefaultFinderNumber();

		final List<Trip> defaultNumberOfTrips = new ArrayList<Trip>();
		Integer counter = 0;
		for (final Trip t : (ArrayList<Trip>) trips) {
			if (t.getPublicationDate().before(new Date(System.currentTimeMillis()))) {
				counter++;
				defaultNumberOfTrips.add(t);
			}
			if (counter.equals(max))
				break;
		}

		return defaultNumberOfTrips;
	}

	private void isExplorerAuthenticated() {
		final Explorer principal;
		principal = this.explorerService.findByPrincipal();
		Assert.notNull(principal);
		final Collection<Authority> authorities;
		authorities = principal.getUserAccount().getAuthorities();
		Boolean isExplorer = false;
		for (final Authority a : authorities)
			if (a.getAuthority().equals("EXPLORER")) {
				isExplorer = true;
				break;
			}
		Assert.isTrue(isExplorer);
	}
	private void isSuspicious(final String keyWord) {
		Boolean isSuspicious = false;

		if (keyWord != null) {
			final Boolean check = this.messageService.checkSpam(keyWord);
			if (check)
				isSuspicious = true;
		}
		Explorer explorer;
		explorer = this.explorerService.findByPrincipal();
		if (isSuspicious)
			explorer.setIsSuspicious(true);

	}

}
