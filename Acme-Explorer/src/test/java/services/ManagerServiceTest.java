
package services;

import java.util.Collection;

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
import domain.Manager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ManagerServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private ManagerService	managerService;

	@Autowired
	private FolderService	folderService;


	// Tests
	@Test
	public void testCreate1() {
		this.authenticate("admin");

		Manager managerToCreate;

		managerToCreate = this.managerService.create();

		Assert.isTrue(!(managerToCreate.getId() > 0));
		Assert.isNull(managerToCreate.getName());
		Assert.isNull(managerToCreate.getSurname());
		Assert.isNull(managerToCreate.getEmail());
		Assert.isNull(managerToCreate.getPhone());
		Assert.isNull(managerToCreate.getAddress());
		Assert.isTrue(managerToCreate.getIsSuspicious() == false);
		Assert.isTrue(managerToCreate.getIsBanned() == false);

		Assert.isTrue(managerToCreate.getSocialIdentities().isEmpty());
		Assert.isTrue(managerToCreate.getFolders().isEmpty());
		Assert.isTrue(managerToCreate.getSent().isEmpty());
		Assert.isTrue(managerToCreate.getReceived().isEmpty());

		Assert.isTrue(managerToCreate.getUserAccount().getAuthorities().iterator().next().getAuthority().equals(Authority.MANAGER));

		this.unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreate2() {

		Manager managerToCreate;

		managerToCreate = this.managerService.create();

		Assert.isTrue(!(managerToCreate.getId() > 0));
		Assert.isNull(managerToCreate.getName());
		Assert.isNull(managerToCreate.getSurname());
		Assert.isNull(managerToCreate.getEmail());
		Assert.isNull(managerToCreate.getPhone());
		Assert.isNull(managerToCreate.getAddress());
		Assert.isTrue(managerToCreate.getIsSuspicious() == false);
		Assert.isTrue(managerToCreate.getIsBanned() == false);

		Assert.isTrue(managerToCreate.getSocialIdentities().isEmpty());
		Assert.isTrue(managerToCreate.getFolders().isEmpty());
		Assert.isTrue(managerToCreate.getSent().isEmpty());
		Assert.isTrue(managerToCreate.getReceived().isEmpty());

		Assert.isTrue(managerToCreate.getUserAccount().getAuthorities().iterator().next().getAuthority().equals(Authority.MANAGER));

	}

	@Test
	public void testSaveFromCreate1() {
		this.authenticate("admin");

		Manager managerToCreate;

		managerToCreate = this.managerService.create();

		Assert.isTrue(!(managerToCreate.getId() > 0));
		Assert.isNull(managerToCreate.getName());
		Assert.isNull(managerToCreate.getSurname());
		Assert.isNull(managerToCreate.getEmail());
		Assert.isNull(managerToCreate.getPhone());
		Assert.isNull(managerToCreate.getAddress());
		Assert.isTrue(managerToCreate.getIsSuspicious() == false);
		Assert.isTrue(managerToCreate.getIsBanned() == false);

		Assert.isTrue(managerToCreate.getSocialIdentities().isEmpty());
		Assert.isTrue(managerToCreate.getFolders().isEmpty());
		Assert.isTrue(managerToCreate.getSent().isEmpty());
		Assert.isTrue(managerToCreate.getReceived().isEmpty());

		Assert.isTrue(managerToCreate.getUserAccount().getAuthorities().iterator().next().getAuthority().equals(Authority.MANAGER));

		this.unauthenticate();

		this.authenticate("admin");

		final Manager managerToSave;
		final UserAccount userAccountToSave;

		managerToCreate.setName("New manager name");
		managerToCreate.setSurname("New manager surname");
		managerToCreate.setEmail("newmanager@manager.com");
		managerToCreate.setPhone("+34954954954");
		managerToCreate.setAddress("New manager address");

		userAccountToSave = managerToCreate.getUserAccount();
		userAccountToSave.setUsername("New manager");
		userAccountToSave.setPassword("New manager");
		managerToCreate.setUserAccount(userAccountToSave);

		managerToSave = this.managerService.saveFromCreate(managerToCreate);

		Manager managerInDB;
		managerInDB = this.managerService.findOne(managerToSave.getId());
		Assert.notNull(managerInDB);

		Assert.isTrue(managerInDB.getName().equals(managerToCreate.getName()));
		Assert.isTrue(managerInDB.getSurname().equals(managerToSave.getSurname()));
		Assert.isTrue(managerInDB.getEmail().equals(managerToCreate.getEmail()));
		Assert.isTrue(managerInDB.getPhone().equals(managerToCreate.getPhone()));
		Assert.isTrue(managerInDB.getAddress().equals(managerToSave.getAddress()));
		Assert.isTrue(managerInDB.getUserAccount().getUsername().equals(managerToCreate.getUserAccount().getUsername()));

		Assert.isTrue(managerInDB.getSent().isEmpty());
		Assert.isTrue(managerInDB.getReceived().isEmpty());
		Assert.isTrue(managerInDB.getFolders().size() == 5);

		for (final Folder f : managerInDB.getFolders())
			Assert.isTrue(f.getSystemFolder());

		this.unauthenticate();

		this.authenticate(managerInDB.getUserAccount().getUsername());

		this.unauthenticate();

	}

	@Test(expected = IllegalArgumentException.class)
	public void testSaveFromCreate2() {
		this.authenticate("admin");

		Manager managerToCreate;

		managerToCreate = this.managerService.create();

		Assert.isTrue(!(managerToCreate.getId() > 0));
		Assert.isNull(managerToCreate.getName());
		Assert.isNull(managerToCreate.getSurname());
		Assert.isNull(managerToCreate.getEmail());
		Assert.isNull(managerToCreate.getPhone());
		Assert.isNull(managerToCreate.getAddress());
		Assert.isTrue(managerToCreate.getIsSuspicious() == false);
		Assert.isTrue(managerToCreate.getIsBanned() == false);

		Assert.isTrue(managerToCreate.getSocialIdentities().isEmpty());
		Assert.isTrue(managerToCreate.getFolders().isEmpty());
		Assert.isTrue(managerToCreate.getSent().isEmpty());
		Assert.isTrue(managerToCreate.getReceived().isEmpty());

		Assert.isTrue(managerToCreate.getUserAccount().getAuthorities().iterator().next().getAuthority().equals(Authority.MANAGER));

		this.unauthenticate();

		final Manager managerToSave;
		final UserAccount userAccountToSave;

		managerToCreate.setName("New manager name");
		managerToCreate.setSurname("New manager surname");
		managerToCreate.setEmail("newmanager@manager.com");
		managerToCreate.setPhone("+34954954954");
		managerToCreate.setAddress("New manager address");

		userAccountToSave = managerToCreate.getUserAccount();
		userAccountToSave.setUsername("New manager");
		userAccountToSave.setPassword("New manager");
		managerToCreate.setUserAccount(userAccountToSave);

		managerToSave = this.managerService.saveFromCreate(managerToCreate);

		Manager managerInDB;
		managerInDB = this.managerService.findOne(managerToSave.getId());
		Assert.notNull(managerInDB);

		Assert.isTrue(managerInDB.getName().equals(managerToCreate.getName()));
		Assert.isTrue(managerInDB.getSurname().equals(managerToSave.getSurname()));
		Assert.isTrue(managerInDB.getEmail().equals(managerToCreate.getEmail()));
		Assert.isTrue(managerInDB.getPhone().equals(managerToCreate.getPhone()));
		Assert.isTrue(managerInDB.getAddress().equals(managerToSave.getAddress()));
		Assert.isTrue(managerInDB.getUserAccount().getUsername().equals(managerToCreate.getUserAccount().getUsername()));

		Assert.isTrue(managerInDB.getSent().isEmpty());
		Assert.isTrue(managerInDB.getReceived().isEmpty());
		Assert.isTrue(managerInDB.getFolders().size() == 5);

		for (final Folder f : managerInDB.getFolders())
			Assert.isTrue(f.getSystemFolder());

		this.authenticate(managerInDB.getUserAccount().getUsername());

		this.unauthenticate();

	}

	@Test
	public void testSaveFromEdit1() {
		this.authenticate("admin");

		Manager managerToCreate;

		managerToCreate = this.managerService.create();

		Assert.isTrue(!(managerToCreate.getId() > 0));
		Assert.isNull(managerToCreate.getName());
		Assert.isNull(managerToCreate.getSurname());
		Assert.isNull(managerToCreate.getEmail());
		Assert.isNull(managerToCreate.getPhone());
		Assert.isNull(managerToCreate.getAddress());
		Assert.isTrue(managerToCreate.getIsSuspicious() == false);
		Assert.isTrue(managerToCreate.getIsBanned() == false);

		Assert.isTrue(managerToCreate.getSocialIdentities().isEmpty());
		Assert.isTrue(managerToCreate.getFolders().isEmpty());
		Assert.isTrue(managerToCreate.getSent().isEmpty());
		Assert.isTrue(managerToCreate.getReceived().isEmpty());

		Assert.isTrue(managerToCreate.getUserAccount().getAuthorities().iterator().next().getAuthority().equals(Authority.MANAGER));

		this.unauthenticate();

		this.authenticate("admin");

		final Manager managerToSave;
		final UserAccount userAccountToSave;

		managerToCreate.setName("New manager name");
		managerToCreate.setSurname("New manager surname");
		managerToCreate.setEmail("newmanager@manager.com");
		managerToCreate.setPhone("+34954954954");
		managerToCreate.setAddress("New manager address");

		userAccountToSave = managerToCreate.getUserAccount();
		userAccountToSave.setUsername("New manager");
		userAccountToSave.setPassword("New manager");
		managerToCreate.setUserAccount(userAccountToSave);

		managerToSave = this.managerService.saveFromCreate(managerToCreate);

		Manager managerInDB;
		managerInDB = this.managerService.findOne(managerToSave.getId());
		Assert.notNull(managerInDB);

		Assert.isTrue(managerInDB.getName().equals(managerToCreate.getName()));
		Assert.isTrue(managerInDB.getSurname().equals(managerToSave.getSurname()));
		Assert.isTrue(managerInDB.getEmail().equals(managerToCreate.getEmail()));
		Assert.isTrue(managerInDB.getPhone().equals(managerToCreate.getPhone()));
		Assert.isTrue(managerInDB.getAddress().equals(managerToSave.getAddress()));
		Assert.isTrue(managerInDB.getUserAccount().getUsername().equals(managerToCreate.getUserAccount().getUsername()));

		Assert.isTrue(managerInDB.getSent().isEmpty());
		Assert.isTrue(managerInDB.getReceived().isEmpty());
		Assert.isTrue(managerInDB.getFolders().size() == 5);

		for (final Folder f : managerInDB.getFolders())
			Assert.isTrue(f.getSystemFolder());

		this.unauthenticate();

		// Logging as the new user.
		this.authenticate(managerInDB.getUserAccount().getUsername());

		Manager principal;
		Manager managerToRetrieve;
		principal = this.managerService.findByPrincipal();
		Assert.notNull(principal);
		Assert.isTrue(principal.equals(managerInDB));

		principal.setName("Updated manager name");
		managerToRetrieve = this.managerService.saveFromEdit(principal);

		principal = null;
		principal = this.managerService.findOne(managerToRetrieve.getId());
		Assert.notNull(principal);

		Assert.isTrue(principal.getName().equals(managerToRetrieve.getName()));
		Assert.isTrue(principal.getFolders().size() == 5);

		for (final Folder f : principal.getFolders())
			Assert.isTrue(f.getSystemFolder());

		this.unauthenticate();

	}

	@Test
	public void testAddFolderToManager1() {
		this.authenticate("admin");

		Manager managerToCreate;

		managerToCreate = this.managerService.create();

		Assert.isTrue(!(managerToCreate.getId() > 0));
		Assert.isNull(managerToCreate.getName());
		Assert.isNull(managerToCreate.getSurname());
		Assert.isNull(managerToCreate.getEmail());
		Assert.isNull(managerToCreate.getPhone());
		Assert.isNull(managerToCreate.getAddress());
		Assert.isTrue(managerToCreate.getIsSuspicious() == false);
		Assert.isTrue(managerToCreate.getIsBanned() == false);

		Assert.isTrue(managerToCreate.getSocialIdentities().isEmpty());
		Assert.isTrue(managerToCreate.getFolders().isEmpty());
		Assert.isTrue(managerToCreate.getSent().isEmpty());
		Assert.isTrue(managerToCreate.getReceived().isEmpty());

		Assert.isTrue(managerToCreate.getUserAccount().getAuthorities().iterator().next().getAuthority().equals(Authority.MANAGER));

		this.unauthenticate();

		this.authenticate("admin");

		final Manager managerToSave;
		final UserAccount userAccountToSave;

		managerToCreate.setName("New manager name");
		managerToCreate.setSurname("New manager surname");
		managerToCreate.setEmail("newmanager@manager.com");
		managerToCreate.setPhone("+34954954954");
		managerToCreate.setAddress("New manager address");

		userAccountToSave = managerToCreate.getUserAccount();
		userAccountToSave.setUsername("New manager");
		userAccountToSave.setPassword("New manager");
		managerToCreate.setUserAccount(userAccountToSave);

		managerToSave = this.managerService.saveFromCreate(managerToCreate);

		Manager managerInDB;
		managerInDB = this.managerService.findOne(managerToSave.getId());
		Assert.notNull(managerInDB);

		Assert.isTrue(managerInDB.getName().equals(managerToCreate.getName()));
		Assert.isTrue(managerInDB.getSurname().equals(managerToSave.getSurname()));
		Assert.isTrue(managerInDB.getEmail().equals(managerToCreate.getEmail()));
		Assert.isTrue(managerInDB.getPhone().equals(managerToCreate.getPhone()));
		Assert.isTrue(managerInDB.getAddress().equals(managerToSave.getAddress()));
		Assert.isTrue(managerInDB.getUserAccount().getUsername().equals(managerToCreate.getUserAccount().getUsername()));

		Assert.isTrue(managerInDB.getSent().isEmpty());
		Assert.isTrue(managerInDB.getReceived().isEmpty());
		Assert.isTrue(managerInDB.getFolders().size() == 5);

		for (final Folder f : managerInDB.getFolders())
			Assert.isTrue(f.getSystemFolder());

		this.unauthenticate();

		// Logging as the new user.
		this.authenticate(managerInDB.getUserAccount().getUsername());

		Manager principal;
		principal = this.managerService.findByPrincipal();
		Assert.notNull(principal);
		Assert.isTrue(principal.equals(managerInDB));
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

		principal = this.managerService.findByPrincipal();
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

		Collection<Manager> allManagers;
		Manager managerToBan = null;
		Manager managerBanned;

		allManagers = this.managerService.findAll();

		for (final Manager manager : allManagers)
			if (manager.getIsSuspicious() && !manager.getIsBanned() && manager.getUserAccount().isEnabled())
				managerToBan = manager;

		Assert.notNull(managerToBan);
		Assert.isTrue(managerToBan.getIsSuspicious());
		Assert.isTrue(!managerToBan.getIsBanned());
		Assert.isTrue(managerToBan.getUserAccount().isEnabled());

		managerBanned = this.managerService.ban(managerToBan.getId());

		final Manager managerInDB = this.managerService.findOne(managerBanned.getId());

		Assert.isTrue(managerInDB.getIsBanned());
		Assert.isTrue(!managerInDB.getUserAccount().isEnabled());

		this.unauthenticate();

	}

	@Test
	public void testUnban1() {
		this.authenticate("admin");

		Collection<Manager> allManagers;
		Manager managerToBan = null;
		Manager managerBanned;

		allManagers = this.managerService.findAll();

		for (final Manager manager : allManagers)
			if (manager.getIsSuspicious() && !manager.getIsBanned() && manager.getUserAccount().isEnabled())
				managerToBan = manager;

		Assert.notNull(managerToBan);
		Assert.isTrue(managerToBan.getIsSuspicious());
		Assert.isTrue(!managerToBan.getIsBanned());
		Assert.isTrue(managerToBan.getUserAccount().isEnabled());

		managerBanned = this.managerService.ban(managerToBan.getId());

		final Manager managerInDB = this.managerService.findOne(managerBanned.getId());

		Assert.isTrue(managerInDB.getIsBanned());
		Assert.isTrue(!managerInDB.getUserAccount().isEnabled());

		this.unauthenticate();

		this.authenticate("admin");

		Manager managerToUnban;
		Manager managerPostUnban;

		managerToUnban = this.managerService.findOne(managerInDB.getId());
		Assert.notNull(managerToUnban);
		Assert.isTrue(managerToUnban.getIsSuspicious());
		Assert.isTrue(managerToUnban.getIsBanned());
		Assert.isTrue(!managerToUnban.getUserAccount().isEnabled());

		managerPostUnban = this.managerService.unban(managerToUnban.getId());

		Assert.isTrue(managerPostUnban.getIsSuspicious());
		Assert.isTrue(!managerPostUnban.getIsBanned());
		Assert.isTrue(managerPostUnban.getUserAccount().isEnabled());

		this.unauthenticate();

	}
}
