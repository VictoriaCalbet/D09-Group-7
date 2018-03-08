
package services;

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
import domain.Sponsor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class SponsorServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private SponsorService	sponsorService;

	@Autowired
	private FolderService	folderService;


	// Tests
	@Test
	public void testCreate1() {
		this.authenticate("admin");

		Sponsor sponsorToCreate;

		sponsorToCreate = this.sponsorService.create();

		Assert.isTrue(!(sponsorToCreate.getId() > 0));
		Assert.isNull(sponsorToCreate.getName());
		Assert.isNull(sponsorToCreate.getSurname());
		Assert.isNull(sponsorToCreate.getEmail());
		Assert.isNull(sponsorToCreate.getPhone());
		Assert.isNull(sponsorToCreate.getAddress());

		Assert.isTrue(sponsorToCreate.getSocialIdentities().isEmpty());
		Assert.isTrue(sponsorToCreate.getFolders().isEmpty());
		Assert.isTrue(sponsorToCreate.getSent().isEmpty());
		Assert.isTrue(sponsorToCreate.getReceived().isEmpty());
		Assert.isTrue(sponsorToCreate.getSponsorships().isEmpty());

		Assert.isTrue(sponsorToCreate.getUserAccount().getAuthorities().iterator().next().getAuthority().equals(Authority.SPONSOR));

		this.unauthenticate();
	}

	@Test
	public void testSaveFromCreate1() {
		this.authenticate("admin");

		Sponsor sponsorToCreate;

		sponsorToCreate = this.sponsorService.create();

		Assert.isTrue(!(sponsorToCreate.getId() > 0));
		Assert.isNull(sponsorToCreate.getName());
		Assert.isNull(sponsorToCreate.getSurname());
		Assert.isNull(sponsorToCreate.getEmail());
		Assert.isNull(sponsorToCreate.getPhone());
		Assert.isNull(sponsorToCreate.getAddress());

		Assert.isTrue(sponsorToCreate.getSocialIdentities().isEmpty());
		Assert.isTrue(sponsorToCreate.getFolders().isEmpty());
		Assert.isTrue(sponsorToCreate.getSent().isEmpty());
		Assert.isTrue(sponsorToCreate.getReceived().isEmpty());
		Assert.isTrue(sponsorToCreate.getSponsorships().isEmpty());

		Assert.isTrue(sponsorToCreate.getUserAccount().getAuthorities().iterator().next().getAuthority().equals(Authority.SPONSOR));

		final Sponsor sponsorToSave;
		final UserAccount userAccountToSave;

		sponsorToCreate.setName("New sponsor name");
		sponsorToCreate.setSurname("New sponsor surname");
		sponsorToCreate.setEmail("newsponsor@sponsor.com");
		sponsorToCreate.setPhone("+34954954954");
		sponsorToCreate.setAddress("New sponsor address");

		userAccountToSave = sponsorToCreate.getUserAccount();
		userAccountToSave.setUsername("New sponsor");
		userAccountToSave.setPassword("New sponsor");
		sponsorToCreate.setUserAccount(userAccountToSave);

		sponsorToSave = this.sponsorService.saveFromCreate(sponsorToCreate);

		Sponsor sponsorInDB;
		sponsorInDB = this.sponsorService.findOne(sponsorToSave.getId());
		Assert.notNull(sponsorInDB);

		Assert.isTrue(sponsorInDB.getName().equals(sponsorToCreate.getName()));
		Assert.isTrue(sponsorInDB.getSurname().equals(sponsorToCreate.getSurname()));
		Assert.isTrue(sponsorInDB.getEmail().equals(sponsorToCreate.getEmail()));
		Assert.isTrue(sponsorInDB.getPhone().equals(sponsorToCreate.getPhone()));
		Assert.isTrue(sponsorInDB.getAddress().equals(sponsorToCreate.getAddress()));
		Assert.isTrue(sponsorInDB.getUserAccount().getUsername().equals(sponsorToCreate.getUserAccount().getUsername()));
		Assert.isTrue(sponsorInDB.getSponsorships().isEmpty());

		Assert.isTrue(sponsorInDB.getSent().isEmpty());
		Assert.isTrue(sponsorInDB.getReceived().isEmpty());
		Assert.isTrue(sponsorInDB.getFolders().size() == 5);

		for (final Folder f : sponsorInDB.getFolders())
			Assert.isTrue(f.getSystemFolder());

		this.unauthenticate();

		this.authenticate(sponsorInDB.getUserAccount().getUsername());

		this.unauthenticate();
	}

	@Test
	public void testSaveFromEdit1() {
		this.authenticate("admin");

		Sponsor sponsorToCreate;

		sponsorToCreate = this.sponsorService.create();

		Assert.isTrue(!(sponsorToCreate.getId() > 0));
		Assert.isNull(sponsorToCreate.getName());
		Assert.isNull(sponsorToCreate.getSurname());
		Assert.isNull(sponsorToCreate.getEmail());
		Assert.isNull(sponsorToCreate.getPhone());
		Assert.isNull(sponsorToCreate.getAddress());

		Assert.isTrue(sponsorToCreate.getSocialIdentities().isEmpty());
		Assert.isTrue(sponsorToCreate.getFolders().isEmpty());
		Assert.isTrue(sponsorToCreate.getSent().isEmpty());
		Assert.isTrue(sponsorToCreate.getReceived().isEmpty());
		Assert.isTrue(sponsorToCreate.getSponsorships().isEmpty());

		Assert.isTrue(sponsorToCreate.getUserAccount().getAuthorities().iterator().next().getAuthority().equals(Authority.SPONSOR));

		final Sponsor sponsorToSave;
		final UserAccount userAccountToSave;

		sponsorToCreate.setName("New sponsor name");
		sponsorToCreate.setSurname("New sponsor surname");
		sponsorToCreate.setEmail("newsponsor@sponsor.com");
		sponsorToCreate.setPhone("+34954954954");
		sponsorToCreate.setAddress("New sponsor address");

		userAccountToSave = sponsorToCreate.getUserAccount();
		userAccountToSave.setUsername("New sponsor");
		userAccountToSave.setPassword("New sponsor");
		sponsorToCreate.setUserAccount(userAccountToSave);

		sponsorToSave = this.sponsorService.saveFromCreate(sponsorToCreate);

		Sponsor sponsorInDB;
		sponsorInDB = this.sponsorService.findOne(sponsorToSave.getId());
		Assert.notNull(sponsorInDB);

		Assert.isTrue(sponsorInDB.getName().equals(sponsorToCreate.getName()));
		Assert.isTrue(sponsorInDB.getSurname().equals(sponsorToCreate.getSurname()));
		Assert.isTrue(sponsorInDB.getEmail().equals(sponsorToCreate.getEmail()));
		Assert.isTrue(sponsorInDB.getPhone().equals(sponsorToCreate.getPhone()));
		Assert.isTrue(sponsorInDB.getAddress().equals(sponsorToCreate.getAddress()));
		Assert.isTrue(sponsorInDB.getUserAccount().getUsername().equals(sponsorToCreate.getUserAccount().getUsername()));
		Assert.isTrue(sponsorInDB.getSponsorships().isEmpty());

		Assert.isTrue(sponsorInDB.getSent().isEmpty());
		Assert.isTrue(sponsorInDB.getReceived().isEmpty());
		Assert.isTrue(sponsorInDB.getFolders().size() == 5);

		for (final Folder f : sponsorInDB.getFolders())
			Assert.isTrue(f.getSystemFolder());

		this.unauthenticate();

		// Logging as the new user.
		this.authenticate(sponsorInDB.getUserAccount().getUsername());

		Sponsor principal;
		Sponsor sponsorToRetrieve;
		principal = this.sponsorService.findByPrincipal();
		Assert.notNull(principal);
		Assert.isTrue(principal.equals(sponsorInDB));

		principal.setName("Updated sponsor name");
		sponsorToRetrieve = this.sponsorService.saveFromEdit(principal);

		principal = null;
		principal = this.sponsorService.findOne(sponsorToRetrieve.getId());
		Assert.notNull(principal);

		Assert.isTrue(principal.getName().equals(sponsorToRetrieve.getName()));
		Assert.isTrue(principal.getFolders().size() == 5);

		for (final Folder f : principal.getFolders())
			Assert.isTrue(f.getSystemFolder());

		this.unauthenticate();
	}

	@Test
	public void testAddFolderToSponsor1() {
		this.authenticate("admin");

		Sponsor sponsorToCreate;

		sponsorToCreate = this.sponsorService.create();

		Assert.isTrue(!(sponsorToCreate.getId() > 0));
		Assert.isNull(sponsorToCreate.getName());
		Assert.isNull(sponsorToCreate.getSurname());
		Assert.isNull(sponsorToCreate.getEmail());
		Assert.isNull(sponsorToCreate.getPhone());
		Assert.isNull(sponsorToCreate.getAddress());

		Assert.isTrue(sponsorToCreate.getSocialIdentities().isEmpty());
		Assert.isTrue(sponsorToCreate.getFolders().isEmpty());
		Assert.isTrue(sponsorToCreate.getSent().isEmpty());
		Assert.isTrue(sponsorToCreate.getReceived().isEmpty());
		Assert.isTrue(sponsorToCreate.getSponsorships().isEmpty());

		Assert.isTrue(sponsorToCreate.getUserAccount().getAuthorities().iterator().next().getAuthority().equals(Authority.SPONSOR));

		final Sponsor sponsorToSave;
		final UserAccount userAccountToSave;

		sponsorToCreate.setName("New sponsor name");
		sponsorToCreate.setSurname("New sponsor surname");
		sponsorToCreate.setEmail("newsponsor@sponsor.com");
		sponsorToCreate.setPhone("+34954954954");
		sponsorToCreate.setAddress("New sponsor address");

		userAccountToSave = sponsorToCreate.getUserAccount();
		userAccountToSave.setUsername("New sponsor");
		userAccountToSave.setPassword("New sponsor");
		sponsorToCreate.setUserAccount(userAccountToSave);

		sponsorToSave = this.sponsorService.saveFromCreate(sponsorToCreate);

		Sponsor sponsorInDB;
		sponsorInDB = this.sponsorService.findOne(sponsorToSave.getId());
		Assert.notNull(sponsorInDB);

		Assert.isTrue(sponsorInDB.getName().equals(sponsorToCreate.getName()));
		Assert.isTrue(sponsorInDB.getSurname().equals(sponsorToCreate.getSurname()));
		Assert.isTrue(sponsorInDB.getEmail().equals(sponsorToCreate.getEmail()));
		Assert.isTrue(sponsorInDB.getPhone().equals(sponsorToCreate.getPhone()));
		Assert.isTrue(sponsorInDB.getAddress().equals(sponsorToCreate.getAddress()));
		Assert.isTrue(sponsorInDB.getUserAccount().getUsername().equals(sponsorToCreate.getUserAccount().getUsername()));
		Assert.isTrue(sponsorInDB.getSponsorships().isEmpty());

		Assert.isTrue(sponsorInDB.getSent().isEmpty());
		Assert.isTrue(sponsorInDB.getReceived().isEmpty());
		Assert.isTrue(sponsorInDB.getFolders().size() == 5);

		for (final Folder f : sponsorInDB.getFolders())
			Assert.isTrue(f.getSystemFolder());

		this.unauthenticate();

		// Logging as the new user.
		this.authenticate(sponsorInDB.getUserAccount().getUsername());

		Sponsor principal;
		principal = this.sponsorService.findByPrincipal();
		Assert.notNull(principal);
		Assert.isTrue(principal.equals(sponsorInDB));
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

		principal = this.sponsorService.findByPrincipal();
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

}
