
package services;

import java.sql.Date;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Explorer;
import domain.Location;
import domain.Manager;
import domain.SurvivalClass;
import domain.Trip;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class SurvivalClassServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private SurvivalClassService	survivalClassService;
	@Autowired
	private LocationService			locationService;

	@Autowired
	private ExplorerService			explorerService;

	@Autowired
	private ManagerService			managerService;


	// Tests
	@Test
	public void testCreate() {

		super.authenticate("manager2");
		SurvivalClass survivalClassToCreate;
		survivalClassToCreate = this.survivalClassService.create();

		Location location;
		Collection<Location> allLocations;
		survivalClassToCreate.setMomentOrganized(new Date(System.currentTimeMillis() - 1));
		allLocations = this.locationService.listAll();
		location = allLocations.iterator().next();

		survivalClassToCreate.setLocation(location);
		Assert.isTrue(!(survivalClassToCreate.getId() > 0));
		Assert.notNull(survivalClassToCreate);
		this.unauthenticate();
	}
	@Test
	public void testSaveFromCreate() {
		//login

		super.authenticate("manager1");
		final SurvivalClass sc = this.survivalClassService.listAll().iterator().next();
		sc.setTitle("title2");
		final Manager principal = this.managerService.findByPrincipal();
		Location location;
		Collection<Location> allLocations;

		allLocations = this.locationService.listAll();
		location = allLocations.iterator().next();
		final Trip t = sc.getTrip();
		sc.setLocation(location);
		sc.setMomentOrganized(new Date(System.currentTimeMillis() - 1));
		sc.setDescription("Description2");
		this.survivalClassService.save(sc);
		Assert.notNull(sc);
		principal.getTrips().add(t);

		this.managerService.saveFromEdit(principal);
		Assert.isTrue(t.getSurvivalClasses().contains(sc));

		super.unauthenticate();

	}
	@Test
	public void testDelete() {
		//CREATE
		this.authenticate("manager1");
		final SurvivalClass sc;
		sc = this.survivalClassService.listAll().iterator().next();

		Location location;

		location = sc.getLocation();
		this.survivalClassService.save(sc);
		final Trip t = sc.getTrip();

		this.locationService.save(location);
		Assert.isTrue(sc.getId() > 0);
		Assert.notNull(sc.getLocation());
		//System.out.println(sc.getId());
		this.survivalClassService.saveFromEdit(sc);
		this.survivalClassService.delete(sc);

		Assert.isTrue(!(t.getSurvivalClasses().contains(sc)));
		this.unauthenticate();
	}

	@Test
	public void testEnrolSurvivalClass() {
		this.authenticate("explorer2");
		final Explorer principal = this.explorerService.findByPrincipal();
		//		final Explorer explorer = (Explorer) this.actorService.findByName("explorer2");
		final Collection<SurvivalClass> scs = principal.getSurvivalClasses();
		Assert.notEmpty(scs);
		Assert.notNull(principal);
		Assert.isTrue((principal.getId() > 0));
		Assert.notNull(principal.getApplications());
		Assert.notNull(principal.getSurvivalClasses());
		for (final SurvivalClass sc : scs)
			if (!principal.getSurvivalClasses().contains(sc)) {
				Assert.notNull(sc);
				this.survivalClassService.enrolASurvivalClass(sc.getId());
				Assert.isTrue(principal.getSurvivalClasses().contains(sc));
			}
		this.explorerService.saveFromEdit(principal);
		Assert.notNull(principal);
		this.unauthenticate();
	}
}
