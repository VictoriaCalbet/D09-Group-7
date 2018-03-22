
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.CreditCard;
import domain.Rendezvous;
import domain.Request;
import domain.Service;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class RequestServiceTest extends AbstractTest {

	// The SUT (Service Under Test) -------------------------------------------

	@Autowired
	private ServiceService		serviceService;

	@Autowired
	private RequestService		requestService;

	@Autowired
	private RendezvousService	rendezvousService;


	// Tests ------------------------------------------------------------------

	/**
	 * 
	 * Acme-Rendezvous 2.0: Requirement 4.3
	 * 
	 * Request a service for one of the rendezvouses that he or she’s created.
	 * He or she must specify a valid credit card in every request for a service.
	 * Optionally, he or she can provide some comments in the request.
	 * 
	 * Note: the service 5 will be always inappropriate in these tests
	 * 
	 * Positive test1: Requesting a service for a rendezvous created by the user and a valid credit card
	 * Positive test2: Requesting a service for a valid rendezvous and a valid credit card
	 * Positive test3: Requesting a service not requested yet for a valid rendezvous
	 * Negative test1: Requesting a service for a rendezvous that the user has not created
	 * Negative test2: Requesting a null service
	 * Negative test3: Requesting a service with null rendezvous
	 * Negative test4: Requesting an available service for an available rendezvous but invalid CreditCard number
	 * Negative test5: Requesting an available service for an available rendezvous but null creditCard number
	 * Negative test6: Requesting an inappropriate service
	 * Negative test7: Requesting an appropriate service, but the user is not the creator of the rendezvous
	 * Negative test8: Requesting an appropriate service, the user is the creator,but the rendezvous is in draft mode
	 * Negative test9: Requesting an appropriate service, but already requested for that rendezvous
	 */

	@Test
	public void testCreateRequestDriver() {
		final Object testingData[][] = {

			{
				"user1", "service4", "rendezvous1", "David Romero", "Visa", "4716228108990601", 9, 2018, 502, null
			}, {

				"user2", "service4", "rendezvous1", "David Romero", "Visa", "4716228108990601", 9, 2018, 502, IllegalArgumentException.class
			}, {

				"user1", null, "rendezvous1", "David Romero", "Visa", "4716228108990601", 9, 2018, 502, NullPointerException.class
			}, {

				"user1", "service4", null, "David Romero", "Visa", "4716228108990601", 9, 2018, 502, NullPointerException.class
			}, {
				"user1", "service4", "rendezvous1", "David Romero", "Visa", "4716228108990444", 9, 2018, 502, ConstraintViolationException.class
			}, {
				"user1", "service4", "rendezvous1", "David Romero", "Visa", null, 9, 2018, 502, IllegalArgumentException.class
			}, {
				"user1", "service5", "rendezvous1", "David Romero", "Visa", "4716228108990601", 9, 2018, 502, IllegalArgumentException.class
			}, {
				"user1", "service1", "rendezvous5", "David Romero", "Visa", "4716228108990601", 9, 2018, 502, IllegalArgumentException.class
			}, {
				"user1", "service1", "rendezvous3", "David Romero", "Visa", "4716228108990601", 9, 2018, 502, IllegalArgumentException.class
			}, {
				"user1", "service4", "rendezvous1", "David Romero", "Visa", "4716228108990601", 9, 2018, 502, ConstraintViolationException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.testCreateRequestTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (int) testingData[i][6],
				(int) testingData[i][7], (int) testingData[i][8], (Class<?>) testingData[i][9]);
	}
	protected void testCreateRequestTemplate(final String username, final String serviceBean, final String rendezvousBean, final String holderName, final String brandName, final String number, final int month, final int year, final int cvv,
		final Class<?> expectedException) {
		Class<?> caught;
		caught = null;
		String messageError = null;
		try {
			this.authenticate(username);

			final CreditCard creditCard1 = new CreditCard();
			creditCard1.setBrandName(brandName);
			creditCard1.setCvv(cvv);
			creditCard1.setHolderName(holderName);
			creditCard1.setNumber(number);
			creditCard1.setExpirationMonth(month);
			creditCard1.setExpirationYear(year);

			final Request request = this.requestService.create();

			final int serviceId = this.getEntityId(serviceBean);
			final int rendezvousId = this.getEntityId(rendezvousBean);
			request.setCreditCard(creditCard1);

			final Service service = this.serviceService.findOne(serviceId);
			final Rendezvous rendezvous = this.rendezvousService.findOne(rendezvousId);
			request.setService(service);
			request.setRendezvous(rendezvous);

			this.requestService.saveFromCreate(request);

			this.requestService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			messageError = oops.getMessage();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptionsWithMessage(expectedException, caught, messageError);

	}
}
