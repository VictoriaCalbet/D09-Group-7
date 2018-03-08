
package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Application;
import domain.Explorer;
import domain.Trip;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ApplicationServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private ApplicationService	applicationService;
	@Autowired
	private ExplorerService		explorerService;
	@Autowired
	private TripService			tripService;


	// Tests

	@Test
	public void testCreate1() {
		this.authenticate("explorer1");

		final Trip trip = this.tripService.findTripByTicker("171001-TTTS");
		Assert.notNull(trip);

		final Application applicationToCreate = this.applicationService.create(trip.getId());
		Assert.notNull(applicationToCreate.getMomentMade());
		Assert.isTrue(applicationToCreate.getStatus().equals("PENDING"));
		Assert.isNull(applicationToCreate.getComment());
		Assert.isTrue(applicationToCreate.getCreditCards().isEmpty());
		Assert.isNull(applicationToCreate.getReasonDenied());

		Assert.isTrue(applicationToCreate.getTrip().getId() == trip.getId());

		this.unauthenticate();
	}

	@Test
	public void testSaveFromCreate1() {
		this.authenticate("explorer1");

		final Trip trip = this.tripService.findTripByTicker("171001-TTTS");
		Assert.notNull(trip);

		final Application applicationToCreate = this.applicationService.create(trip.getId());
		Assert.notNull(applicationToCreate.getMomentMade());
		Assert.isTrue(applicationToCreate.getStatus().equals("PENDING"));
		Assert.isNull(applicationToCreate.getComment());
		Assert.isTrue(applicationToCreate.getCreditCards().isEmpty());
		Assert.isNull(applicationToCreate.getReasonDenied());

		Assert.isTrue(applicationToCreate.getTrip().getId() == trip.getId());

		this.unauthenticate();

		this.authenticate("explorer1");

		Application applicationToSave;

		applicationToCreate.setComment("Application example comment");

		applicationToSave = this.applicationService.saveFromCreate(applicationToCreate);

		Assert.notNull(applicationToSave);
		Assert.isTrue(applicationToSave.getStatus().equals("PENDING"));
		Assert.isTrue(applicationToSave.getComment().equals(applicationToCreate.getComment()));

		Assert.isTrue(applicationToSave.getTrip().getApplications().contains(applicationToSave));

		this.unauthenticate();
	}

	@Test
	/**
	 * Test: list an application.
	 */
	public void testAcceptCancelApplication() {
		//login
		this.authenticate("explorer1");

		final Explorer e = this.explorerService.findByPrincipal();
		Collection<Application> explorerApplications;
		Application a = null;

		explorerApplications = e.getApplications();
		Assert.notNull(explorerApplications);
		Assert.notEmpty(explorerApplications);

		for (final Application application : explorerApplications)
			if (application.getStatus().equals("DUE")) {
				a = application;
				break;
			}

		Assert.notNull(a);
		Assert.isTrue(a.getStatus().equals("DUE"));

		this.applicationService.acceptApplication(a);

		Assert.isTrue(a.getStatus() == "ACCEPTED");
		this.applicationService.cancelApplication(a);
		Assert.isTrue(a.getStatus() == "CANCELLED");

		this.unauthenticate();

	}

}
