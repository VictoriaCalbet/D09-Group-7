
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
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
	 * Create and save a new service
	 * Test 1: Positive case --> expected: Request a services Successful
	 * Test 2: Negative case. Fail requesting a service
	 */

	@Test
	public void testCreateRequestDriver() {
		final Object testingData[][] = {
			{
				"user1", "86", "80", "76", null

			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.testCreateRequestTemplate((String) testingData[i][0], (int) testingData[i][1], (int) testingData[i][2], (int) testingData[i][3], (Class<?>) testingData[i][4]);
	}
	protected void testCreateRequestTemplate(final String username, final int requestId, final int rendezvousId, final int serviceId, final Class<?> expectedException) {
		Class<?> caught;
		caught = null;

		try {
			this.authenticate(username);
			final Service service = this.serviceService.findOne(serviceId);
			final Request request = this.requestService.findOne(requestId);
			final Rendezvous rendezvous = this.rendezvousService.findOne(rendezvousId);
			request.setService(service);
			request.setRendezvous(rendezvous);
			this.requestService.saveFromCreate(request);

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expectedException, caught);
	}
}
