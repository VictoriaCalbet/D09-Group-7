
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
import domain.Category;
import domain.LegalText;
import domain.Note;
import domain.Ranger;
import domain.Trip;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class NoteServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private NoteService	noteService;

	//Supporting services
	@Autowired
	private TripService	tripService;
	
	@Autowired
	private RangerService	rangerService;
	
	@Autowired
	private CategoryService	categoryService;
	
	@Autowired
	private LegalTextService	legalTextService;


	// Tests

	@Test
	public void createNote() {
		this.authenticate("auditor1");
		final Note n = this.noteService.create();

		Assert.isTrue(!(n.getId() > 0));
		this.unauthenticate();
	}

	@Test
	public void saveFromCreate() {
		this.authenticate("manager1");

		Trip t;
		Collection<Trip> allTrips;

		allTrips = this.tripService.findAll();
		t = allTrips.iterator().next();

		this.unauthenticate();

		super.authenticate("auditor1");
		final Note n = this.noteService.create();

		Assert.isTrue(!(n.getId() > 0));

		n.setRemark("This trip looks great!");
		n.setTrip(t);
		final Note savedNote = this.noteService.saveFromCreate(n);

		Assert.isTrue(savedNote.getId() > 0);

		this.unauthenticate();

	}

	@Test
	public void saveFromReply() {

		super.authenticate("admin");
		final LegalText lt = this.legalTextService.create();

		Assert.isTrue(!(lt.getId() > 0));

		lt.setBody("Body of the legal text");
		lt.setIsDraft(false);
		lt.setNumberLaw("Laws");
		lt.setTitle("Legal text 8");

		final LegalText savedLegalT = this.legalTextService.save(lt);
		//Creation of category
		Category category;
		category = this.categoryService.create();
		category.setName("This is a category");
		Category savedCategory;
		savedCategory = this.categoryService.save(category);
		super.unauthenticate();

		//login
		this.authenticate("manager1");

		Trip t;
		t = this.tripService.create();

		final String s = "Requirement1";

		t.setTitle("title1");
		t.setDescription("Description1");
		t.setRequirements(s);
		t.setPublicationDate(new DateTime().plusDays(10).toDate());
		t.setStartMoment(new DateTime().plusDays(11).toDate());
		t.setEndMoment(new DateTime().plusDays(22).toDate());
		t.setLegalText(savedLegalT);
		t.setCategory(savedCategory);
		Collection<Ranger> allRangers;
		allRangers = this.rangerService.findAll();

		t.setRanger(allRangers.iterator().next());

		final Trip tripInDB = this.tripService.save(t);

		this.unauthenticate();

		super.authenticate("auditor1");
		final Note n = this.noteService.create();

		Assert.isTrue(!(n.getId() > 0));

		n.setRemark("This trip looks great!");
		n.setTrip(tripInDB);
		final Note savedNote = this.noteService.saveFromCreate(n);

		Assert.isTrue(savedNote.getId() > 0);
		
		this.unauthenticate();
		
		this.authenticate("manager1");

		savedNote.setResponse("Good to know!");

		final Note savedNote2 = this.noteService.saveFromReply(savedNote);

		Assert.isTrue(savedNote2.getId() > 0);
		Assert.notNull(savedNote2.getResponse());
		Assert.notNull(savedNote2.getRemark());
		this.unauthenticate();

	}

	@Test
	public void findMaxNotesPerTrip() {

		final Integer max = this.noteService.findMaxNotesPerTrip();
		Assert.notNull(max);
	}

	@Test
	public void findAverageNumberOfNotesPerTrip() {

		final Double average = this.noteService.findAverageNumberOfNotesPerTrip();
		Assert.notNull(average);
	}

	@Test
	public void findStandardDeviationOfNotesPerTrip() {

		final Double st = this.noteService.findStandardDeviationOfNotesPerTrip();
		Assert.notNull(st);
	}

}
