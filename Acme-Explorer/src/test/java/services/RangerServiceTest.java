
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

import security.Authority;
import security.UserAccount;
import utilities.AbstractTest;
import domain.Folder;
import domain.Ranger;
import domain.Trip;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class RangerServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private RangerService		rangerService;

	@Autowired
	private FolderService		folderService;
	@Autowired
	private TripService			tripService;
	@Autowired
	private LegalTextService	legalTextService;
	@Autowired
	private CategoryService		categoryService;


	// Tests
	@Test
	public void testCreate1() {
		Ranger rangerToCreate;

		rangerToCreate = this.rangerService.create();

		Assert.isTrue(!(rangerToCreate.getId() > 0));
		Assert.isNull(rangerToCreate.getName());
		Assert.isNull(rangerToCreate.getSurname());
		Assert.isNull(rangerToCreate.getEmail());
		Assert.isNull(rangerToCreate.getPhone());
		Assert.isNull(rangerToCreate.getAddress());
		Assert.isNull(rangerToCreate.getCurriculum());
		Assert.isTrue(rangerToCreate.getIsSuspicious() == false);
		Assert.isTrue(rangerToCreate.getIsBanned() == false);

		Assert.isTrue(rangerToCreate.getSocialIdentities().isEmpty());
		Assert.isTrue(rangerToCreate.getFolders().isEmpty());
		Assert.isTrue(rangerToCreate.getSent().isEmpty());
		Assert.isTrue(rangerToCreate.getReceived().isEmpty());

		Assert.isTrue(rangerToCreate.getUserAccount().getAuthorities().iterator().next().getAuthority().equals(Authority.RANGER));

	}

	@Test
	public void testSaveFromCreate1() {
		Ranger rangerToCreate;

		rangerToCreate = this.rangerService.create();

		Assert.isTrue(!(rangerToCreate.getId() > 0));
		Assert.isNull(rangerToCreate.getName());
		Assert.isNull(rangerToCreate.getSurname());
		Assert.isNull(rangerToCreate.getEmail());
		Assert.isNull(rangerToCreate.getPhone());
		Assert.isNull(rangerToCreate.getAddress());
		Assert.isNull(rangerToCreate.getCurriculum());
		Assert.isTrue(rangerToCreate.getIsSuspicious() == false);
		Assert.isTrue(rangerToCreate.getIsBanned() == false);

		Assert.isTrue(rangerToCreate.getSocialIdentities().isEmpty());
		Assert.isTrue(rangerToCreate.getFolders().isEmpty());
		Assert.isTrue(rangerToCreate.getSent().isEmpty());
		Assert.isTrue(rangerToCreate.getReceived().isEmpty());

		Assert.isTrue(rangerToCreate.getUserAccount().getAuthorities().iterator().next().getAuthority().equals(Authority.RANGER));

		final Ranger rangerToSave;
		final UserAccount userAccountToSave;

		rangerToCreate.setName("New ranger name");
		rangerToCreate.setSurname("New ranger surname");
		rangerToCreate.setEmail("newranger@ranger.com");
		rangerToCreate.setPhone("+34954954954");
		rangerToCreate.setAddress("New ranger address");

		userAccountToSave = rangerToCreate.getUserAccount();
		userAccountToSave.setUsername("New ranger");
		userAccountToSave.setPassword("New ranger");
		rangerToCreate.setUserAccount(userAccountToSave);

		rangerToSave = this.rangerService.saveFromCreate(rangerToCreate);

		Ranger rangerInDB;
		rangerInDB = this.rangerService.findOne(rangerToSave.getId());
		Assert.notNull(rangerInDB);

		Assert.isTrue(rangerInDB.getName().equals(rangerToCreate.getName()));
		Assert.isTrue(rangerInDB.getSurname().equals(rangerToCreate.getSurname()));
		Assert.isTrue(rangerInDB.getEmail().equals(rangerToCreate.getEmail()));
		Assert.isTrue(rangerInDB.getPhone().equals(rangerToCreate.getPhone()));
		Assert.isTrue(rangerInDB.getAddress().equals(rangerToCreate.getAddress()));
		Assert.isTrue(rangerInDB.getUserAccount().getUsername().equals(rangerToCreate.getUserAccount().getUsername()));

		Assert.isTrue(rangerInDB.getSent().isEmpty());
		Assert.isTrue(rangerInDB.getReceived().isEmpty());
		Assert.isTrue(rangerInDB.getFolders().size() == 5);

		for (final Folder f : rangerInDB.getFolders())
			Assert.isTrue(f.getSystemFolder());

		this.authenticate(rangerInDB.getUserAccount().getUsername());

		this.unauthenticate();
	}

	@Test
	public void testSaveFromEdit1() {
		this.authenticate("admin");

		Ranger rangerToCreate;

		rangerToCreate = this.rangerService.create();

		Assert.isTrue(!(rangerToCreate.getId() > 0));
		Assert.isNull(rangerToCreate.getName());
		Assert.isNull(rangerToCreate.getSurname());
		Assert.isNull(rangerToCreate.getEmail());
		Assert.isNull(rangerToCreate.getPhone());
		Assert.isNull(rangerToCreate.getAddress());
		Assert.isNull(rangerToCreate.getCurriculum());
		Assert.isTrue(rangerToCreate.getIsSuspicious() == false);
		Assert.isTrue(rangerToCreate.getIsBanned() == false);

		Assert.isTrue(rangerToCreate.getSocialIdentities().isEmpty());
		Assert.isTrue(rangerToCreate.getFolders().isEmpty());
		Assert.isTrue(rangerToCreate.getSent().isEmpty());
		Assert.isTrue(rangerToCreate.getReceived().isEmpty());

		Assert.isTrue(rangerToCreate.getUserAccount().getAuthorities().iterator().next().getAuthority().equals(Authority.RANGER));

		this.unauthenticate();

		this.authenticate("admin");

		final Ranger rangerToSave;
		final UserAccount userAccountToSave;

		rangerToCreate.setName("New ranger name");
		rangerToCreate.setSurname("New ranger surname");
		rangerToCreate.setEmail("newranger@ranger.com");
		rangerToCreate.setPhone("+34954954954");
		rangerToCreate.setAddress("New ranger address");

		userAccountToSave = rangerToCreate.getUserAccount();
		userAccountToSave.setUsername("New ranger");
		userAccountToSave.setPassword("New ranger");
		rangerToCreate.setUserAccount(userAccountToSave);

		rangerToSave = this.rangerService.saveFromCreate(rangerToCreate);

		Ranger rangerInDB;
		rangerInDB = this.rangerService.findOne(rangerToSave.getId());
		Assert.notNull(rangerInDB);

		Assert.isTrue(rangerInDB.getName().equals(rangerToCreate.getName()));
		Assert.isTrue(rangerInDB.getSurname().equals(rangerToCreate.getSurname()));
		Assert.isTrue(rangerInDB.getEmail().equals(rangerToCreate.getEmail()));
		Assert.isTrue(rangerInDB.getPhone().equals(rangerToCreate.getPhone()));
		Assert.isTrue(rangerInDB.getAddress().equals(rangerToCreate.getAddress()));
		Assert.isTrue(rangerInDB.getUserAccount().getUsername().equals(rangerToCreate.getUserAccount().getUsername()));

		Assert.isTrue(rangerInDB.getSent().isEmpty());
		Assert.isTrue(rangerInDB.getReceived().isEmpty());
		Assert.isTrue(rangerInDB.getFolders().size() == 5);

		for (final Folder f : rangerInDB.getFolders())
			Assert.isTrue(f.getSystemFolder());

		this.unauthenticate();

		// Logging as the new user.
		this.authenticate(rangerInDB.getUserAccount().getUsername());

		Ranger principal;
		Ranger rangerToRetrieve;
		principal = this.rangerService.findByPrincipal();
		Assert.notNull(principal);
		Assert.isTrue(principal.equals(rangerInDB));

		principal.setName("Updated ranger name");
		rangerToRetrieve = this.rangerService.saveFromEdit(principal);

		principal = null;
		principal = this.rangerService.findOne(rangerToRetrieve.getId());
		Assert.notNull(principal);

		Assert.isTrue(principal.getName().equals(rangerToRetrieve.getName()));
		Assert.isTrue(principal.getFolders().size() == 5);

		for (final Folder f : principal.getFolders())
			Assert.isTrue(f.getSystemFolder());

		this.unauthenticate();
	}

	@Test
	public void testAddFolderToRanger1() {
		this.authenticate("admin");

		Ranger rangerToCreate;

		rangerToCreate = this.rangerService.create();

		Assert.isTrue(!(rangerToCreate.getId() > 0));
		Assert.isNull(rangerToCreate.getName());
		Assert.isNull(rangerToCreate.getSurname());
		Assert.isNull(rangerToCreate.getEmail());
		Assert.isNull(rangerToCreate.getPhone());
		Assert.isNull(rangerToCreate.getAddress());
		Assert.isNull(rangerToCreate.getCurriculum());
		Assert.isTrue(rangerToCreate.getIsSuspicious() == false);
		Assert.isTrue(rangerToCreate.getIsBanned() == false);

		Assert.isTrue(rangerToCreate.getSocialIdentities().isEmpty());
		Assert.isTrue(rangerToCreate.getFolders().isEmpty());
		Assert.isTrue(rangerToCreate.getSent().isEmpty());
		Assert.isTrue(rangerToCreate.getReceived().isEmpty());

		Assert.isTrue(rangerToCreate.getUserAccount().getAuthorities().iterator().next().getAuthority().equals(Authority.RANGER));

		this.unauthenticate();

		this.authenticate("admin");

		final Ranger rangerToSave;
		final UserAccount userAccountToSave;

		rangerToCreate.setName("New ranger name");
		rangerToCreate.setSurname("New ranger surname");
		rangerToCreate.setEmail("newranger@ranger.com");
		rangerToCreate.setPhone("+34954954954");
		rangerToCreate.setAddress("New ranger address");

		userAccountToSave = rangerToCreate.getUserAccount();
		userAccountToSave.setUsername("New ranger");
		userAccountToSave.setPassword("New ranger");
		rangerToCreate.setUserAccount(userAccountToSave);

		rangerToSave = this.rangerService.saveFromCreate(rangerToCreate);

		Ranger rangerInDB;
		rangerInDB = this.rangerService.findOne(rangerToSave.getId());
		Assert.notNull(rangerInDB);

		Assert.isTrue(rangerInDB.getName().equals(rangerToCreate.getName()));
		Assert.isTrue(rangerInDB.getSurname().equals(rangerToCreate.getSurname()));
		Assert.isTrue(rangerInDB.getEmail().equals(rangerToCreate.getEmail()));
		Assert.isTrue(rangerInDB.getPhone().equals(rangerToCreate.getPhone()));
		Assert.isTrue(rangerInDB.getAddress().equals(rangerToCreate.getAddress()));
		Assert.isTrue(rangerInDB.getUserAccount().getUsername().equals(rangerToCreate.getUserAccount().getUsername()));

		Assert.isTrue(rangerInDB.getSent().isEmpty());
		Assert.isTrue(rangerInDB.getReceived().isEmpty());
		Assert.isTrue(rangerInDB.getFolders().size() == 5);

		for (final Folder f : rangerInDB.getFolders())
			Assert.isTrue(f.getSystemFolder());

		this.unauthenticate();

		// Logging as the new user.
		this.authenticate(rangerInDB.getUserAccount().getUsername());

		Ranger principal;
		principal = this.rangerService.findByPrincipal();
		Assert.notNull(principal);
		Assert.isTrue(principal.equals(rangerInDB));
		Assert.isTrue(principal.getFolders().size() == 5);

		// Creation of a new folder for the user.
		final Folder folderToCreate;

		folderToCreate = this.folderService.create();

		Assert.isTrue(!(folderToCreate.getId() > 0));
		Assert.isNull(folderToCreate.getName());
		Assert.isTrue(!folderToCreate.getSystemFolder());
		Assert.isNull(folderToCreate.getParent());

		final Folder folderToSave;
		final Folder folderToRetrieve;

		folderToCreate.setName("Folder created");
		folderToSave = this.folderService.saveFromCreate(folderToCreate);

		Assert.isTrue(folderToSave.getId() > 0);
		Assert.isTrue(folderToSave.getName().equals(folderToCreate.getName()));
		Assert.isTrue(!folderToSave.getSystemFolder());
		Assert.isNull(folderToSave.getParent());

		principal = this.rangerService.findByPrincipal();
		Assert.notNull(principal);

		folderToRetrieve = this.folderService.findFolderByOwnerAndName(principal.getId(), folderToSave.getName());
		Assert.notNull(folderToRetrieve);
		Assert.isTrue(principal.getFolders().size() == 6);

		for (final Folder f : principal.getFolders())
			if (f.getName().equals(folderToSave.getName()))
				Assert.isTrue(!f.getSystemFolder());
			else
				Assert.isTrue(f.getSystemFolder());

		this.unauthenticate();
	}

	@Test
	public void testBan1() {
		this.authenticate("admin");

		Collection<Ranger> allRangers;
		Ranger rangerToBan = null;
		Ranger rangerBanned;

		allRangers = this.rangerService.findAll();

		for (final Ranger ranger : allRangers)
			if (ranger.getIsSuspicious() && !ranger.getIsBanned() && ranger.getUserAccount().isEnabled())
				rangerToBan = ranger;

		Assert.notNull(rangerToBan);
		Assert.isTrue(rangerToBan.getIsSuspicious());
		Assert.isTrue(!rangerToBan.getIsBanned());
		Assert.isTrue(rangerToBan.getUserAccount().isEnabled());

		rangerBanned = this.rangerService.ban(rangerToBan.getId());

		Assert.isTrue(rangerBanned.getIsBanned());
		Assert.isTrue(!rangerBanned.getUserAccount().isEnabled());

		this.unauthenticate();

	}

	@Test
	public void testUnban1() {
		this.authenticate("admin");

		Collection<Ranger> allRangers;
		Ranger rangerToBan = null;
		Ranger rangerBanned;

		allRangers = this.rangerService.findAll();

		for (final Ranger ranger : allRangers)
			if (ranger.getIsSuspicious() && !ranger.getIsBanned() && ranger.getUserAccount().isEnabled())
				rangerToBan = ranger;

		Assert.notNull(rangerToBan);
		Assert.isTrue(rangerToBan.getIsSuspicious());
		Assert.isTrue(!rangerToBan.getIsBanned());
		Assert.isTrue(rangerToBan.getUserAccount().isEnabled());

		rangerBanned = this.rangerService.ban(rangerToBan.getId());

		Assert.isTrue(rangerBanned.getIsBanned());
		Assert.isTrue(!rangerBanned.getUserAccount().isEnabled());

		this.unauthenticate();

		this.authenticate("admin");

		final Ranger rangerUnbanned;

		rangerUnbanned = this.rangerService.unban(rangerBanned.getId());

		Assert.isTrue(!rangerUnbanned.getIsBanned());
		Assert.isTrue(rangerUnbanned.getUserAccount().isEnabled());

		this.unauthenticate();

	}
	@Test
	public void testFindByTripId() {
		//Ranger
		this.authenticate("admin");
		final Ranger ranger = this.rangerService.create();
		ranger.setName("New ranger name");
		ranger.setSurname("New ranger surname");
		ranger.setEmail("newranger@ranger.com");
		ranger.setPhone("+34954954954");
		ranger.setAddress("New ranger address");

		UserAccount userAccountToSave;
		userAccountToSave = ranger.getUserAccount();
		userAccountToSave.setUsername("Manuel");
		userAccountToSave.setPassword("Manuel");
		ranger.setUserAccount(userAccountToSave);

		Ranger savedRanger;
		savedRanger = this.rangerService.saveFromCreate(ranger);
		this.unauthenticate();

		//Trip
		this.authenticate("manager1");

		Trip t;
		t = this.tripService.create();
		final String s = "Requirement1";

		t.setTitle("title1");
		t.setDescription("Description1");
		t.setRequirements(s);
		t.setPublicationDate(new DateTime().plusDays(10).toDate());
		t.setStartMoment(new DateTime().plusDays(20).toDate());
		t.setEndMoment(new DateTime().plusDays(30).toDate());
		t.setRanger(savedRanger);
		t.setLegalText(this.legalTextService.findAll().iterator().next());
		t.setCategory(this.categoryService.findAll().iterator().next());
		final Trip updatedTrip = this.tripService.save(t);
		final Trip savedTrip = this.tripService.findTripByTicker(updatedTrip.getTicker());
		this.unauthenticate();
		savedRanger.getTrips().add(savedTrip);
		this.rangerService.saveFromEdit(savedRanger);
		Assert.isTrue(this.rangerService.findByTripId(savedTrip.getId()).equals(savedRanger));

	}

}
