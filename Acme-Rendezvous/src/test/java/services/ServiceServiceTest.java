
package services;

import java.util.ArrayList;
import java.util.Arrays;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Service;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ServiceServiceTest extends AbstractTest {

	// The SUT (Service Under Test) -------------------------------------------

	@Autowired
	private ServiceService	serviceService;

	@Autowired
	private RequestService	requestService;

	@Autowired
	private CategoryService	categoryService;


	// Tests ------------------------------------------------------------------

	/**
	 * Create and save a new service
	 * Test 1: Positive case --> expected: service created
	 * Test 2: Negative case. Wrong VAT pattern.
	 * Test 3: Negative case. Future birth date.
	 */

	@SuppressWarnings("unchecked")
	@Test
	public void testCreateServiceDriver() {
		// principal(manager), name, description, pictureURL, isInappropriate, requests, categories, expected exception
		final Object[][] testingData = {
			{
				"manager1", "Service name", "Service description", "http://www.myImage.com", false, new ArrayList<String>(), new ArrayList<String>(Arrays.asList("category1")), null
			}, {
				"manager1", null, "Service description", "http://www.myImage.com", false, new ArrayList<String>(), new ArrayList<String>(Arrays.asList("category1")), ConstraintViolationException.class
			}, {
				"manager1", "Service name", null, "http://www.myImage.com", false, new ArrayList<String>(), new ArrayList<String>(Arrays.asList("category1")), ConstraintViolationException.class
			}, {
				"manager1", "Service name", "Service description", "WRONG://www.myImage.com", false, new ArrayList<String>(), new ArrayList<String>(Arrays.asList("category1")), ConstraintViolationException.class
			}, {
				"manager1", "Service name", "Service description", "http://www.myImage.com", true, new ArrayList<String>(), new ArrayList<String>(Arrays.asList("category1")), IllegalArgumentException.class
			}, {
				"manager1", "Service name", "Service description", "http://www.myImage.com", false, new ArrayList<String>(Arrays.asList("request1")), new ArrayList<String>(Arrays.asList("category1")), IllegalArgumentException.class
			}, {
				"manager1", "Service name", "Service description", "http://www.myImage.com", false, new ArrayList<String>(Arrays.asList("request1", "request2")), new ArrayList<String>(Arrays.asList("category1")), IllegalArgumentException.class
			}, {
				"manager1", "Service name", "Service description", "http://www.myImage.com", false, new ArrayList<String>(Arrays.asList("request1", "request2", "request3")), new ArrayList<String>(Arrays.asList("category1")), IllegalArgumentException.class
			}, {
				"manager1", "Service name", "Service description", "http://www.myImage.com", false, new ArrayList<String>(), new ArrayList<String>(), IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testCreateServiceTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (boolean) testingData[i][4], (ArrayList<String>) testingData[i][5],
				(ArrayList<String>) testingData[i][6], (Class<?>) testingData[i][7]);
	}
	protected void testCreateServiceTemplate(final String manager, final String name, final String description, final String pictureURL, final boolean isInappropriate, final ArrayList<String> requests, final ArrayList<String> categories,
		final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate("manager1");

			Service service = null;
			Service result = null;

			service = this.serviceService.create();

			service.setName(name);
			service.setDescription(description);
			service.setPictureURL(pictureURL);
			service.setIsInappropriate(isInappropriate);

			for (int i = 0; i < requests.size(); i++)
				service.getRequests().add(this.requestService.findOne(this.getEntityId(requests.get(i))));

			for (int i = 0; i < categories.size(); i++)
				service.getCategories().add(this.categoryService.findOne(super.getEntityId(categories.get(i))));

			result = this.serviceService.saveFromCreate(service);
			this.serviceService.flush();

			Assert.notNull(result);

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expectedException, caught);
	}
}
