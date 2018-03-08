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

import security.UserAccount;
import utilities.AbstractTest;
import domain.Administrator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class AdministratorServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private ActorService			actorService;


	// Tests ------------------------------------------------------------------

	@Test
	public void testCreate1() {
		this.authenticate("admin");

		final Administrator createdAdministrator = this.administratorService.create();

		Assert.isNull(createdAdministrator.getName());
		Assert.isNull(createdAdministrator.getSurname());
		Assert.isNull(createdAdministrator.getAddress());
		Assert.isNull(createdAdministrator.getBirthDate());
		Assert.isNull(createdAdministrator.getEmail());
		Assert.isNull(createdAdministrator.getPhone());
		Assert.isNull(createdAdministrator.getUserAccount().getUsername());
		Assert.isNull(createdAdministrator.getUserAccount().getPassword());

		Assert.isTrue(this.actorService.checkAuthority(createdAdministrator, "ADMIN"));

		this.unauthenticate();
	}

	@Test
	public void testSaveFromCreate1() {
		this.authenticate("admin");

		final Administrator createdAdministrator = this.administratorService.create();
		Administrator savedAdministrator;
		UserAccount savedUserAccount;

		createdAdministrator.setName("Example name");
		createdAdministrator.setSurname("Example surname");
		createdAdministrator.setEmail("example@example.com");

		savedUserAccount = createdAdministrator.getUserAccount();
		savedUserAccount.setUsername("Example");
		savedUserAccount.setPassword("Example");

		createdAdministrator.setUserAccount(savedUserAccount);

		savedAdministrator = this.administratorService.saveFromCreate(createdAdministrator);

		Assert.isTrue(savedAdministrator.getName().equals(createdAdministrator.getName()));
		Assert.isTrue(savedAdministrator.getSurname().equals(createdAdministrator.getSurname()));
		Assert.isTrue(savedAdministrator.getEmail().equals(createdAdministrator.getEmail()));

		this.unauthenticate();

	}

	@Test
	public void testSaveFromEdit1() {
		this.authenticate("admin");

		final Administrator createdAdministrator = this.administratorService.create();
		Administrator savedAdministrator;
		UserAccount savedUserAccount;

		createdAdministrator.setName("Example name");
		createdAdministrator.setSurname("Example surname");
		createdAdministrator.setEmail("example@example.com");

		savedUserAccount = createdAdministrator.getUserAccount();
		savedUserAccount.setUsername("Example");
		savedUserAccount.setPassword("Example");

		createdAdministrator.setUserAccount(savedUserAccount);

		savedAdministrator = this.administratorService.saveFromCreate(createdAdministrator);

		this.unauthenticate();

		this.authenticate("example");

		Administrator editedAdministrator;
		UserAccount editedUserAccount;

		savedAdministrator.setName("Edited Example");

		editedUserAccount = savedAdministrator.getUserAccount();
		editedUserAccount.setPassword("Example");

		savedAdministrator.setUserAccount(editedUserAccount);

		editedAdministrator = this.administratorService.save(savedAdministrator);

		Assert.isTrue(editedAdministrator.getName().equals(editedAdministrator.getName()));

		this.unauthenticate();

	}
}
