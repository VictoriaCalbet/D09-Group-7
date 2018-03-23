
package services;

import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import security.UserAccount;
import utilities.AbstractTest;
import domain.Manager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ManagerServiceTest extends AbstractTest {

	// Service under test -----------------------------------------------------

	@Autowired
	private ManagerService	managerService;


	// Tests ------------------------------------------------------------------

	/**
	 * 
	 * Acme-Rendezvous 2.0: Requirement 3.1
	 * 
	 * An actor who is not authenticated must be able to:
	 * - Register to the system as a manager.
	 * 
	 * Positive test 1: Create a manager.
	 * Negative test 2: Create a manager with a wrong VAT pattern.
	 * Negative test 3: Create a manager with a future birth date.
	 */
	@Test
	public void testSaveFromCreateManager() {
		// Manager: Name, surname, email, address, phone, birthDate, VAT, username, password, expectedException
		final Object[][] testingData = {
			{
				"testManagerName1", "testManagerSurname1", "testManager1@testManager1.com", "testManagerAddress1", "619619619", new DateTime().plusYears(-20).toDate(), "TEST-VAT-1", "testManager1", "testManager1", null
			}, {
				"testManagerName2", "testManagerSurname2", "testManager2@testManager2.com", "testManagerAddress2", "619619619", new DateTime().plusYears(-20).toDate(), "WRONG&TEST&VAT", "testManager2", "testManager2", ConstraintViolationException.class
			}, {
				"testManagerName3", "testManagerSurname3", "testManager3@testManager3.com", "testManagerAddress3", "619619619", new DateTime().plusDays(10).toDate(), "TEST-VAT-3", "testManager3", "testManager3", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testSaveFromCreateManagerTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Date) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (String) testingData[i][8], (Class<?>) testingData[i][9]);

	}

	protected void testSaveFromCreateManagerTemplate(final String name, final String surname, final String email, final String address, final String phone, final Date birthDate, final String VAT, final String username, final String password,
		final Class<?> expectedException) {

		Class<?> caught;
		String messageError;

		caught = null;
		messageError = null;

		try {
			Manager result;
			UserAccount userAccount;

			result = this.managerService.create();

			result.setName(name);
			result.setSurname(surname);
			result.setEmail(email);
			result.setAddress(address);
			result.setPhone(phone);
			result.setBirthDate(birthDate);
			result.setVAT(VAT);

			userAccount = result.getUserAccount();
			userAccount.setUsername(username);
			userAccount.setPassword(password);
			result.setUserAccount(userAccount);

			this.managerService.saveFromCreate(result);
			this.managerService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			messageError = oops.getMessage();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptionsWithMessage(expectedException, caught, messageError);

	}
}
