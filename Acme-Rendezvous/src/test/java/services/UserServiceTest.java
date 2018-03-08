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
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class UserServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private UserService		userService;

	@Autowired
	private ActorService	actorService;


	// Tests ------------------------------------------------------------------

	@Test
	public void testCreate1() {
		final User createdUser = this.userService.create();

		Assert.isNull(createdUser.getName());
		Assert.isNull(createdUser.getSurname());
		Assert.isNull(createdUser.getAddress());
		Assert.isNull(createdUser.getBirthDate());
		Assert.isNull(createdUser.getEmail());
		Assert.isNull(createdUser.getPhone());
		Assert.isNull(createdUser.getUserAccount().getUsername());
		Assert.isNull(createdUser.getUserAccount().getPassword());

		Assert.isTrue(this.actorService.checkAuthority(createdUser, "USER"));

		this.unauthenticate();
	}

	@Test
	public void testSaveFromCreate1() {
		final User createdUser = this.userService.create();
		User savedUser;
		UserAccount savedUserAccount;

		createdUser.setName("Example name");
		createdUser.setSurname("Example surname");
		createdUser.setEmail("example@example.com");

		savedUserAccount = createdUser.getUserAccount();
		savedUserAccount.setUsername("Example");
		savedUserAccount.setPassword("Example");

		createdUser.setUserAccount(savedUserAccount);

		savedUser = this.userService.saveFromCreate(createdUser);

		Assert.isTrue(savedUser.getName().equals(createdUser.getName()));
		Assert.isTrue(savedUser.getSurname().equals(createdUser.getSurname()));
		Assert.isTrue(savedUser.getEmail().equals(createdUser.getEmail()));

		this.unauthenticate();

	}

	@Test
	public void testSaveFromEdit1() {
		final User createdUser = this.userService.create();
		User savedUser;
		UserAccount savedUserAccount;

		createdUser.setName("Example name");
		createdUser.setSurname("Example surname");
		createdUser.setEmail("example@example.com");

		savedUserAccount = createdUser.getUserAccount();
		savedUserAccount.setUsername("Example");
		savedUserAccount.setPassword("Example");

		createdUser.setUserAccount(savedUserAccount);

		savedUser = this.userService.saveFromCreate(createdUser);

		this.unauthenticate();

		this.authenticate("example");

		User editedUser;
		UserAccount editedUserAccount;

		savedUser.setName("Edited Example");

		editedUserAccount = savedUser.getUserAccount();
		editedUserAccount.setPassword("Example");

		savedUser.setUserAccount(editedUserAccount);

		editedUser = this.userService.save(savedUser);

		Assert.isTrue(editedUser.getName().equals(editedUser.getName()));

		this.unauthenticate();

	}
}
