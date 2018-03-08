
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
import domain.Story;
import domain.Trip;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class StoryServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private StoryService	storyService;

	//Supporting services
	@Autowired
	private TripService		tripService;


	// Tests

	@Test
	public void testCreateStory() {
		super.authenticate("explorer1");
		final Story s = this.storyService.create();

		Assert.isTrue(!(s.getId() > 0));
		super.unauthenticate();
	}

	@Test
	public void saveStory() {
		super.authenticate("explorer1");
		final Story s = this.storyService.create();

		Assert.isTrue(!(s.getId() > 0));

		s.setPieceOfText("Some text about the trip.");
		s.setTitle("Story 6");

		Trip t;
		Collection<Trip> allTrips;
		
		allTrips = this.tripService.findAll();
		t = allTrips.iterator().next();
		
		s.setTrip(t);
		
		final Story savedS = this.storyService.save(s);

		Assert.isTrue(savedS.getId() > 0);
		
		super.unauthenticate();
	}

}
