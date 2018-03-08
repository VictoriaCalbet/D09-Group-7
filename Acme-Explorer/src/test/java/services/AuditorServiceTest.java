
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
import domain.Auditor;
import domain.Folder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class AuditorServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private AuditorService	auditorService;

	@Autowired
	private FolderService	folderService;


	// Tests
	@Test
	public void testCreate1() {
		this.authenticate("admin");

		Auditor auditorToCreate;

		auditorToCreate = this.auditorService.create();

		Assert.isTrue(!(auditorToCreate.getId() > 0));
		Assert.isNull(auditorToCreate.getName());
		Assert.isNull(auditorToCreate.getSurname());
		Assert.isNull(auditorToCreate.getEmail());
		Assert.isNull(auditorToCreate.getPhone());
		Assert.isNull(auditorToCreate.getAddress());

		Assert.isTrue(auditorToCreate.getSocialIdentities().isEmpty());
		Assert.isTrue(auditorToCreate.getFolders().isEmpty());
		Assert.isTrue(auditorToCreate.getSent().isEmpty());
		Assert.isTrue(auditorToCreate.getReceived().isEmpty());
		Assert.isTrue(auditorToCreate.getAudits().isEmpty());
		Assert.isTrue(auditorToCreate.getNotes().isEmpty());

		Assert.isTrue(auditorToCreate.getUserAccount().getAuthorities().iterator().next().getAuthority().equals(Authority.AUDITOR));

		this.unauthenticate();
	}

	@Test
	public void testSaveFromCreate1() {
		this.authenticate("admin");

		Auditor auditorToCreate;

		auditorToCreate = this.auditorService.create();

		Assert.isTrue(!(auditorToCreate.getId() > 0));
		Assert.isNull(auditorToCreate.getName());
		Assert.isNull(auditorToCreate.getSurname());
		Assert.isNull(auditorToCreate.getEmail());
		Assert.isNull(auditorToCreate.getPhone());
		Assert.isNull(auditorToCreate.getAddress());

		Assert.isTrue(auditorToCreate.getSocialIdentities().isEmpty());
		Assert.isTrue(auditorToCreate.getFolders().isEmpty());
		Assert.isTrue(auditorToCreate.getSent().isEmpty());
		Assert.isTrue(auditorToCreate.getReceived().isEmpty());
		Assert.isTrue(auditorToCreate.getAudits().isEmpty());
		Assert.isTrue(auditorToCreate.getNotes().isEmpty());

		Assert.isTrue(auditorToCreate.getUserAccount().getAuthorities().iterator().next().getAuthority().equals(Authority.AUDITOR));

		final Auditor auditorToSave;
		final UserAccount userAccountToSave;

		auditorToCreate.setName("New auditor name");
		auditorToCreate.setSurname("New auditor surname");
		auditorToCreate.setEmail("newauditor@auditor.com");
		auditorToCreate.setPhone("+34954954954");
		auditorToCreate.setAddress("New auditor address");

		userAccountToSave = auditorToCreate.getUserAccount();
		userAccountToSave.setUsername("New auditor");
		userAccountToSave.setPassword("New auditor");
		auditorToCreate.setUserAccount(userAccountToSave);

		auditorToSave = this.auditorService.saveFromCreate(auditorToCreate);

		Auditor auditorInDB;
		auditorInDB = this.auditorService.findOne(auditorToSave.getId());
		Assert.notNull(auditorInDB);

		Assert.isTrue(auditorInDB.getName().equals(auditorToCreate.getName()));
		Assert.isTrue(auditorInDB.getSurname().equals(auditorToCreate.getSurname()));
		Assert.isTrue(auditorInDB.getEmail().equals(auditorToCreate.getEmail()));
		Assert.isTrue(auditorInDB.getPhone().equals(auditorToCreate.getPhone()));
		Assert.isTrue(auditorInDB.getAddress().equals(auditorToCreate.getAddress()));
		Assert.isTrue(auditorInDB.getUserAccount().getUsername().equals(auditorToCreate.getUserAccount().getUsername()));
		Assert.isTrue(auditorInDB.getAudits().isEmpty());
		Assert.isTrue(auditorInDB.getNotes().isEmpty());

		Assert.isTrue(auditorInDB.getSent().isEmpty());
		Assert.isTrue(auditorInDB.getReceived().isEmpty());
		Assert.isTrue(auditorInDB.getFolders().size() == 5);

		for (final Folder f : auditorInDB.getFolders())
			Assert.isTrue(f.getSystemFolder());

		this.unauthenticate();

		this.authenticate(auditorInDB.getUserAccount().getUsername());

		this.unauthenticate();
	}

	@Test
	public void testSaveFromEdit1() {
		this.authenticate("admin");

		Auditor auditorToCreate;

		auditorToCreate = this.auditorService.create();

		Assert.isTrue(!(auditorToCreate.getId() > 0));
		Assert.isNull(auditorToCreate.getName());
		Assert.isNull(auditorToCreate.getSurname());
		Assert.isNull(auditorToCreate.getEmail());
		Assert.isNull(auditorToCreate.getPhone());
		Assert.isNull(auditorToCreate.getAddress());

		Assert.isTrue(auditorToCreate.getSocialIdentities().isEmpty());
		Assert.isTrue(auditorToCreate.getFolders().isEmpty());
		Assert.isTrue(auditorToCreate.getSent().isEmpty());
		Assert.isTrue(auditorToCreate.getReceived().isEmpty());
		Assert.isTrue(auditorToCreate.getAudits().isEmpty());
		Assert.isTrue(auditorToCreate.getNotes().isEmpty());

		Assert.isTrue(auditorToCreate.getUserAccount().getAuthorities().iterator().next().getAuthority().equals(Authority.AUDITOR));

		final Auditor auditorToSave;
		final UserAccount userAccountToSave;

		auditorToCreate.setName("New auditor name");
		auditorToCreate.setSurname("New auditor surname");
		auditorToCreate.setEmail("newauditor@auditor.com");
		auditorToCreate.setPhone("+34954954954");
		auditorToCreate.setAddress("New auditor address");

		userAccountToSave = auditorToCreate.getUserAccount();
		userAccountToSave.setUsername("New auditor");
		userAccountToSave.setPassword("New auditor");
		auditorToCreate.setUserAccount(userAccountToSave);

		auditorToSave = this.auditorService.saveFromCreate(auditorToCreate);

		Auditor auditorInDB;
		auditorInDB = this.auditorService.findOne(auditorToSave.getId());
		Assert.notNull(auditorInDB);

		Assert.isTrue(auditorInDB.getName().equals(auditorToCreate.getName()));
		Assert.isTrue(auditorInDB.getSurname().equals(auditorToCreate.getSurname()));
		Assert.isTrue(auditorInDB.getEmail().equals(auditorToCreate.getEmail()));
		Assert.isTrue(auditorInDB.getPhone().equals(auditorToCreate.getPhone()));
		Assert.isTrue(auditorInDB.getAddress().equals(auditorToCreate.getAddress()));
		Assert.isTrue(auditorInDB.getUserAccount().getUsername().equals(auditorToCreate.getUserAccount().getUsername()));
		Assert.isTrue(auditorInDB.getAudits().isEmpty());
		Assert.isTrue(auditorInDB.getNotes().isEmpty());

		Assert.isTrue(auditorInDB.getSent().isEmpty());
		Assert.isTrue(auditorInDB.getReceived().isEmpty());
		Assert.isTrue(auditorInDB.getFolders().size() == 5);

		for (final Folder f : auditorInDB.getFolders())
			Assert.isTrue(f.getSystemFolder());

		this.unauthenticate();

		// Logging as the new user.
		this.authenticate(auditorInDB.getUserAccount().getUsername());

		Auditor principal;
		Auditor auditorToRetrieve;
		principal = this.auditorService.findByPrincipal();
		Assert.notNull(principal);
		Assert.isTrue(principal.equals(auditorInDB));

		principal.setName("Updated auditor name");
		auditorToRetrieve = this.auditorService.saveFromEdit(principal);

		principal = null;
		principal = this.auditorService.findOne(auditorToRetrieve.getId());
		Assert.notNull(principal);

		Assert.isTrue(principal.getName().equals(auditorToRetrieve.getName()));
		Assert.isTrue(principal.getFolders().size() == 5);

		for (final Folder f : principal.getFolders())
			Assert.isTrue(f.getSystemFolder());

		this.unauthenticate();
	}

	@Test
	public void testAddFolderToAuditor1() {
		this.authenticate("admin");

		Auditor auditorToCreate;

		auditorToCreate = this.auditorService.create();

		Assert.isTrue(!(auditorToCreate.getId() > 0));
		Assert.isNull(auditorToCreate.getName());
		Assert.isNull(auditorToCreate.getSurname());
		Assert.isNull(auditorToCreate.getEmail());
		Assert.isNull(auditorToCreate.getPhone());
		Assert.isNull(auditorToCreate.getAddress());

		Assert.isTrue(auditorToCreate.getSocialIdentities().isEmpty());
		Assert.isTrue(auditorToCreate.getFolders().isEmpty());
		Assert.isTrue(auditorToCreate.getSent().isEmpty());
		Assert.isTrue(auditorToCreate.getReceived().isEmpty());
		Assert.isTrue(auditorToCreate.getAudits().isEmpty());
		Assert.isTrue(auditorToCreate.getNotes().isEmpty());

		Assert.isTrue(auditorToCreate.getUserAccount().getAuthorities().iterator().next().getAuthority().equals(Authority.AUDITOR));

		final Auditor auditorToSave;
		final UserAccount userAccountToSave;

		auditorToCreate.setName("New auditor name");
		auditorToCreate.setSurname("New auditor surname");
		auditorToCreate.setEmail("newauditor@auditor.com");
		auditorToCreate.setPhone("+34954954954");
		auditorToCreate.setAddress("New auditor address");

		userAccountToSave = auditorToCreate.getUserAccount();
		userAccountToSave.setUsername("New auditor");
		userAccountToSave.setPassword("New auditor");
		auditorToCreate.setUserAccount(userAccountToSave);

		auditorToSave = this.auditorService.saveFromCreate(auditorToCreate);

		Auditor auditorInDB;
		auditorInDB = this.auditorService.findOne(auditorToSave.getId());
		Assert.notNull(auditorInDB);

		Assert.isTrue(auditorInDB.getName().equals(auditorToCreate.getName()));
		Assert.isTrue(auditorInDB.getSurname().equals(auditorToCreate.getSurname()));
		Assert.isTrue(auditorInDB.getEmail().equals(auditorToCreate.getEmail()));
		Assert.isTrue(auditorInDB.getPhone().equals(auditorToCreate.getPhone()));
		Assert.isTrue(auditorInDB.getAddress().equals(auditorToCreate.getAddress()));
		Assert.isTrue(auditorInDB.getUserAccount().getUsername().equals(auditorToCreate.getUserAccount().getUsername()));
		Assert.isTrue(auditorInDB.getAudits().isEmpty());
		Assert.isTrue(auditorInDB.getNotes().isEmpty());

		Assert.isTrue(auditorInDB.getSent().isEmpty());
		Assert.isTrue(auditorInDB.getReceived().isEmpty());
		Assert.isTrue(auditorInDB.getFolders().size() == 5);

		for (final Folder f : auditorInDB.getFolders())
			Assert.isTrue(f.getSystemFolder());

		this.unauthenticate();

		// Logging as the new user.
		this.authenticate(auditorInDB.getUserAccount().getUsername());

		Auditor principal;
		principal = this.auditorService.findByPrincipal();
		Assert.notNull(principal);
		Assert.isTrue(principal.equals(auditorInDB));
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

		principal = this.auditorService.findByPrincipal();
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
