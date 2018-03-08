
package services;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Stage;
import domain.Trip;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class StageServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private StageService		stageService;

	@Autowired
	private TripService			tripService;
	@Autowired
	private RangerService		rangerService;
	@Autowired
	private LegalTextService	legalTextService;
	@Autowired
	private CategoryService		categoryService;


	// Tests

	@Test
	/**
	 * Test: create a stage.
	 */
	public void testCreateStage() {
		//login
		this.authenticate("manager1");

		final Stage s;
		final Trip t = this.tripService.create();
		t.setTitle("Sevilla");
		t.setDescription("New trip");
		t.setPublicationDate(new DateTime().plusDays(10).toDate());
		t.setStartMoment(new DateTime().plusDays(20).toDate());
		t.setEndMoment(new DateTime().plusDays(30).toDate());
		t.setRanger(this.rangerService.findAll().iterator().next());
		t.setLegalText(this.legalTextService.findAll().iterator().next());
		t.setCategory(this.categoryService.findAll().iterator().next());
		final Trip savedTrip = this.tripService.save(t);
		s = this.stageService.create(savedTrip.getId());

		s.setTitle("Stage13");
		s.setDescription("Description of stage 13");
		s.setPrice(100.0);

		this.stageService.save(s);

		this.unauthenticate();
	}
	@Test
	/**
	 * Test: modify a stage.
	 */
	public void testModifyStage() {
		//login
		this.authenticate("manager3");

		final Trip t = this.tripService.create();
		t.setTitle("Sevilla");
		t.setDescription("New trip");
		t.setRequirements("");
		t.setPublicationDate(new DateTime().plusDays(10).toDate());
		t.setStartMoment(new DateTime().plusDays(20).toDate());
		t.setEndMoment(new DateTime().plusDays(30).toDate());
		t.setRanger(this.rangerService.findAll().iterator().next());
		t.setLegalText(this.legalTextService.findAll().iterator().next());
		t.setCategory(this.categoryService.findAll().iterator().next());

		final Trip savedTrip = this.tripService.save(t);

		final Stage s = this.stageService.create(savedTrip.getId());

		s.setTitle("Stage13");
		s.setDescription("Description of stage 13");
		s.setPrice(100.0);

		final Stage stage = this.stageService.save(s);

		stage.setTitle("Stage22");
		stage.setDescription("Description of stage 22");
		stage.setPrice(3400.0);

		this.stageService.update(stage);
		this.unauthenticate();

	}

	@Test
	/**
	 * Test: delete stage.
	 */
	public void testDeleteStage() {
		this.authenticate("manager3");

		final Trip t = this.tripService.create();
		t.setTitle("Sevilla");
		t.setDescription("New trip");
		t.setRequirements("");
		t.setPublicationDate(new DateTime().plusDays(10).toDate());
		t.setStartMoment(new DateTime().plusDays(20).toDate());
		t.setEndMoment(new DateTime().plusDays(30).toDate());
		t.setRanger(this.rangerService.findAll().iterator().next());
		t.setLegalText(this.legalTextService.findAll().iterator().next());
		t.setCategory(this.categoryService.findAll().iterator().next());

		final Trip savedTrip = this.tripService.save(t);

		final Stage s = this.stageService.create(savedTrip.getId());

		s.setTitle("Stage13");
		s.setDescription("Description of stage 13");
		s.setPrice(100.0);

		final Stage stage = this.stageService.save(s);

		this.stageService.delete(stage.getId());
		this.unauthenticate();
	}

}
