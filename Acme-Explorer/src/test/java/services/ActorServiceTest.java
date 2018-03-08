
package services;

import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.CreditCard;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ActorServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private ActorService	actorService;


	// Supporting services

	// Tests
	@Test
	public void testCreditCard1NextYear() {
		final CreditCard creditCardToCreate;
		boolean validCreditCard;

		int expirationYear;
		int expirationMonth;

		expirationYear = Calendar.getInstance().get(Calendar.YEAR) + 1;
		expirationMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;

		creditCardToCreate = new CreditCard();
		creditCardToCreate.setHolderName("New explorer name");
		creditCardToCreate.setBrandName("VISA");
		creditCardToCreate.setNumber("4485750721419113");
		creditCardToCreate.setExpirationMonth(expirationMonth);
		creditCardToCreate.setExpirationYear(expirationYear);
		creditCardToCreate.setCvv(673);

		validCreditCard = this.actorService.checkCreditCard(creditCardToCreate);
		Assert.isTrue(validCreditCard);
	}

	@Test
	public void testCreditCard2ThisMonthAndYear() {
		final CreditCard creditCardToCreate;
		boolean validCreditCard;

		int expirationYear;
		int expirationMonth;

		expirationYear = Calendar.getInstance().get(Calendar.YEAR);
		expirationMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;

		creditCardToCreate = new CreditCard();
		creditCardToCreate.setHolderName("New explorer name");
		creditCardToCreate.setBrandName("VISA");
		creditCardToCreate.setNumber("4485750721419113");
		creditCardToCreate.setExpirationMonth(expirationMonth);
		creditCardToCreate.setExpirationYear(expirationYear);
		creditCardToCreate.setCvv(673);

		validCreditCard = this.actorService.checkCreditCard(creditCardToCreate);
		Assert.isTrue(validCreditCard);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreditCard3LastYear() {
		final CreditCard creditCardToCreate;
		boolean validCreditCard;

		int expirationYear;
		int expirationMonth;

		expirationYear = Calendar.getInstance().get(Calendar.YEAR) - 1;
		expirationMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;

		creditCardToCreate = new CreditCard();
		creditCardToCreate.setHolderName("New explorer name");
		creditCardToCreate.setBrandName("VISA");
		creditCardToCreate.setNumber("4485750721419113");
		creditCardToCreate.setExpirationMonth(expirationMonth);
		creditCardToCreate.setExpirationYear(expirationYear);
		creditCardToCreate.setCvv(673);

		validCreditCard = this.actorService.checkCreditCard(creditCardToCreate);
		Assert.isTrue(validCreditCard);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreditCard4LastMonthAndThisYear() {
		final CreditCard creditCardToCreate;
		boolean validCreditCard;

		int expirationYear;
		int expirationMonth;

		expirationYear = Calendar.getInstance().get(Calendar.YEAR);
		expirationMonth = Calendar.getInstance().get(Calendar.MONTH); // It was -1

		creditCardToCreate = new CreditCard();
		creditCardToCreate.setHolderName("New explorer name");
		creditCardToCreate.setBrandName("VISA");
		creditCardToCreate.setNumber("4485750721419113");
		creditCardToCreate.setExpirationMonth(expirationMonth);
		creditCardToCreate.setExpirationYear(expirationYear);
		creditCardToCreate.setCvv(673);

		validCreditCard = this.actorService.checkCreditCard(creditCardToCreate);
		Assert.isTrue(validCreditCard);
	}

	@Test
	public void testPhone1_4PN() {
		final String phone = "1234";

		boolean validPhone;

		validPhone = this.actorService.checkPhone(phone);

		Assert.isTrue(validPhone);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPhone2_3PN() {
		final String phone = "123";

		boolean validPhone;

		validPhone = this.actorService.checkPhone(phone);

		Assert.isTrue(validPhone);
	}

	@Test
	public void testPhone3_13PN() {
		final String phone = "1234567891011";

		boolean validPhone;

		validPhone = this.actorService.checkPhone(phone);

		Assert.isTrue(validPhone);
	}

	@Test
	public void testPhone4_2CC4PN() {
		final String phone = "+341234";

		boolean validPhone;

		validPhone = this.actorService.checkPhone(phone);

		Assert.isTrue(validPhone);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPhone5_3CC2PN() {
		final String phone = "+9991";

		boolean validPhone;

		validPhone = this.actorService.checkPhone(phone);

		Assert.isTrue(validPhone);
	}

	@Test
	public void testPhone6_2CC2AC4PN() {
		final String phone = "+34(619)1234";

		boolean validPhone;

		validPhone = this.actorService.checkPhone(phone);

		Assert.isTrue(validPhone);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPhone7_4CC2AC4PN() {
		final String phone = "+3434(619)1234";

		boolean validPhone;

		validPhone = this.actorService.checkPhone(phone);

		Assert.isTrue(validPhone);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPhone8_2CC4AC4PN() {
		final String phone = "+34(619619)1234";

		boolean validPhone;

		validPhone = this.actorService.checkPhone(phone);

		Assert.isTrue(validPhone);
	}

	// Query tests

}
