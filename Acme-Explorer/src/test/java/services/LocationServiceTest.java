
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
import domain.Location;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class LocationServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private LocationService	locationService;


	// Tests
	@Test
	public void testCreate() {
		Location locationToCreate;
		locationToCreate = this.locationService.create();
		Assert.notNull(locationToCreate);
	}

	@Test
	public void testSave() {
		Location locationToSave;
		locationToSave = this.locationService.create();
		this.locationService.save(locationToSave);
		Assert.notNull(locationToSave);
		Assert.notNull(locationToSave);
	}
	@Test
	public void testDelete() {
		final Location location;
		Collection<Location> allLocatios;

		allLocatios = this.locationService.listAll();
		location = allLocatios.iterator().next();
		this.locationService.save(location);
		Assert.notNull(location);

	}
	@Test
	public void testFindLocation() {
		final Location location;
		Collection<Location> allLocatios;

		allLocatios = this.locationService.listAll();
		location = allLocatios.iterator().next();
		Assert.notNull(location);
	}
}
