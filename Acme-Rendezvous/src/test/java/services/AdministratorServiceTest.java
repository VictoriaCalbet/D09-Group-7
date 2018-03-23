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
import domain.Administrator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AdministratorServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------
	@Autowired
	private AdministratorService	administratorService;


	// Tests ------------------------------------------------------------------

	/**
	 * 
	 * Acme-Rendezvous 1.0: Requirement not listed
	 * 
	 * Create and save a new administrator
	 * 
	 * Positive test 1: Create an administrator logged as an administrator.
	 * Negative test 2: Create an administrator with a birth date in the future.
	 */
	@Test
	public void testSaveFromCreateAdmin() {
		// Manager: Name, surname, email, address, phone, birthDate, username, password, expectedException
		final Object[][] testingData = {
			{
				"testAdminName1", "testAdminSurname1", "testAdmin1@testAdmin1.com", "testAdminAddress1", "619619619", new DateTime().plusYears(-20).toDate(), "testAdmin1", "testAdmin1", null
			}, {
				"testAdminName2", "testAdminSurname2", "testAdmin2@testAdmin2.com", "testAdminAddress2", "619619619", new DateTime().plusYears(20).toDate(), "testAdmin2", "testAdmin2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testSaveFromCreateAdminTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Date) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (Class<?>) testingData[i][8]);

	}

	protected void testSaveFromCreateAdminTemplate(final String name, final String surname, final String email, final String address, final String phone, final Date birthDate, final String username, final String password, final Class<?> expectedException) {

		Class<?> caught;
		String messageError;

		caught = null;
		messageError = null;

		try {
			this.authenticate("admin");
			Administrator result;
			UserAccount userAccount;

			result = this.administratorService.create();

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

			this.administratorService.saveFromCreate(result);
			this.administratorService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			messageError = oops.getMessage();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptionsWithMessage(expectedException, caught, messageError);

	}
}
