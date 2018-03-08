
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
import domain.Administrator;
import domain.Folder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class AdministratorServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private FolderService			folderService;


	// Tests
	@Test
	public void testCreate1() {
		this.authenticate("admin");

		Administrator administratorToCreate;

		administratorToCreate = this.administratorService.create();

		Assert.isTrue(!(administratorToCreate.getId() > 0));
		Assert.isNull(administratorToCreate.getName());
		Assert.isNull(administratorToCreate.getSurname());
		Assert.isNull(administratorToCreate.getEmail());
		Assert.isNull(administratorToCreate.getPhone());
		Assert.isNull(administratorToCreate.getAddress());

		Assert.isTrue(administratorToCreate.getSocialIdentities().isEmpty());
		Assert.isTrue(administratorToCreate.getFolders().isEmpty());
		Assert.isTrue(administratorToCreate.getSent().isEmpty());
		Assert.isTrue(administratorToCreate.getReceived().isEmpty());

		Assert.isTrue(administratorToCreate.getUserAccount().getAuthorities().iterator().next().getAuthority().equals(Authority.ADMIN));

		this.unauthenticate();
	}

	@Test
	public void testSaveFromCreate1() {
		this.authenticate("admin");

		Administrator administratorToCreate;

		administratorToCreate = this.administratorService.create();

		Assert.isTrue(!(administratorToCreate.getId() > 0));
		Assert.isNull(administratorToCreate.getName());
		Assert.isNull(administratorToCreate.getSurname());
		Assert.isNull(administratorToCreate.getEmail());
		Assert.isNull(administratorToCreate.getPhone());
		Assert.isNull(administratorToCreate.getAddress());

		Assert.isTrue(administratorToCreate.getSocialIdentities().isEmpty());
		Assert.isTrue(administratorToCreate.getFolders().isEmpty());
		Assert.isTrue(administratorToCreate.getSent().isEmpty());
		Assert.isTrue(administratorToCreate.getReceived().isEmpty());

		Assert.isTrue(administratorToCreate.getUserAccount().getAuthorities().iterator().next().getAuthority().equals(Authority.ADMIN));

		final Administrator administratorToSave;
		final UserAccount userAccountToSave;

		administratorToCreate.setName("New admin name");
		administratorToCreate.setSurname("New admin surname");
		administratorToCreate.setEmail("newadmin@admin.com");
		administratorToCreate.setPhone("+34954954954");
		administratorToCreate.setAddress("New admin address");

		userAccountToSave = administratorToCreate.getUserAccount();
		userAccountToSave.setUsername("New admin");
		userAccountToSave.setPassword("New admin");
		administratorToCreate.setUserAccount(userAccountToSave);

		administratorToSave = this.administratorService.saveFromCreate(administratorToCreate);

		Administrator administratorInDB;
		administratorInDB = this.administratorService.findOne(administratorToSave.getId());
		Assert.notNull(administratorInDB);

		Assert.isTrue(administratorInDB.getName().equals(administratorToCreate.getName()));
		Assert.isTrue(administratorInDB.getSurname().equals(administratorToCreate.getSurname()));
		Assert.isTrue(administratorInDB.getEmail().equals(administratorToCreate.getEmail()));
		Assert.isTrue(administratorInDB.getPhone().equals(administratorToCreate.getPhone()));
		Assert.isTrue(administratorInDB.getAddress().equals(administratorToCreate.getAddress()));
		Assert.isTrue(administratorInDB.getUserAccount().getUsername().equals(administratorToCreate.getUserAccount().getUsername()));

		Assert.isTrue(administratorInDB.getSent().isEmpty());
		Assert.isTrue(administratorInDB.getReceived().isEmpty());
		Assert.isTrue(administratorInDB.getFolders().size() == 5);

		for (final Folder f : administratorInDB.getFolders())
			Assert.isTrue(f.getSystemFolder());

		this.unauthenticate();

		this.authenticate(administratorInDB.getUserAccount().getUsername());

		this.unauthenticate();
	}

	@Test
	public void testSaveFromEdit1() {
		this.authenticate("admin");

		Administrator administratorToCreate;

		administratorToCreate = this.administratorService.create();

		Assert.isTrue(!(administratorToCreate.getId() > 0));
		Assert.isNull(administratorToCreate.getName());
		Assert.isNull(administratorToCreate.getSurname());
		Assert.isNull(administratorToCreate.getEmail());
		Assert.isNull(administratorToCreate.getPhone());
		Assert.isNull(administratorToCreate.getAddress());

		Assert.isTrue(administratorToCreate.getSocialIdentities().isEmpty());
		Assert.isTrue(administratorToCreate.getFolders().isEmpty());
		Assert.isTrue(administratorToCreate.getSent().isEmpty());
		Assert.isTrue(administratorToCreate.getReceived().isEmpty());

		Assert.isTrue(administratorToCreate.getUserAccount().getAuthorities().iterator().next().getAuthority().equals(Authority.ADMIN));

		final Administrator administratorToSave;
		final UserAccount userAccountToSave;

		administratorToCreate.setName("New admin name");
		administratorToCreate.setSurname("New admin surname");
		administratorToCreate.setEmail("newadmin@admin.com");
		administratorToCreate.setPhone("+34954954954");
		administratorToCreate.setAddress("New admin address");

		userAccountToSave = administratorToCreate.getUserAccount();
		userAccountToSave.setUsername("New admin");
		userAccountToSave.setPassword("New admin");
		administratorToCreate.setUserAccount(userAccountToSave);

		administratorToSave = this.administratorService.saveFromCreate(administratorToCreate);

		Administrator administratorInDB;
		administratorInDB = this.administratorService.findOne(administratorToSave.getId());
		Assert.notNull(administratorInDB);

		Assert.isTrue(administratorInDB.getName().equals(administratorToCreate.getName()));
		Assert.isTrue(administratorInDB.getSurname().equals(administratorToCreate.getSurname()));
		Assert.isTrue(administratorInDB.getEmail().equals(administratorToCreate.getEmail()));
		Assert.isTrue(administratorInDB.getPhone().equals(administratorToCreate.getPhone()));
		Assert.isTrue(administratorInDB.getAddress().equals(administratorToCreate.getAddress()));
		Assert.isTrue(administratorInDB.getUserAccount().getUsername().equals(administratorToCreate.getUserAccount().getUsername()));

		Assert.isTrue(administratorInDB.getSent().isEmpty());
		Assert.isTrue(administratorInDB.getReceived().isEmpty());
		Assert.isTrue(administratorInDB.getFolders().size() == 5);

		for (final Folder f : administratorInDB.getFolders())
			Assert.isTrue(f.getSystemFolder());

		this.unauthenticate();

		// Logging as the new user.
		this.authenticate(administratorInDB.getUserAccount().getUsername());

		Administrator principal;
		Administrator administratorToRetrieve;
		principal = this.administratorService.findByPrincipal();
		Assert.notNull(principal);
		Assert.isTrue(principal.equals(administratorInDB));

		principal.setName("Updated admin name");
		administratorToRetrieve = this.administratorService.saveFromEdit(principal);

		principal = null;
		principal = this.administratorService.findOne(administratorToRetrieve.getId());
		Assert.notNull(principal);

		Assert.isTrue(principal.getName().equals(administratorToRetrieve.getName()));
		Assert.isTrue(principal.getFolders().size() == 5);

		for (final Folder f : principal.getFolders())
			Assert.isTrue(f.getSystemFolder());

		this.unauthenticate();
	}

	@Test
	public void testAddFolderToAdmin1() {
		this.authenticate("admin");

		Administrator administratorToCreate;

		administratorToCreate = this.administratorService.create();

		Assert.isTrue(!(administratorToCreate.getId() > 0));
		Assert.isNull(administratorToCreate.getName());
		Assert.isNull(administratorToCreate.getSurname());
		Assert.isNull(administratorToCreate.getEmail());
		Assert.isNull(administratorToCreate.getPhone());
		Assert.isNull(administratorToCreate.getAddress());

		Assert.isTrue(administratorToCreate.getSocialIdentities().isEmpty());
		Assert.isTrue(administratorToCreate.getFolders().isEmpty());
		Assert.isTrue(administratorToCreate.getSent().isEmpty());
		Assert.isTrue(administratorToCreate.getReceived().isEmpty());

		Assert.isTrue(administratorToCreate.getUserAccount().getAuthorities().iterator().next().getAuthority().equals(Authority.ADMIN));

		final Administrator administratorToSave;
		final UserAccount userAccountToSave;

		administratorToCreate.setName("New admin name");
		administratorToCreate.setSurname("New admin surname");
		administratorToCreate.setEmail("newadmin@admin.com");
		administratorToCreate.setPhone("+34954954954");
		administratorToCreate.setAddress("New admin address");

		userAccountToSave = administratorToCreate.getUserAccount();
		userAccountToSave.setUsername("New admin");
		userAccountToSave.setPassword("New admin");
		administratorToCreate.setUserAccount(userAccountToSave);

		administratorToSave = this.administratorService.saveFromCreate(administratorToCreate);

		Administrator administratorInDB;
		administratorInDB = this.administratorService.findOne(administratorToSave.getId());
		Assert.notNull(administratorInDB);

		Assert.isTrue(administratorInDB.getName().equals(administratorToCreate.getName()));
		Assert.isTrue(administratorInDB.getSurname().equals(administratorToCreate.getSurname()));
		Assert.isTrue(administratorInDB.getEmail().equals(administratorToCreate.getEmail()));
		Assert.isTrue(administratorInDB.getPhone().equals(administratorToCreate.getPhone()));
		Assert.isTrue(administratorInDB.getAddress().equals(administratorToCreate.getAddress()));
		Assert.isTrue(administratorInDB.getUserAccount().getUsername().equals(administratorToCreate.getUserAccount().getUsername()));

		Assert.isTrue(administratorInDB.getSent().isEmpty());
		Assert.isTrue(administratorInDB.getReceived().isEmpty());
		Assert.isTrue(administratorInDB.getFolders().size() == 5);

		for (final Folder f : administratorInDB.getFolders())
			Assert.isTrue(f.getSystemFolder());

		this.unauthenticate();

		// Logging as the new user.
		this.authenticate(administratorInDB.getUserAccount().getUsername());

		Administrator principal;
		principal = this.administratorService.findByPrincipal();
		Assert.notNull(principal);
		Assert.isTrue(principal.equals(administratorInDB));
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

		principal = this.administratorService.findByPrincipal();
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
