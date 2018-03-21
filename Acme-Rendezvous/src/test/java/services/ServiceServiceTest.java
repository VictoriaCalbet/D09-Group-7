
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
	private CategoryService	categoryService;


	// Tests ------------------------------------------------------------------

	/**
	 * Create a new service:
	 * Positive test 1: creating a service without categories
	 * Positive test 2: creating a service with one category
	 * Positive test 3: creating a service with many categories
	 * Positive test 4: creating a service without pictureURL
	 * Negative test 5: creating a service without name
	 * Negative test 6: creating a service without description
	 * Negative test 7: creating a service with wrong pictureURL
	 * Negative test 8: creating a service with isInappropriate equals true
	 * Negative test 9: creating a service with admin
	 * Negative test 10: creating a service with user
	 */

	@SuppressWarnings("unchecked")
	@Test
	public void testCreateServiceDriver() {
		// principal(actor), name, description, pictureURL, isInappropriate, categories, expected exception
		final Object[][] testingData = {
			{
				"manager1", "Service name", "Service description", "http://www.myImage.com", false, new ArrayList<String>(), null
			}, {
				"manager1", "Service name", "Service description", "http://www.myImage.com", false, new ArrayList<String>(Arrays.asList("category1")), null
			}, {
				"manager1", "Service name", "Service description", "http://www.myImage.com", false, new ArrayList<String>(Arrays.asList("category1", "category2", "category3", "category4")), null
			}, {
				"manager1", "Service name", "Service description", null, false, new ArrayList<String>(Arrays.asList("category1")), null
			}, {
				"manager1", null, "Service description", "http://www.myImage.com", false, new ArrayList<String>(Arrays.asList("category1")), ConstraintViolationException.class
			}, {
				"manager1", "Service name", null, "http://www.myImage.com", false, new ArrayList<String>(Arrays.asList("category1")), ConstraintViolationException.class
			}, {
				"manager1", "Service name", "Service description", "WRONG://www.myImage.com", false, new ArrayList<String>(Arrays.asList("category1")), ConstraintViolationException.class
			}, {
				"manager1", "Service name", "Service description", "http://www.myImage.com", true, new ArrayList<String>(Arrays.asList("category1")), IllegalArgumentException.class
			}, {
				"admin", "Service name", "Service description", "http://www.myImage.com", false, new ArrayList<String>(), IllegalArgumentException.class
			}, {
				"user", "Service name", "Service description", "http://www.myImage.com", false, new ArrayList<String>(), IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testCreateServiceTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (boolean) testingData[i][4], (ArrayList<String>) testingData[i][5], (Class<?>) testingData[i][6]);
	}
	protected void testCreateServiceTemplate(final String actor, final String name, final String description, final String pictureURL, final boolean isInappropriate, final ArrayList<String> categories, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(actor);

			Service service = null;

			service = this.serviceService.create();

			service.setName(name);
			service.setDescription(description);
			service.setPictureURL(pictureURL);
			service.setIsInappropriate(isInappropriate);

			for (int i = 0; i < categories.size(); i++)
				service.getCategories().add(this.categoryService.findOne(super.getEntityId(categories.get(i))));

			this.serviceService.saveFromCreate(service);
			this.serviceService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptions(expectedException, caught);
	}

	/**
	 * Edit a service:
	 * Positive test 1: edit a available service (without requests and isInappropriate equals false) without categories
	 * Positive test 2: edit a available service (without requests and isInappropriate equals false) with one category
	 * Positive test 3: edit a available service (without requests and isInappropriate equals false) with many categories
	 * Negative test 4: edit a available service with admin
	 * Negative test 5: edit a available service with user
	 * Negative test 6: edit a available service with isInappropriate equals true
	 * Negative test 7: edit a available service with some requests
	 * Negative test 8: edit a available service that it's not owner
	 */

	@SuppressWarnings("unchecked")
	@Test
	public void testEditServiceDriver() {
		// principal(actor), service bean, name, description, pictureURL, isInappropriate, categories, expected exception
		final Object[][] testingData = {
			{
				"manager2", "service3", "Service name mod", "Service description mod", "http://www.myImageMod.com", false, new ArrayList<String>(), null
			}, {
				"manager2", "service3", "Service name mod", "Service description mod", "http://www.myImageMod.com", false, new ArrayList<String>(Arrays.asList("category1")), null
			}, {
				"manager2", "service3", "Service name mod", "Service description mod", "http://www.myImageMod.com", false, new ArrayList<String>(Arrays.asList("category1", "category2", "category3")), null
			}, {
				"admin", "service3", "Service name mod", "Service description mod", "http://www.myImageMod.com", false, new ArrayList<String>(Arrays.asList("category1")), IllegalArgumentException.class
			}, {
				"user1", "service3", "Service name mod", "Service description mod", "http://www.myImageMod.com", false, new ArrayList<String>(Arrays.asList("category1")), IllegalArgumentException.class
			}, {
				"manager2", "service5", "Service name mod", "Service description mod", "http://www.myImageMod.com", false, new ArrayList<String>(Arrays.asList("category1")), IllegalArgumentException.class
			}, {
				"manager1", "service1", "Service name mod", "Service description mod", "http://www.myImageMod.com", false, new ArrayList<String>(Arrays.asList("category1")), IllegalArgumentException.class
			}, {
				"manager1", "service3", "Service name mod", "Service description mod", "http://www.myImageMod.com", false, new ArrayList<String>(Arrays.asList("category1")), IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testEditServiceTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (boolean) testingData[i][5], (ArrayList<String>) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}
	protected void testEditServiceTemplate(final String actor, final String serviceToEdit, final String name, final String description, final String pictureURL, final boolean isInappropriate, final ArrayList<String> categories,
		final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(actor);

			Service service = null;

			service = this.serviceService.findOne(this.getEntityId(serviceToEdit));

			service.setName(name);
			service.setDescription(description);
			service.setPictureURL(pictureURL);
			service.setIsInappropriate(isInappropriate);

			for (int i = 0; i < categories.size(); i++)
				service.getCategories().add(this.categoryService.findOne(super.getEntityId(categories.get(i))));

			this.serviceService.saveFromEdit(service);
			this.serviceService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptions(expectedException, caught);
	}

	/**
	 * Delete a service:
	 * Positive test 1: delete a service (without requests and isInappropriate equals false) without categories
	 * Negative test 2: delete a service that it's not owner
	 * Negative test 3: delete a service with admin
	 * Negative test 4: delete a service with user
	 * Negative test 5: delete a service that it has some requests
	 * Negative test 6: delete a service that it's inappropriate
	 */
	@Test
	public void testDeleteServiceDriver() {
		// principal(actor), service bean, expected
		final Object[][] testingData = {
			{
				"manager2", "service3", null
			}, {
				"manager1", "service3", IllegalArgumentException.class
			}, {
				"admin", "service3", IllegalArgumentException.class
			}, {
				"user", "service3", IllegalArgumentException.class
			}, {
				"manager1", "service1", IllegalArgumentException.class
			}, {
				"manager1", "service2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testDeleteServiceTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	protected void testDeleteServiceTemplate(final String actor, final String serviceToDelete, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(actor);

			Service service = null;

			service = this.serviceService.findOne(this.getEntityId(serviceToDelete));

			this.serviceService.delete(service);
			this.serviceService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptions(expectedException, caught);
	}

}
