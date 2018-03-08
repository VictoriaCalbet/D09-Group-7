
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
import domain.CreditCard;
import domain.Explorer;
import domain.Folder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ExplorerServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private ExplorerService	explorerService;

	@Autowired
	private FolderService	folderService;


	// Tests
	@Test
	public void testCreate1() {
		Explorer explorerToCreate;

		explorerToCreate = this.explorerService.create();

		Assert.isTrue(!(explorerToCreate.getId() > 0));
		Assert.isNull(explorerToCreate.getName());
		Assert.isNull(explorerToCreate.getSurname());
		Assert.isNull(explorerToCreate.getEmail());
		Assert.isNull(explorerToCreate.getPhone());
		Assert.isNull(explorerToCreate.getAddress());

		Assert.isTrue(explorerToCreate.getSocialIdentities().isEmpty());
		Assert.isTrue(explorerToCreate.getFolders().isEmpty());
		Assert.isTrue(explorerToCreate.getSent().isEmpty());
		Assert.isTrue(explorerToCreate.getReceived().isEmpty());

		Assert.isTrue(explorerToCreate.getUserAccount().getAuthorities().iterator().next().getAuthority().equals(Authority.EXPLORER));
	}

	@Test
	public void testSaveFromCreate1() {
		Explorer explorerToCreate;

		explorerToCreate = this.explorerService.create();

		Assert.isTrue(!(explorerToCreate.getId() > 0));
		Assert.isNull(explorerToCreate.getName());
		Assert.isNull(explorerToCreate.getSurname());
		Assert.isNull(explorerToCreate.getEmail());
		Assert.isNull(explorerToCreate.getPhone());
		Assert.isNull(explorerToCreate.getAddress());

		Assert.isTrue(explorerToCreate.getSocialIdentities().isEmpty());
		Assert.isTrue(explorerToCreate.getFolders().isEmpty());
		Assert.isTrue(explorerToCreate.getSent().isEmpty());
		Assert.isTrue(explorerToCreate.getReceived().isEmpty());

		Assert.isTrue(explorerToCreate.getUserAccount().getAuthorities().iterator().next().getAuthority().equals(Authority.EXPLORER));

		final Explorer explorerToSave;
		CreditCard creditCardToCreate;
		final UserAccount userAccountToSave;

		explorerToCreate.setName("New explorer name");
		explorerToCreate.setSurname("New explorer surname");
		explorerToCreate.setEmail("newexplorer@explorer.com");
		explorerToCreate.setPhone("+34954954954");
		explorerToCreate.setAddress("New explorer address");

		creditCardToCreate = explorerToCreate.getCreditCard();
		creditCardToCreate.setHolderName("New explorer name");
		creditCardToCreate.setBrandName("VISA");
		creditCardToCreate.setNumber("4485750721419113");
		creditCardToCreate.setExpirationMonth(10);
		creditCardToCreate.setExpirationYear(2020);
		creditCardToCreate.setCvv(673);
		explorerToCreate.setCreditCard(creditCardToCreate);

		userAccountToSave = explorerToCreate.getUserAccount();
		userAccountToSave.setUsername("New explorer");
		userAccountToSave.setPassword("New explorer");
		explorerToCreate.setUserAccount(userAccountToSave);

		explorerToSave = this.explorerService.saveFromCreate(explorerToCreate);

		Explorer explorerInDB;
		explorerInDB = this.explorerService.findOne(explorerToSave.getId());
		Assert.notNull(explorerInDB);

		Assert.isTrue(explorerInDB.getName().equals(explorerToCreate.getName()));
		Assert.isTrue(explorerInDB.getSurname().equals(explorerToCreate.getSurname()));
		Assert.isTrue(explorerInDB.getEmail().equals(explorerToCreate.getEmail()));
		Assert.isTrue(explorerInDB.getPhone().equals(explorerToCreate.getPhone()));
		Assert.isTrue(explorerInDB.getAddress().equals(explorerToCreate.getAddress()));
		Assert.isTrue(explorerInDB.getUserAccount().getUsername().equals(explorerToCreate.getUserAccount().getUsername()));

		Assert.isTrue(explorerInDB.getCreditCard().getHolderName().equals(explorerToCreate.getCreditCard().getHolderName()));
		Assert.isTrue(explorerInDB.getCreditCard().getBrandName().equals(explorerToCreate.getCreditCard().getBrandName()));
		Assert.isTrue(explorerInDB.getCreditCard().getNumber().equals(explorerToCreate.getCreditCard().getNumber()));
		Assert.isTrue(explorerInDB.getCreditCard().getExpirationMonth() == explorerToCreate.getCreditCard().getExpirationMonth());
		Assert.isTrue(explorerInDB.getCreditCard().getExpirationYear() == explorerToCreate.getCreditCard().getExpirationYear());
		Assert.isTrue(explorerInDB.getCreditCard().getCvv() == explorerToCreate.getCreditCard().getCvv());

		Assert.isTrue(explorerInDB.getSent().isEmpty());
		Assert.isTrue(explorerInDB.getReceived().isEmpty());
		Assert.isTrue(explorerInDB.getFolders().size() == 5);

		for (final Folder f : explorerInDB.getFolders())
			Assert.isTrue(f.getSystemFolder());

		this.authenticate(explorerInDB.getUserAccount().getUsername());

		this.unauthenticate();
	}

	@Test
	public void testSaveFromEdit1() {
		Explorer explorerToCreate;

		explorerToCreate = this.explorerService.create();

		Assert.isTrue(!(explorerToCreate.getId() > 0));
		Assert.isNull(explorerToCreate.getName());
		Assert.isNull(explorerToCreate.getSurname());
		Assert.isNull(explorerToCreate.getEmail());
		Assert.isNull(explorerToCreate.getPhone());
		Assert.isNull(explorerToCreate.getAddress());

		Assert.isTrue(explorerToCreate.getSocialIdentities().isEmpty());
		Assert.isTrue(explorerToCreate.getFolders().isEmpty());
		Assert.isTrue(explorerToCreate.getSent().isEmpty());
		Assert.isTrue(explorerToCreate.getReceived().isEmpty());

		Assert.isTrue(explorerToCreate.getUserAccount().getAuthorities().iterator().next().getAuthority().equals(Authority.EXPLORER));

		final Explorer explorerToSave;
		CreditCard creditCardToCreate;
		final UserAccount userAccountToSave;

		explorerToCreate.setName("New explorer name");
		explorerToCreate.setSurname("New explorer surname");
		explorerToCreate.setEmail("newexplorer@explorer.com");
		explorerToCreate.setPhone("+34954954954");
		explorerToCreate.setAddress("New explorer address");

		creditCardToCreate = explorerToCreate.getCreditCard();
		creditCardToCreate.setHolderName("New explorer name");
		creditCardToCreate.setBrandName("VISA");
		creditCardToCreate.setNumber("4485750721419113");
		creditCardToCreate.setExpirationMonth(10);
		creditCardToCreate.setExpirationYear(2020);
		creditCardToCreate.setCvv(673);
		explorerToCreate.setCreditCard(creditCardToCreate);

		userAccountToSave = explorerToCreate.getUserAccount();
		userAccountToSave.setUsername("New explorer");
		userAccountToSave.setPassword("New explorer");
		explorerToCreate.setUserAccount(userAccountToSave);

		explorerToSave = this.explorerService.saveFromCreate(explorerToCreate);

		Explorer explorerInDB;
		explorerInDB = this.explorerService.findOne(explorerToSave.getId());
		Assert.notNull(explorerInDB);

		Assert.isTrue(explorerInDB.getName().equals(explorerToCreate.getName()));
		Assert.isTrue(explorerInDB.getSurname().equals(explorerToCreate.getSurname()));
		Assert.isTrue(explorerInDB.getEmail().equals(explorerToCreate.getEmail()));
		Assert.isTrue(explorerInDB.getPhone().equals(explorerToCreate.getPhone()));
		Assert.isTrue(explorerInDB.getAddress().equals(explorerToCreate.getAddress()));
		Assert.isTrue(explorerInDB.getUserAccount().getUsername().equals(explorerToCreate.getUserAccount().getUsername()));

		Assert.isTrue(explorerInDB.getCreditCard().getHolderName().equals(explorerToCreate.getCreditCard().getHolderName()));
		Assert.isTrue(explorerInDB.getCreditCard().getBrandName().equals(explorerToCreate.getCreditCard().getBrandName()));
		Assert.isTrue(explorerInDB.getCreditCard().getNumber().equals(explorerToCreate.getCreditCard().getNumber()));
		Assert.isTrue(explorerInDB.getCreditCard().getExpirationMonth() == explorerToCreate.getCreditCard().getExpirationMonth());
		Assert.isTrue(explorerInDB.getCreditCard().getExpirationYear() == explorerToCreate.getCreditCard().getExpirationYear());
		Assert.isTrue(explorerInDB.getCreditCard().getCvv() == explorerToCreate.getCreditCard().getCvv());

		Assert.isTrue(explorerInDB.getSent().isEmpty());
		Assert.isTrue(explorerInDB.getReceived().isEmpty());
		Assert.isTrue(explorerInDB.getFolders().size() == 5);

		for (final Folder f : explorerInDB.getFolders())
			Assert.isTrue(f.getSystemFolder());

		// Logging as the new user.
		this.authenticate(explorerInDB.getUserAccount().getUsername());

		Explorer principal;
		Explorer explorerToRetrieve;
		principal = this.explorerService.findByPrincipal();
		Assert.notNull(principal);
		Assert.isTrue(principal.equals(explorerInDB));

		principal.setName("Updated explorer name");
		explorerToRetrieve = this.explorerService.saveFromEdit(principal);

		principal = null;
		principal = this.explorerService.findOne(explorerToRetrieve.getId());
		Assert.notNull(principal);

		Assert.isTrue(principal.getName().equals(explorerToRetrieve.getName()));
		Assert.isTrue(principal.getFolders().size() == 5);

		for (final Folder f : principal.getFolders())
			Assert.isTrue(f.getSystemFolder());

		this.unauthenticate();
	}

	@Test
	public void testAddFolderToExplorer1() {
		Explorer explorerToCreate;

		explorerToCreate = this.explorerService.create();

		Assert.isTrue(!(explorerToCreate.getId() > 0));
		Assert.isNull(explorerToCreate.getName());
		Assert.isNull(explorerToCreate.getSurname());
		Assert.isNull(explorerToCreate.getEmail());
		Assert.isNull(explorerToCreate.getPhone());
		Assert.isNull(explorerToCreate.getAddress());

		Assert.isTrue(explorerToCreate.getSocialIdentities().isEmpty());
		Assert.isTrue(explorerToCreate.getFolders().isEmpty());
		Assert.isTrue(explorerToCreate.getSent().isEmpty());
		Assert.isTrue(explorerToCreate.getReceived().isEmpty());

		Assert.isTrue(explorerToCreate.getUserAccount().getAuthorities().iterator().next().getAuthority().equals(Authority.EXPLORER));

		final Explorer explorerToSave;
		CreditCard creditCardToCreate;
		final UserAccount userAccountToSave;

		explorerToCreate.setName("New explorer name");
		explorerToCreate.setSurname("New explorer surname");
		explorerToCreate.setEmail("newexplorer@explorer.com");
		explorerToCreate.setPhone("+34954954954");
		explorerToCreate.setAddress("New explorer address");

		creditCardToCreate = explorerToCreate.getCreditCard();
		creditCardToCreate.setHolderName("New explorer name");
		creditCardToCreate.setBrandName("VISA");
		creditCardToCreate.setNumber("4485750721419113");
		creditCardToCreate.setExpirationMonth(10);
		creditCardToCreate.setExpirationYear(2020);
		creditCardToCreate.setCvv(673);
		explorerToCreate.setCreditCard(creditCardToCreate);

		userAccountToSave = explorerToCreate.getUserAccount();
		userAccountToSave.setUsername("New explorer");
		userAccountToSave.setPassword("New explorer");
		explorerToCreate.setUserAccount(userAccountToSave);

		explorerToSave = this.explorerService.saveFromCreate(explorerToCreate);

		Explorer explorerInDB;
		explorerInDB = this.explorerService.findOne(explorerToSave.getId());
		Assert.notNull(explorerInDB);

		Assert.isTrue(explorerInDB.getName().equals(explorerToCreate.getName()));
		Assert.isTrue(explorerInDB.getSurname().equals(explorerToCreate.getSurname()));
		Assert.isTrue(explorerInDB.getEmail().equals(explorerToCreate.getEmail()));
		Assert.isTrue(explorerInDB.getPhone().equals(explorerToCreate.getPhone()));
		Assert.isTrue(explorerInDB.getAddress().equals(explorerToCreate.getAddress()));
		Assert.isTrue(explorerInDB.getUserAccount().getUsername().equals(explorerToCreate.getUserAccount().getUsername()));

		Assert.isTrue(explorerInDB.getCreditCard().getHolderName().equals(explorerToCreate.getCreditCard().getHolderName()));
		Assert.isTrue(explorerInDB.getCreditCard().getBrandName().equals(explorerToCreate.getCreditCard().getBrandName()));
		Assert.isTrue(explorerInDB.getCreditCard().getNumber().equals(explorerToCreate.getCreditCard().getNumber()));
		Assert.isTrue(explorerInDB.getCreditCard().getExpirationMonth() == explorerToCreate.getCreditCard().getExpirationMonth());
		Assert.isTrue(explorerInDB.getCreditCard().getExpirationYear() == explorerToCreate.getCreditCard().getExpirationYear());
		Assert.isTrue(explorerInDB.getCreditCard().getCvv() == explorerToCreate.getCreditCard().getCvv());

		Assert.isTrue(explorerInDB.getSent().isEmpty());
		Assert.isTrue(explorerInDB.getReceived().isEmpty());
		Assert.isTrue(explorerInDB.getFolders().size() == 5);

		for (final Folder f : explorerInDB.getFolders())
			Assert.isTrue(f.getSystemFolder());

		// Logging as the new user.
		this.authenticate(explorerInDB.getUserAccount().getUsername());

		Explorer principal;
		principal = this.explorerService.findByPrincipal();
		Assert.notNull(principal);
		Assert.isTrue(principal.equals(explorerInDB));
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

		principal = this.explorerService.findByPrincipal();
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
