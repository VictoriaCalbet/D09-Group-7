
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Actor;
import domain.Folder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional()
public class FolderServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private FolderService	folderService;

	// Supporting services
	@Autowired
	private ActorService	actorService;


	// Tests

	@Test
	/**
	 * Positive test: Correct entity creation
	 */
	public void testCreate1() {
		this.authenticate("sponsor1");

		final Folder folderToCreate;

		folderToCreate = this.folderService.create();

		Assert.isTrue(!(folderToCreate.getId() > 0));
		Assert.isNull(folderToCreate.getName());
		Assert.isNull(folderToCreate.getParent());

		this.unauthenticate();
	}

	@Test
	/**
	 * Positive test: Correct entity creation and saving.
	 */
	public void testSaveFromCreate1() {
		this.authenticate("sponsor1");

		final Folder folderToCreate;

		folderToCreate = this.folderService.create();

		Assert.isTrue(!(folderToCreate.getId() > 0));
		Assert.isNull(folderToCreate.getName());
		Assert.isTrue(!folderToCreate.getSystemFolder());
		Assert.isNull(folderToCreate.getParent());

		this.unauthenticate();

		this.authenticate("sponsor1");

		Actor principal;
		final Folder folderToSave;
		final Folder folderToRetrieve;

		folderToCreate.setName("Folder created");
		folderToSave = this.folderService.saveFromCreate(folderToCreate);

		Assert.isTrue(folderToSave.getId() > 0);
		Assert.isTrue(folderToSave.getName().equals(folderToCreate.getName()));
		Assert.isTrue(!folderToSave.getSystemFolder());
		Assert.isNull(folderToSave.getParent());

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		folderToRetrieve = this.folderService.findFolderByOwnerAndName(principal.getId(), folderToSave.getName());
		Assert.notNull(folderToRetrieve);

		this.unauthenticate();

	}

	@Test(expected = IllegalArgumentException.class)
	/**
	 * Positive test: Correct entity creation but null name when saving.
	 */
	public void testSaveFromCreate2() {
		this.authenticate("sponsor1");

		final Folder folderToCreate;

		folderToCreate = this.folderService.create();

		Assert.isTrue(!(folderToCreate.getId() > 0));
		Assert.isNull(folderToCreate.getName());
		Assert.isTrue(!folderToCreate.getSystemFolder());
		Assert.isNull(folderToCreate.getParent());

		this.unauthenticate();

		this.authenticate("sponsor1");

		Actor principal;
		final Folder folderToSave;
		final Folder folderToRetrieve;

		// folderToCreate.setName("Folder created");
		folderToSave = this.folderService.saveFromCreate(folderToCreate);

		Assert.isTrue(folderToSave.getId() > 0);
		Assert.isTrue(folderToSave.getName().equals(folderToCreate.getName()));
		Assert.isTrue(!folderToSave.getSystemFolder());
		Assert.isNull(folderToSave.getParent());

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		folderToRetrieve = this.folderService.findFolderByOwnerAndName(principal.getId(), folderToSave.getName());
		Assert.notNull(folderToRetrieve);

		this.unauthenticate();

	}

	@Test
	/**
	 * Positive test: Correct entity retrieve from Actor and saving.
	 */
	public void testSaveFromEdit1() {
		this.authenticate("sponsor1");

		final Folder folderToCreate;

		folderToCreate = this.folderService.create();

		Assert.isTrue(!(folderToCreate.getId() > 0));
		Assert.isNull(folderToCreate.getName());
		Assert.isTrue(!folderToCreate.getSystemFolder());
		Assert.isNull(folderToCreate.getParent());

		this.unauthenticate();

		this.authenticate("sponsor1");

		Actor principal;
		final Folder folderToSave;
		Folder folderToRetrieve;

		folderToCreate.setName("Folder created");
		folderToSave = this.folderService.saveFromCreate(folderToCreate);

		Assert.isTrue(folderToSave.getId() > 0);
		Assert.isTrue(folderToSave.getName().equals(folderToCreate.getName()));
		Assert.isTrue(!folderToSave.getSystemFolder());
		Assert.isNull(folderToSave.getParent());

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		folderToRetrieve = this.folderService.findFolderByOwnerAndName(principal.getId(), folderToSave.getName());
		Assert.notNull(folderToRetrieve);

		this.unauthenticate();

		this.authenticate("sponsor1");

		final Folder folderPreEdit;
		final Folder folderPostEdit;

		principal = this.actorService.findByPrincipal();
		folderPreEdit = this.folderService.findFolderByOwnerAndName(principal.getId(), folderToSave.getName());
		Assert.notNull(folderPreEdit);

		folderPreEdit.setName("Updated name");
		folderPostEdit = this.folderService.saveFromEdit(folderPreEdit);

		Assert.isTrue(folderPostEdit.getId() > 0);
		Assert.isTrue(folderPostEdit.getName().equals(folderPreEdit.getName()));
		Assert.isTrue(!folderPostEdit.getSystemFolder());
		Assert.isNull(folderPostEdit.getParent());

		folderToRetrieve = null;
		folderToRetrieve = this.folderService.findFolderByOwnerAndName(principal.getId(), folderPostEdit.getName());
		Assert.notNull(folderToRetrieve);

		this.unauthenticate();
	}

	@Test
	/**
	 * Positive test: Correct entity retrieve from Actor and saving.
	 */
	public void testDelete1() {
		this.authenticate("sponsor1");

		final Folder folderToCreate;

		folderToCreate = this.folderService.create();

		Assert.isTrue(!(folderToCreate.getId() > 0));
		Assert.isNull(folderToCreate.getName());
		Assert.isTrue(!folderToCreate.getSystemFolder());
		Assert.isNull(folderToCreate.getParent());

		this.unauthenticate();

		this.authenticate("sponsor1");

		Actor principal;
		final Folder folderToSave;
		Folder folderToRetrieve;

		folderToCreate.setName("Folder created");
		folderToSave = this.folderService.saveFromCreate(folderToCreate);

		Assert.isTrue(folderToSave.getId() > 0);
		Assert.isTrue(folderToSave.getName().equals(folderToCreate.getName()));
		Assert.isTrue(!folderToSave.getSystemFolder());
		Assert.isNull(folderToSave.getParent());

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		folderToRetrieve = this.folderService.findFolderByOwnerAndName(principal.getId(), folderToSave.getName());
		Assert.notNull(folderToRetrieve);

		this.unauthenticate();

		this.authenticate("sponsor1");

		final Folder folderPreDelete;
		final Folder folderPostDelete;

		principal = this.actorService.findByPrincipal();
		folderPreDelete = this.folderService.findFolderByOwnerAndName(principal.getId(), folderToSave.getName());
		Assert.notNull(folderPreDelete);

		this.folderService.delete(folderPreDelete);

		folderPostDelete = this.folderService.findOne(folderPreDelete.getId());
		Assert.isNull(folderPostDelete);

		this.unauthenticate();
	}

	// Query tests

	@Test
	/**
	 * Positive test: Proper actor and folder.
	 */
	public void testFindFolderByOwnerAndName1() {
		this.authenticate("sponsor1");

		Actor principal;
		Folder folderToFind;
		Folder folderRetrived;

		principal = this.actorService.findByPrincipal();
		folderToFind = principal.getFolders().iterator().next();
		Assert.notNull(folderToFind);
		folderRetrived = this.folderService.findFolderByOwnerAndName(principal.getId(), folderToFind.getName());

		Assert.isTrue(folderToFind.equals(folderRetrived));

		this.unauthenticate();
	}

	@Test
	/**
	 * Negative test: Proper actor but incorrect folder.
	 */
	public void testFindFolderByOwnerAndName2() {
		this.authenticate("sponsor1");

		Actor principal;
		Folder folderRetrived;

		principal = this.actorService.findByPrincipal();
		folderRetrived = this.folderService.findFolderByOwnerAndName(principal.getId(), "Folder name not present");

		Assert.isNull(folderRetrived);

		this.unauthenticate();
	}

	@Test
	/**
	 * Negative test: Proper folder but wrong actorId.
	 */
	public void testFindFolderByOwnerAndName3() {
		this.authenticate("sponsor1");

		Actor principal;
		Folder folderToFind;
		Folder folderRetrived;

		principal = this.actorService.findByPrincipal();
		folderToFind = principal.getFolders().iterator().next();
		Assert.notNull(folderToFind);
		folderRetrived = this.folderService.findFolderByOwnerAndName(Integer.MIN_VALUE, folderToFind.getName());

		Assert.isNull(folderRetrived);

		this.unauthenticate();
	}

	@Test
	/**
	 * Positive test: Proper folder.
	 */
	public void testFindFolderOwner1() {
		this.authenticate("sponsor1");

		Actor principal;
		Actor owner;
		Folder principalRandomFolder;

		principal = this.actorService.findByPrincipal();
		principalRandomFolder = principal.getFolders().iterator().next();
		Assert.notNull(principalRandomFolder);
		owner = this.folderService.findFolderOwner(principalRandomFolder.getId());

		Assert.isTrue(principal.equals(owner));

		this.unauthenticate();
	}

	@Test
	/**
	 * Negative test: Wrong folderId.
	 */
	public void testFindFolderOwner2() {
		this.authenticate("sponsor1");

		Actor owner;

		owner = this.folderService.findFolderOwner(Integer.MIN_VALUE);

		Assert.isNull(owner);

		this.unauthenticate();
	}

}
