
package services;

import java.util.Collection;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Manager;
import domain.Ranger;
import domain.Trip;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class TripServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private TripService			tripService;
	@Autowired
	private ManagerService		managerService;
	@Autowired
	private RangerService		rangerService;
	@Autowired
	private LegalTextService	legalTextService;
	@Autowired
	private CategoryService		categoryService;


	// Tests

	@Test
	/**
	 * Test: create a trip.
	 */
	public void testCreateTrip() {
		//login
		this.authenticate("manager1");

		Trip t;
		t = this.tripService.create();

		Assert.isTrue(t.getManager().getUserAccount().getUsername().equals("manager1"));

		final String s = "Requirement1";

		t.setTitle("title1");
		t.setDescription("Description1");
		t.setRequirements(s);
		t.setPublicationDate(new DateTime().plusDays(10).toDate());
		t.setStartMoment(new DateTime().plusDays(20).toDate());
		t.setEndMoment(new DateTime().plusDays(30).toDate());
		t.setCategory(this.categoryService.findAll().iterator().next());
		t.setRanger(this.rangerService.findAll().iterator().next());
		t.setLegalText(this.legalTextService.findAll().iterator().next());
		this.tripService.save(t);

		this.unauthenticate();

	}

	@Test
	/**
	 * Test: modify a trip.
	 */
	public void testModifyTrip() {
		this.authenticate("manager1");

		Trip t;
		t = this.tripService.create();

		Assert.isTrue(t.getManager().getUserAccount().getUsername().equals("manager1"));

		final String s = "Requirement1";

		t.setTitle("title1");
		t.setDescription("Description1");
		t.setRequirements(s);
		t.setPublicationDate(new DateTime().plusDays(10).toDate());
		t.setStartMoment(new DateTime().plusDays(20).toDate());
		t.setEndMoment(new DateTime().plusDays(30).toDate());
		t.setCategory(this.categoryService.findAll().iterator().next());
		t.setRanger(this.rangerService.findAll().iterator().next());
		t.setLegalText(this.legalTextService.findAll().iterator().next());

		final Trip trip = this.tripService.save(t);

		trip.setTitle("Titulo");
		this.tripService.update(trip);

		this.unauthenticate();

	}

	@Test
	/**
	 * Test: delete a trip.
	 */
	public void testDeleteTrip() {
		//login
		this.authenticate("manager3");

		Trip t;
		t = this.tripService.create();

		Assert.isTrue(t.getManager().getUserAccount().getUsername().equals("manager3"));

		final String s = "Requirement1";

		t.setTitle("title1");
		t.setDescription("Description1");
		t.setRequirements(s);
		t.setPublicationDate(new DateTime().plusDays(10).toDate());
		t.setStartMoment(new DateTime().plusDays(20).toDate());
		t.setEndMoment(new DateTime().plusDays(30).toDate());
		t.setCategory(this.categoryService.findAll().iterator().next());
		t.setRanger(this.rangerService.findAll().iterator().next());
		t.setLegalText(this.legalTextService.findAll().iterator().next());

		final Trip trip = this.tripService.save(t);

		this.tripService.delete(trip.getId());
		this.unauthenticate();

	}

	@Test
	/**
	 * Test: list a trip.
	 */
	public void testListTrip() {
		//login
		this.authenticate("manager1");

		final Manager m = this.managerService.findByPrincipal();

		final Collection<Trip> t = m.getTrips();
		Assert.notEmpty(t);

		this.unauthenticate();

	}

	@Test
	/**
	 * Test: list all trips.
	 */
	public void testListAllTrip() {

		final Collection<Trip> t = this.tripService.findAll();
		Assert.notEmpty(t);

	}

	@Test
	/**
	 * Test: cancel a trip.
	 */
	public void testCancelTrip() {
		//login
		this.authenticate("manager1");

		final Manager m = this.managerService.findByPrincipal();

		final Trip t = m.getTrips().iterator().next();

		this.tripService.cancelledTrip(t.getId(), "Cancelled because yes");

		this.unauthenticate();

	}

	@Test
	/**
	 * Test: search keyword trips.
	 */
	public void testSearchKeyWordTrip() {

		final Collection<Trip> trips = this.tripService.findTripByKeyWord("Description of");
		Assert.notEmpty(trips);

	}

	@Test
	public void testSuspicious() {

		Collection<Manager> allManagers;
		allManagers = this.managerService.findAll();
		Manager principal = null;

		for (final Manager manager : allManagers)
			if (!manager.getIsSuspicious()) {
				principal = manager;
				break;
			}

		this.authenticate(principal.getUserAccount().getUsername());

		Trip tripToCreate;

		tripToCreate = this.tripService.create();

		final String s = "Requirement1";

		tripToCreate.setTitle("sex");
		tripToCreate.setDescription("Description1");
		tripToCreate.setRequirements(s);
		tripToCreate.setPublicationDate(new DateTime().plusDays(10).toDate());
		tripToCreate.setStartMoment(new DateTime().plusDays(20).toDate());
		tripToCreate.setEndMoment(new DateTime().plusDays(30).toDate());

		Collection<Ranger> allRangers;
		allRangers = this.rangerService.findAll();

		tripToCreate.setRanger(allRangers.iterator().next());

		tripToCreate.setCategory(this.categoryService.findAll().iterator().next());
		tripToCreate.setLegalText(this.legalTextService.findAll().iterator().next());

		this.tripService.save(tripToCreate);

		final Trip tripSaved = this.tripService.findTripByTicker(tripToCreate.getTicker());
		principal = this.managerService.findByPrincipal();

		final Trip tripInDB = this.tripService.findOne(tripSaved.getId());

		Assert.isTrue(principal.getTrips().contains(tripInDB));
		Assert.isTrue(principal.getIsSuspicious());

		this.unauthenticate();

	}
}
