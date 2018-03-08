
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
import domain.ContactEmergency;
import domain.Explorer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ContactEmergencyServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private ContactEmergencyService	contactEmergencyService;

	@Autowired
	private ExplorerService			explorerService;


	// Tests
	@Test
	public void testCreate1() {
		this.authenticate("explorer1");

		ContactEmergency contactEmergencyToCreate;

		contactEmergencyToCreate = this.contactEmergencyService.create();
		Assert.isNull(contactEmergencyToCreate.getName());
		Assert.isNull(contactEmergencyToCreate.getEmail());
		Assert.isNull(contactEmergencyToCreate.getPhone());

		this.unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreate2() {

		ContactEmergency contactEmergencyToCreate;

		contactEmergencyToCreate = this.contactEmergencyService.create();
		Assert.isNull(contactEmergencyToCreate.getName());
		Assert.isNull(contactEmergencyToCreate.getEmail());
		Assert.isNull(contactEmergencyToCreate.getPhone());

	}

	@Test
	public void testSaveFromCreate1() {
		this.authenticate("explorer1");

		ContactEmergency contactEmergencyToCreate;

		contactEmergencyToCreate = this.contactEmergencyService.create();
		Assert.isNull(contactEmergencyToCreate.getName());
		Assert.isNull(contactEmergencyToCreate.getEmail());
		Assert.isNull(contactEmergencyToCreate.getPhone());

		this.unauthenticate();

		this.authenticate("explorer1");

		ContactEmergency contactEmergencyToSave;
		Explorer principal;
		Collection<ContactEmergency> explorerContactEmergenciesPostSave;
		Integer numberOfexplorerContactEmergenciesPreSave;
		Integer numberOfexplorerContactEmergenciesPostSave;

		principal = this.explorerService.findByPrincipal();
		numberOfexplorerContactEmergenciesPreSave = principal.getContactEmergencies().size();

		contactEmergencyToCreate.setName("New contact emergency");
		contactEmergencyToCreate.setEmail("newcontactemergency@contactemergency.com");
		contactEmergencyToCreate.setPhone("+34954954954");

		contactEmergencyToSave = this.contactEmergencyService.saveFromCreate(contactEmergencyToCreate);

		Assert.notNull(contactEmergencyToSave);
		Assert.isTrue(contactEmergencyToSave.getName().equals(contactEmergencyToCreate.getName()));
		Assert.isTrue(contactEmergencyToSave.getEmail().equals(contactEmergencyToCreate.getEmail()));
		Assert.isTrue(contactEmergencyToSave.getPhone().equals(contactEmergencyToCreate.getPhone()));

		principal = this.explorerService.findByPrincipal();
		explorerContactEmergenciesPostSave = principal.getContactEmergencies();
		numberOfexplorerContactEmergenciesPostSave = explorerContactEmergenciesPostSave.size();

		Assert.isTrue(explorerContactEmergenciesPostSave.contains(contactEmergencyToSave));
		Assert.isTrue(numberOfexplorerContactEmergenciesPostSave - numberOfexplorerContactEmergenciesPreSave == 1);

		this.unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSaveFromCreate2() {
		this.authenticate("explorer1");

		ContactEmergency contactEmergencyToCreate;

		contactEmergencyToCreate = this.contactEmergencyService.create();
		Assert.isNull(contactEmergencyToCreate.getName());
		Assert.isNull(contactEmergencyToCreate.getEmail());
		Assert.isNull(contactEmergencyToCreate.getPhone());

		this.unauthenticate();

		ContactEmergency contactEmergencyToSave;
		Explorer principal;
		Collection<ContactEmergency> explorerContactEmergenciesPostSave;
		Integer numberOfexplorerContactEmergenciesPreSave;
		Integer numberOfexplorerContactEmergenciesPostSave;

		principal = this.explorerService.findByPrincipal();
		numberOfexplorerContactEmergenciesPreSave = principal.getContactEmergencies().size();

		contactEmergencyToCreate.setName("New contact emergency");
		contactEmergencyToCreate.setEmail("newcontactemergency@contactemergency.com");
		contactEmergencyToCreate.setPhone("+34954954954");

		contactEmergencyToSave = this.contactEmergencyService.saveFromCreate(contactEmergencyToCreate);

		Assert.notNull(contactEmergencyToSave);
		Assert.isTrue(contactEmergencyToSave.getName().equals(contactEmergencyToCreate.getName()));
		Assert.isTrue(contactEmergencyToSave.getEmail().equals(contactEmergencyToCreate.getEmail()));
		Assert.isTrue(contactEmergencyToSave.getPhone().equals(contactEmergencyToCreate.getPhone()));

		principal = this.explorerService.findByPrincipal();
		explorerContactEmergenciesPostSave = principal.getContactEmergencies();
		numberOfexplorerContactEmergenciesPostSave = explorerContactEmergenciesPostSave.size();

		Assert.isTrue(explorerContactEmergenciesPostSave.contains(contactEmergencyToSave));
		Assert.isTrue(numberOfexplorerContactEmergenciesPostSave - numberOfexplorerContactEmergenciesPreSave == 1);

	}

	@Test
	public void testSaveFromEdit1() {
		this.authenticate("explorer1");

		ContactEmergency contactEmergencyToCreate;

		contactEmergencyToCreate = this.contactEmergencyService.create();
		Assert.isNull(contactEmergencyToCreate.getName());
		Assert.isNull(contactEmergencyToCreate.getEmail());
		Assert.isNull(contactEmergencyToCreate.getPhone());

		this.unauthenticate();

		this.authenticate("explorer1");

		ContactEmergency contactEmergencyToSave;
		Explorer principal;
		Collection<ContactEmergency> explorerContactEmergenciesPostSave;
		Integer numberOfexplorerContactEmergenciesPreSave;
		Integer numberOfexplorerContactEmergenciesPostSave;

		principal = this.explorerService.findByPrincipal();
		numberOfexplorerContactEmergenciesPreSave = principal.getContactEmergencies().size();

		contactEmergencyToCreate.setName("New contact emergency");
		contactEmergencyToCreate.setEmail("newcontactemergency@contactemergency.com");
		contactEmergencyToCreate.setPhone("+34954954954");

		contactEmergencyToSave = this.contactEmergencyService.saveFromCreate(contactEmergencyToCreate);

		Assert.notNull(contactEmergencyToSave);
		Assert.isTrue(contactEmergencyToSave.getName().equals(contactEmergencyToCreate.getName()));
		Assert.isTrue(contactEmergencyToSave.getEmail().equals(contactEmergencyToCreate.getEmail()));
		Assert.isTrue(contactEmergencyToSave.getPhone().equals(contactEmergencyToCreate.getPhone()));

		principal = this.explorerService.findByPrincipal();
		explorerContactEmergenciesPostSave = principal.getContactEmergencies();
		numberOfexplorerContactEmergenciesPostSave = explorerContactEmergenciesPostSave.size();

		Assert.isTrue(explorerContactEmergenciesPostSave.contains(contactEmergencyToSave));
		Assert.isTrue(numberOfexplorerContactEmergenciesPostSave - numberOfexplorerContactEmergenciesPreSave == 1);

		this.unauthenticate();

		this.authenticate("explorer1");

		final ContactEmergency contactEmergencyToUpdate;

		final Collection<ContactEmergency> explorerContactEmergenciesPostUpdate;
		Integer numberOfexplorerContactEmergenciesPreUpdate;
		final Integer numberOfexplorerContactEmergenciesPostUpdate;

		principal = this.explorerService.findByPrincipal();
		numberOfexplorerContactEmergenciesPreUpdate = principal.getContactEmergencies().size();

		contactEmergencyToSave.setName("Updated contact emergency");

		contactEmergencyToUpdate = this.contactEmergencyService.saveFromEdit(contactEmergencyToSave);

		Assert.notNull(contactEmergencyToUpdate);
		Assert.isTrue(contactEmergencyToUpdate.getName().equals(contactEmergencyToSave.getName()));
		Assert.isTrue(contactEmergencyToUpdate.getEmail().equals(contactEmergencyToSave.getEmail()));
		Assert.isTrue(contactEmergencyToUpdate.getPhone().equals(contactEmergencyToSave.getPhone()));

		principal = this.explorerService.findByPrincipal();
		explorerContactEmergenciesPostUpdate = principal.getContactEmergencies();
		numberOfexplorerContactEmergenciesPostUpdate = explorerContactEmergenciesPostSave.size();

		Assert.isTrue(explorerContactEmergenciesPostUpdate.contains(contactEmergencyToUpdate));
		Assert.isTrue(!explorerContactEmergenciesPostUpdate.contains(contactEmergencyToCreate));
		Assert.isTrue(numberOfexplorerContactEmergenciesPostUpdate - numberOfexplorerContactEmergenciesPreUpdate == 0);

		this.unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSaveFromEdit2() {
		this.authenticate("explorer1");

		ContactEmergency contactEmergencyToCreate;

		contactEmergencyToCreate = this.contactEmergencyService.create();
		Assert.isNull(contactEmergencyToCreate.getName());
		Assert.isNull(contactEmergencyToCreate.getEmail());
		Assert.isNull(contactEmergencyToCreate.getPhone());

		this.unauthenticate();

		this.authenticate("explorer1");

		ContactEmergency contactEmergencyToSave;
		Explorer principal;
		Collection<ContactEmergency> explorerContactEmergenciesPostSave;
		Integer numberOfexplorerContactEmergenciesPreSave;
		Integer numberOfexplorerContactEmergenciesPostSave;

		principal = this.explorerService.findByPrincipal();
		numberOfexplorerContactEmergenciesPreSave = principal.getContactEmergencies().size();

		contactEmergencyToCreate.setName("New contact emergency");
		contactEmergencyToCreate.setEmail("newcontactemergency@contactemergency.com");
		contactEmergencyToCreate.setPhone("+34954954954");

		contactEmergencyToSave = this.contactEmergencyService.saveFromCreate(contactEmergencyToCreate);

		Assert.notNull(contactEmergencyToSave);
		Assert.isTrue(contactEmergencyToSave.getName().equals(contactEmergencyToCreate.getName()));
		Assert.isTrue(contactEmergencyToSave.getEmail().equals(contactEmergencyToCreate.getEmail()));
		Assert.isTrue(contactEmergencyToSave.getPhone().equals(contactEmergencyToCreate.getPhone()));

		principal = this.explorerService.findByPrincipal();
		explorerContactEmergenciesPostSave = principal.getContactEmergencies();
		numberOfexplorerContactEmergenciesPostSave = explorerContactEmergenciesPostSave.size();

		Assert.isTrue(explorerContactEmergenciesPostSave.contains(contactEmergencyToSave));
		Assert.isTrue(numberOfexplorerContactEmergenciesPostSave - numberOfexplorerContactEmergenciesPreSave == 1);

		this.unauthenticate();

		final ContactEmergency contactEmergencyToUpdate;

		final Collection<ContactEmergency> explorerContactEmergenciesPostUpdate;
		Integer numberOfexplorerContactEmergenciesPreUpdate;
		final Integer numberOfexplorerContactEmergenciesPostUpdate;

		principal = this.explorerService.findByPrincipal();
		numberOfexplorerContactEmergenciesPreUpdate = principal.getContactEmergencies().size();

		contactEmergencyToSave.setName("Updated contact emergency");

		contactEmergencyToUpdate = this.contactEmergencyService.saveFromEdit(contactEmergencyToSave);

		Assert.notNull(contactEmergencyToUpdate);
		Assert.isTrue(contactEmergencyToUpdate.getName().equals(contactEmergencyToSave.getName()));
		Assert.isTrue(contactEmergencyToUpdate.getEmail().equals(contactEmergencyToSave.getEmail()));
		Assert.isTrue(contactEmergencyToUpdate.getPhone().equals(contactEmergencyToSave.getPhone()));

		principal = this.explorerService.findByPrincipal();
		explorerContactEmergenciesPostUpdate = principal.getContactEmergencies();
		numberOfexplorerContactEmergenciesPostUpdate = explorerContactEmergenciesPostSave.size();

		Assert.isTrue(explorerContactEmergenciesPostUpdate.contains(contactEmergencyToUpdate));
		Assert.isTrue(!explorerContactEmergenciesPostUpdate.contains(contactEmergencyToCreate));
		Assert.isTrue(numberOfexplorerContactEmergenciesPostUpdate - numberOfexplorerContactEmergenciesPreUpdate == 0);

	}

	@Test(expected = IllegalArgumentException.class)
	public void testSaveFromEdit3() {
		this.authenticate("explorer1");

		ContactEmergency contactEmergencyToCreate;

		contactEmergencyToCreate = this.contactEmergencyService.create();
		Assert.isNull(contactEmergencyToCreate.getName());
		Assert.isNull(contactEmergencyToCreate.getEmail());
		Assert.isNull(contactEmergencyToCreate.getPhone());

		this.unauthenticate();

		this.authenticate("explorer1");

		ContactEmergency contactEmergencyToSave;
		Explorer principal;
		Collection<ContactEmergency> explorerContactEmergenciesPostSave;
		Integer numberOfexplorerContactEmergenciesPreSave;
		Integer numberOfexplorerContactEmergenciesPostSave;

		principal = this.explorerService.findByPrincipal();
		numberOfexplorerContactEmergenciesPreSave = principal.getContactEmergencies().size();

		contactEmergencyToCreate.setName("New contact emergency");
		contactEmergencyToCreate.setEmail("newcontactemergency@contactemergency.com");
		contactEmergencyToCreate.setPhone("+34954954954");

		contactEmergencyToSave = this.contactEmergencyService.saveFromCreate(contactEmergencyToCreate);

		Assert.notNull(contactEmergencyToSave);
		Assert.isTrue(contactEmergencyToSave.getName().equals(contactEmergencyToCreate.getName()));
		Assert.isTrue(contactEmergencyToSave.getEmail().equals(contactEmergencyToCreate.getEmail()));
		Assert.isTrue(contactEmergencyToSave.getPhone().equals(contactEmergencyToCreate.getPhone()));

		principal = this.explorerService.findByPrincipal();
		explorerContactEmergenciesPostSave = principal.getContactEmergencies();
		numberOfexplorerContactEmergenciesPostSave = explorerContactEmergenciesPostSave.size();

		Assert.isTrue(explorerContactEmergenciesPostSave.contains(contactEmergencyToSave));
		Assert.isTrue(numberOfexplorerContactEmergenciesPostSave - numberOfexplorerContactEmergenciesPreSave == 1);

		this.unauthenticate();

		this.authenticate("explorer2");

		final ContactEmergency contactEmergencyToUpdate;

		final Collection<ContactEmergency> explorerContactEmergenciesPostUpdate;
		Integer numberOfexplorerContactEmergenciesPreUpdate;
		final Integer numberOfexplorerContactEmergenciesPostUpdate;

		principal = this.explorerService.findByPrincipal();
		numberOfexplorerContactEmergenciesPreUpdate = principal.getContactEmergencies().size();

		contactEmergencyToSave.setName("Updated contact emergency");

		contactEmergencyToUpdate = this.contactEmergencyService.saveFromEdit(contactEmergencyToSave);

		Assert.notNull(contactEmergencyToUpdate);
		Assert.isTrue(contactEmergencyToUpdate.getName().equals(contactEmergencyToSave.getName()));
		Assert.isTrue(contactEmergencyToUpdate.getEmail().equals(contactEmergencyToSave.getEmail()));
		Assert.isTrue(contactEmergencyToUpdate.getPhone().equals(contactEmergencyToSave.getPhone()));

		principal = this.explorerService.findByPrincipal();
		explorerContactEmergenciesPostUpdate = principal.getContactEmergencies();
		numberOfexplorerContactEmergenciesPostUpdate = explorerContactEmergenciesPostSave.size();

		Assert.isTrue(explorerContactEmergenciesPostUpdate.contains(contactEmergencyToUpdate));
		Assert.isTrue(!explorerContactEmergenciesPostUpdate.contains(contactEmergencyToCreate));
		Assert.isTrue(numberOfexplorerContactEmergenciesPostUpdate - numberOfexplorerContactEmergenciesPreUpdate == 0);

		this.unauthenticate();
	}

	@Test
	public void testDelete1() {
		this.authenticate("explorer1");

		ContactEmergency contactEmergencyToCreate;

		contactEmergencyToCreate = this.contactEmergencyService.create();
		Assert.isNull(contactEmergencyToCreate.getName());
		Assert.isNull(contactEmergencyToCreate.getEmail());
		Assert.isNull(contactEmergencyToCreate.getPhone());

		this.unauthenticate();

		this.authenticate("explorer1");

		ContactEmergency contactEmergencyToSave;
		Explorer principal;
		Collection<ContactEmergency> explorerContactEmergenciesPostSave;
		Integer numberOfexplorerContactEmergenciesPreSave;
		Integer numberOfexplorerContactEmergenciesPostSave;

		principal = this.explorerService.findByPrincipal();
		numberOfexplorerContactEmergenciesPreSave = principal.getContactEmergencies().size();

		contactEmergencyToCreate.setName("New contact emergency");
		contactEmergencyToCreate.setEmail("newcontactemergency@contactemergency.com");
		contactEmergencyToCreate.setPhone("+34954954954");

		contactEmergencyToSave = this.contactEmergencyService.saveFromCreate(contactEmergencyToCreate);

		Assert.notNull(contactEmergencyToSave);
		Assert.isTrue(contactEmergencyToSave.getName().equals(contactEmergencyToCreate.getName()));
		Assert.isTrue(contactEmergencyToSave.getEmail().equals(contactEmergencyToCreate.getEmail()));
		Assert.isTrue(contactEmergencyToSave.getPhone().equals(contactEmergencyToCreate.getPhone()));

		principal = this.explorerService.findByPrincipal();
		explorerContactEmergenciesPostSave = principal.getContactEmergencies();
		numberOfexplorerContactEmergenciesPostSave = explorerContactEmergenciesPostSave.size();

		Assert.isTrue(explorerContactEmergenciesPostSave.contains(contactEmergencyToSave));
		Assert.isTrue(numberOfexplorerContactEmergenciesPostSave - numberOfexplorerContactEmergenciesPreSave == 1);

		this.unauthenticate();

		this.authenticate("explorer1");

		final ContactEmergency contactEmergencyToUpdate;

		final Collection<ContactEmergency> explorerContactEmergenciesPostUpdate;
		Integer numberOfexplorerContactEmergenciesPreUpdate;
		final Integer numberOfexplorerContactEmergenciesPostUpdate;

		principal = this.explorerService.findByPrincipal();
		numberOfexplorerContactEmergenciesPreUpdate = principal.getContactEmergencies().size();

		contactEmergencyToSave.setName("Updated contact emergency");

		contactEmergencyToUpdate = this.contactEmergencyService.saveFromEdit(contactEmergencyToSave);

		Assert.notNull(contactEmergencyToUpdate);
		Assert.isTrue(contactEmergencyToUpdate.getName().equals(contactEmergencyToSave.getName()));
		Assert.isTrue(contactEmergencyToUpdate.getEmail().equals(contactEmergencyToSave.getEmail()));
		Assert.isTrue(contactEmergencyToUpdate.getPhone().equals(contactEmergencyToSave.getPhone()));

		principal = this.explorerService.findByPrincipal();
		explorerContactEmergenciesPostUpdate = principal.getContactEmergencies();
		numberOfexplorerContactEmergenciesPostUpdate = explorerContactEmergenciesPostSave.size();

		Assert.isTrue(explorerContactEmergenciesPostUpdate.contains(contactEmergencyToUpdate));
		Assert.isTrue(!explorerContactEmergenciesPostUpdate.contains(contactEmergencyToCreate));
		Assert.isTrue(numberOfexplorerContactEmergenciesPostUpdate - numberOfexplorerContactEmergenciesPreUpdate == 0);

		this.unauthenticate();

		this.authenticate("explorer1");

		final Collection<ContactEmergency> explorerContactEmergenciesPostDelete;
		Integer numberOfexplorerContactEmergenciesPreDelete;
		final Integer numberOfexplorerContactEmergenciesPostDelete;

		principal = this.explorerService.findByPrincipal();
		numberOfexplorerContactEmergenciesPreDelete = principal.getContactEmergencies().size();

		this.contactEmergencyService.delete(contactEmergencyToUpdate);

		principal = this.explorerService.findByPrincipal();
		explorerContactEmergenciesPostDelete = principal.getContactEmergencies();
		numberOfexplorerContactEmergenciesPostDelete = explorerContactEmergenciesPostDelete.size();

		Assert.isTrue(!explorerContactEmergenciesPostDelete.contains(contactEmergencyToUpdate));
		Assert.isTrue(numberOfexplorerContactEmergenciesPreDelete - numberOfexplorerContactEmergenciesPostDelete == 1);

		this.unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDelete2() {
		this.authenticate("explorer1");

		ContactEmergency contactEmergencyToCreate;

		contactEmergencyToCreate = this.contactEmergencyService.create();
		Assert.isNull(contactEmergencyToCreate.getName());
		Assert.isNull(contactEmergencyToCreate.getEmail());
		Assert.isNull(contactEmergencyToCreate.getPhone());

		this.unauthenticate();

		this.authenticate("explorer1");

		ContactEmergency contactEmergencyToSave;
		Explorer principal;
		Collection<ContactEmergency> explorerContactEmergenciesPostSave;
		Integer numberOfexplorerContactEmergenciesPreSave;
		Integer numberOfexplorerContactEmergenciesPostSave;

		principal = this.explorerService.findByPrincipal();
		numberOfexplorerContactEmergenciesPreSave = principal.getContactEmergencies().size();

		contactEmergencyToCreate.setName("New contact emergency");
		contactEmergencyToCreate.setEmail("newcontactemergency@contactemergency.com");
		contactEmergencyToCreate.setPhone("+34954954954");

		contactEmergencyToSave = this.contactEmergencyService.saveFromCreate(contactEmergencyToCreate);

		Assert.notNull(contactEmergencyToSave);
		Assert.isTrue(contactEmergencyToSave.getName().equals(contactEmergencyToCreate.getName()));
		Assert.isTrue(contactEmergencyToSave.getEmail().equals(contactEmergencyToCreate.getEmail()));
		Assert.isTrue(contactEmergencyToSave.getPhone().equals(contactEmergencyToCreate.getPhone()));

		principal = this.explorerService.findByPrincipal();
		explorerContactEmergenciesPostSave = principal.getContactEmergencies();
		numberOfexplorerContactEmergenciesPostSave = explorerContactEmergenciesPostSave.size();

		Assert.isTrue(explorerContactEmergenciesPostSave.contains(contactEmergencyToSave));
		Assert.isTrue(numberOfexplorerContactEmergenciesPostSave - numberOfexplorerContactEmergenciesPreSave == 1);

		this.unauthenticate();

		this.authenticate("explorer1");

		final ContactEmergency contactEmergencyToUpdate;

		final Collection<ContactEmergency> explorerContactEmergenciesPostUpdate;
		Integer numberOfexplorerContactEmergenciesPreUpdate;
		final Integer numberOfexplorerContactEmergenciesPostUpdate;

		principal = this.explorerService.findByPrincipal();
		numberOfexplorerContactEmergenciesPreUpdate = principal.getContactEmergencies().size();

		contactEmergencyToSave.setName("Updated contact emergency");

		contactEmergencyToUpdate = this.contactEmergencyService.saveFromEdit(contactEmergencyToSave);

		Assert.notNull(contactEmergencyToUpdate);
		Assert.isTrue(contactEmergencyToUpdate.getName().equals(contactEmergencyToSave.getName()));
		Assert.isTrue(contactEmergencyToUpdate.getEmail().equals(contactEmergencyToSave.getEmail()));
		Assert.isTrue(contactEmergencyToUpdate.getPhone().equals(contactEmergencyToSave.getPhone()));

		principal = this.explorerService.findByPrincipal();
		explorerContactEmergenciesPostUpdate = principal.getContactEmergencies();
		numberOfexplorerContactEmergenciesPostUpdate = explorerContactEmergenciesPostSave.size();

		Assert.isTrue(explorerContactEmergenciesPostUpdate.contains(contactEmergencyToUpdate));
		Assert.isTrue(!explorerContactEmergenciesPostUpdate.contains(contactEmergencyToCreate));
		Assert.isTrue(numberOfexplorerContactEmergenciesPostUpdate - numberOfexplorerContactEmergenciesPreUpdate == 0);

		this.unauthenticate();

		final Collection<ContactEmergency> explorerContactEmergenciesPostDelete;
		Integer numberOfexplorerContactEmergenciesPreDelete;
		final Integer numberOfexplorerContactEmergenciesPostDelete;

		principal = this.explorerService.findByPrincipal();
		numberOfexplorerContactEmergenciesPreDelete = principal.getContactEmergencies().size();

		this.contactEmergencyService.delete(contactEmergencyToUpdate);

		principal = this.explorerService.findByPrincipal();
		explorerContactEmergenciesPostDelete = principal.getContactEmergencies();
		numberOfexplorerContactEmergenciesPostDelete = explorerContactEmergenciesPostDelete.size();

		Assert.isTrue(!explorerContactEmergenciesPostDelete.contains(contactEmergencyToUpdate));
		Assert.isTrue(numberOfexplorerContactEmergenciesPreDelete - numberOfexplorerContactEmergenciesPostDelete == 1);

	}

	@Test(expected = IllegalArgumentException.class)
	public void testDelete3() {
		this.authenticate("explorer1");

		ContactEmergency contactEmergencyToCreate;

		contactEmergencyToCreate = this.contactEmergencyService.create();
		Assert.isNull(contactEmergencyToCreate.getName());
		Assert.isNull(contactEmergencyToCreate.getEmail());
		Assert.isNull(contactEmergencyToCreate.getPhone());

		this.unauthenticate();

		this.authenticate("explorer1");

		ContactEmergency contactEmergencyToSave;
		Explorer principal;
		Collection<ContactEmergency> explorerContactEmergenciesPostSave;
		Integer numberOfexplorerContactEmergenciesPreSave;
		Integer numberOfexplorerContactEmergenciesPostSave;

		principal = this.explorerService.findByPrincipal();
		numberOfexplorerContactEmergenciesPreSave = principal.getContactEmergencies().size();

		contactEmergencyToCreate.setName("New contact emergency");
		contactEmergencyToCreate.setEmail("newcontactemergency@contactemergency.com");
		contactEmergencyToCreate.setPhone("+34954954954");

		contactEmergencyToSave = this.contactEmergencyService.saveFromCreate(contactEmergencyToCreate);

		Assert.notNull(contactEmergencyToSave);
		Assert.isTrue(contactEmergencyToSave.getName().equals(contactEmergencyToCreate.getName()));
		Assert.isTrue(contactEmergencyToSave.getEmail().equals(contactEmergencyToCreate.getEmail()));
		Assert.isTrue(contactEmergencyToSave.getPhone().equals(contactEmergencyToCreate.getPhone()));

		principal = this.explorerService.findByPrincipal();
		explorerContactEmergenciesPostSave = principal.getContactEmergencies();
		numberOfexplorerContactEmergenciesPostSave = explorerContactEmergenciesPostSave.size();

		Assert.isTrue(explorerContactEmergenciesPostSave.contains(contactEmergencyToSave));
		Assert.isTrue(numberOfexplorerContactEmergenciesPostSave - numberOfexplorerContactEmergenciesPreSave == 1);

		this.unauthenticate();

		this.authenticate("explorer1");

		final ContactEmergency contactEmergencyToUpdate;

		final Collection<ContactEmergency> explorerContactEmergenciesPostUpdate;
		Integer numberOfexplorerContactEmergenciesPreUpdate;
		final Integer numberOfexplorerContactEmergenciesPostUpdate;

		principal = this.explorerService.findByPrincipal();
		numberOfexplorerContactEmergenciesPreUpdate = principal.getContactEmergencies().size();

		contactEmergencyToSave.setName("Updated contact emergency");

		contactEmergencyToUpdate = this.contactEmergencyService.saveFromEdit(contactEmergencyToSave);

		Assert.notNull(contactEmergencyToUpdate);
		Assert.isTrue(contactEmergencyToUpdate.getName().equals(contactEmergencyToSave.getName()));
		Assert.isTrue(contactEmergencyToUpdate.getEmail().equals(contactEmergencyToSave.getEmail()));
		Assert.isTrue(contactEmergencyToUpdate.getPhone().equals(contactEmergencyToSave.getPhone()));

		principal = this.explorerService.findByPrincipal();
		explorerContactEmergenciesPostUpdate = principal.getContactEmergencies();
		numberOfexplorerContactEmergenciesPostUpdate = explorerContactEmergenciesPostSave.size();

		Assert.isTrue(explorerContactEmergenciesPostUpdate.contains(contactEmergencyToUpdate));
		Assert.isTrue(!explorerContactEmergenciesPostUpdate.contains(contactEmergencyToCreate));
		Assert.isTrue(numberOfexplorerContactEmergenciesPostUpdate - numberOfexplorerContactEmergenciesPreUpdate == 0);

		this.unauthenticate();

		this.authenticate("explorer2");

		final Collection<ContactEmergency> explorerContactEmergenciesPostDelete;
		Integer numberOfexplorerContactEmergenciesPreDelete;
		final Integer numberOfexplorerContactEmergenciesPostDelete;

		principal = this.explorerService.findByPrincipal();
		numberOfexplorerContactEmergenciesPreDelete = principal.getContactEmergencies().size();

		this.contactEmergencyService.delete(contactEmergencyToUpdate);

		principal = this.explorerService.findByPrincipal();
		explorerContactEmergenciesPostDelete = principal.getContactEmergencies();
		numberOfexplorerContactEmergenciesPostDelete = explorerContactEmergenciesPostDelete.size();

		Assert.isTrue(!explorerContactEmergenciesPostDelete.contains(contactEmergencyToUpdate));
		Assert.isTrue(numberOfexplorerContactEmergenciesPreDelete - numberOfexplorerContactEmergenciesPostDelete == 1);

		this.unauthenticate();
	}
}
