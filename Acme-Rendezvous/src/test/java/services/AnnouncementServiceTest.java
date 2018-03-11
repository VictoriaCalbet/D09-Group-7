/*
 * AdministratorServiceTest.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Announcement;
import domain.Rendezvous;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class AnnouncementServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private AnnouncementService	announcementService;

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private UserService			userService;


	// Tests ------------------------------------------------------------------

	@Test
	public void testCreate1() {
		this.authenticate("user2");

		Announcement announcementFromCreate = null;

		announcementFromCreate = this.announcementService.create();

		Assert.notNull(announcementFromCreate);
		Assert.notNull(announcementFromCreate.getMomentMade());
		Assert.isNull(announcementFromCreate.getTitle());
		Assert.isNull(announcementFromCreate.getDescription());
		Assert.isNull(announcementFromCreate.getRendezvous());

		this.unauthenticate();
	}

	@Test
	public void testSaveFromCreate() {
		this.authenticate("user2");

		Announcement announcementFromCreate = null;
		Rendezvous rendezvous = null;
		Rendezvous rendezvousAfterSaveFromCreate = null;
		User user = null;
		Announcement announcementSaveFromCreate = null;

		announcementFromCreate = this.announcementService.create();

		Assert.notNull(announcementFromCreate);
		Assert.notNull(announcementFromCreate.getMomentMade());
		Assert.isNull(announcementFromCreate.getTitle());
		Assert.isNull(announcementFromCreate.getDescription());
		Assert.isNull(announcementFromCreate.getRendezvous());

		// Esto sería los elementos que añadimos en el formulario
		announcementFromCreate.setTitle("Title test");
		announcementFromCreate.setDescription("Description test");

		user = this.userService.findByPrincipal();
		rendezvous = this.rendezvousService.findAllAvailableRendezvousesCreatedByUserId(user.getId()).iterator().next();
		announcementFromCreate.setRendezvous(rendezvous);

		announcementSaveFromCreate = this.announcementService.saveFromCreate(announcementFromCreate);

		Assert.notNull(announcementSaveFromCreate);
		Assert.isTrue(announcementSaveFromCreate.getTitle().equals(announcementFromCreate.getTitle()));
		Assert.isTrue(announcementSaveFromCreate.getDescription().equals(announcementFromCreate.getDescription()));
		Assert.notNull(announcementSaveFromCreate.getMomentMade());	// El momentMade es distinto en announcementFromCreate

		rendezvousAfterSaveFromCreate = this.rendezvousService.findOne(rendezvous.getId());
		Assert.isTrue(rendezvousAfterSaveFromCreate.getAnnouncements().contains(announcementSaveFromCreate));

		this.unauthenticate();
	}

	@Test
	public void testSaveFromEdit() {
		this.authenticate("user2");

		Announcement announcementFromCreate = null;
		Rendezvous rendezvous = null;
		Rendezvous rendezvousAfterSaveFromCreate = null;
		User user = null;
		Announcement announcementSaveFromCreate = null;

		announcementFromCreate = this.announcementService.create();

		Assert.notNull(announcementFromCreate);
		Assert.notNull(announcementFromCreate.getMomentMade());
		Assert.isNull(announcementFromCreate.getTitle());
		Assert.isNull(announcementFromCreate.getDescription());
		Assert.isNull(announcementFromCreate.getRendezvous());

		// Esto sería los elementos que añadimos en el formulario

		announcementFromCreate.setTitle("Title test");
		announcementFromCreate.setDescription("Description test");

		user = this.userService.findByPrincipal();
		rendezvous = this.rendezvousService.findAllAvailableRendezvousesCreatedByUserId(user.getId()).iterator().next();
		announcementFromCreate.setRendezvous(rendezvous);

		announcementSaveFromCreate = this.announcementService.saveFromCreate(announcementFromCreate);

		Assert.notNull(announcementSaveFromCreate);
		Assert.isTrue(announcementSaveFromCreate.getTitle().equals(announcementFromCreate.getTitle()));
		Assert.isTrue(announcementSaveFromCreate.getDescription().equals(announcementFromCreate.getDescription()));
		Assert.notNull(announcementSaveFromCreate.getMomentMade());	// El momentMade es distinto en announcementFromCreate

		rendezvousAfterSaveFromCreate = this.rendezvousService.findOne(rendezvous.getId());
		Assert.isTrue(rendezvousAfterSaveFromCreate.getAnnouncements().contains(announcementSaveFromCreate));

		// A partir de aquí, hacemos las comprobaciones al editar los datos

		Announcement announcementSaveFromEdit = null;

		announcementSaveFromCreate.setTitle("Title modified");
		announcementSaveFromCreate.setDescription("Description modified");

		announcementSaveFromEdit = this.announcementService.saveFromEdit(announcementSaveFromCreate);

		Assert.notNull(announcementSaveFromEdit);
		Assert.isTrue(announcementSaveFromEdit.getTitle().equals(announcementSaveFromCreate.getTitle()));
		Assert.isTrue(announcementSaveFromEdit.getDescription().equals(announcementSaveFromCreate.getDescription()));
		Assert.notNull(announcementSaveFromEdit.getMomentMade());	// El momentMade es distinto en announcementFromCreate

		this.unauthenticate();
	}

	@Test
	public void testDelete() {
		this.authenticate("user2");

		Announcement announcementFromCreate = null;
		Rendezvous rendezvous = null;
		Rendezvous rendezvousAfterSaveFromCreate = null;
		User user = null;
		Announcement announcementSaveFromCreate = null;

		announcementFromCreate = this.announcementService.create();

		Assert.notNull(announcementFromCreate);
		Assert.notNull(announcementFromCreate.getMomentMade());
		Assert.isNull(announcementFromCreate.getTitle());
		Assert.isNull(announcementFromCreate.getDescription());
		Assert.isNull(announcementFromCreate.getRendezvous());

		// Esto sería los elementos que añadimos en el formulario
		announcementFromCreate.setTitle("Title test");
		announcementFromCreate.setDescription("Description test");

		user = this.userService.findByPrincipal();
		rendezvous = this.rendezvousService.findAllAvailableRendezvousesCreatedByUserId(user.getId()).iterator().next();
		announcementFromCreate.setRendezvous(rendezvous);

		announcementSaveFromCreate = this.announcementService.saveFromCreate(announcementFromCreate);

		Assert.notNull(announcementSaveFromCreate);
		Assert.isTrue(announcementSaveFromCreate.getTitle().equals(announcementFromCreate.getTitle()));
		Assert.isTrue(announcementSaveFromCreate.getDescription().equals(announcementFromCreate.getDescription()));
		Assert.notNull(announcementSaveFromCreate.getMomentMade());	// El momentMade es distinto en announcementFromCreate

		rendezvousAfterSaveFromCreate = this.rendezvousService.findOne(rendezvous.getId());

		Assert.isTrue(rendezvousAfterSaveFromCreate.getAnnouncements().contains(announcementSaveFromCreate));

		this.unauthenticate();

		// A partir de aquí, hacemos las comprobaciones al borrar los datos

		this.authenticate("admin");

		Announcement announcementPostDelete = null;
		Rendezvous rendezvousPostDelete = null;

		this.announcementService.delete(announcementSaveFromCreate);

		// Paso 1: comprobamos que el announcement se borra corectamente

		announcementPostDelete = this.announcementService.findOne(announcementSaveFromCreate.getId());

		Assert.isNull(announcementPostDelete);

		// Paso 2: comprobamos que el announcement no se encuentra en las entidades relacionadas con él (Rendezvous)

		rendezvousPostDelete = this.rendezvousService.findOne(announcementSaveFromCreate.getRendezvous().getId());

		Assert.isTrue(!rendezvousPostDelete.getAnnouncements().contains(announcementFromCreate));

		this.unauthenticate();
	}
}
