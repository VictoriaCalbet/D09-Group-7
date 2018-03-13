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

import java.util.Date;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import security.UserAccount;
import utilities.AbstractTest;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class UserServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private UserService	userService;


	// Tests ------------------------------------------------------------------

	/**
	 * Create and save a new user
	 * Test 1: Positive case.
	 * Test 2: Negative case. Future birth date.
	 */
	@Test
	public void testSaveFromCreateUser() {
		// Manager: Name, surname, email, address, phone, birthDate, username, password, expectedException
		final Object[][] testingData = {
			{
				"testUserName1", "testUserSurname1", "testUser1@testUser1.com", "testUserAddress1", "619619619", new DateTime().plusYears(-20).toDate(), "testUser1", "testUser1", null
			}, {
				"testUserName2", "testUserSurname2", "testUser2@testUser2.com", "testUserAddress2", "619619619", new DateTime().plusYears(20).toDate(), "testUser2", "testUser2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testSaveFromCreateUserTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Date) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (Class<?>) testingData[i][8]);

	}

	protected void testSaveFromCreateUserTemplate(final String name, final String surname, final String email, final String address, final String phone, final Date birthDate, final String username, final String password, final Class<?> expectedException) {

		Class<?> caught;
		String messageError;

		caught = null;
		messageError = null;

		try {
			User result;
			UserAccount userAccount;

			result = this.userService.create();

			result.setName(name);
			result.setSurname(surname);
			result.setEmail(email);
			result.setAddress(address);
			result.setPhone(phone);
			result.setBirthDate(birthDate);

			userAccount = result.getUserAccount();
			userAccount.setUsername(username);
			userAccount.setPassword(password);
			result.setUserAccount(userAccount);

			this.userService.saveFromCreate(result);
			this.userService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			messageError = oops.getMessage();
		}

		this.checkExceptionsWithMessage(expectedException, caught, messageError);

	}
}
