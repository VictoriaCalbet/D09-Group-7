
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Category;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CategoryServiceTest extends AbstractTest {

	@Autowired
	private CategoryService	categoryService;


	/**
	 * 
	 * Acme-Rendezvous 2.0: Requirement 11.1
	 * 
	 * An actor who is authenticated as an administrator must be able to:
	 * -Manage the categories of services, which includes listing, creating, updating, 
	 * deleting, and re-organising them in the category hierarchies.
	 * 
	 * These tests check that a category is deleted from the dabatase properly
	 * 
	 * Test 1: Positive case.
	 * Test 2: Negative case. The category is null
	 */
	
	@Test
	public void testDeleteCategory() {
		// Category, expectedException
		final Category category = this.categoryService.findOne(this.getEntityId("category2"));

		final Object[][] testingData = {

			{
				category, null
			}, {
				null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testDeleteCategoryTemplate((Category) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	protected void testDeleteCategoryTemplate(final Category category, final Class<?> expectedException) {

		Class<?> caught;
		String messageError;

		caught = null;
		messageError = null;

		try {
			this.authenticate("admin");

			this.categoryService.delete(category);

			this.unauthenticate();
			this.categoryService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			messageError = oops.getMessage();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptionsWithMessage(expectedException, caught, messageError);

	}
	
	/**
	 * 
	 * Acme-Rendezvous 2.0: Requirement 11.1
	 * 
	 * An actor who is authenticated as an administrator must be able to:
	 * -Manage the categories of services, which includes listing, creating, updating, 
	 * deleting, and re-organising them in the category hierarchies.
	 * 
	 * These tests check that the creation of a category from scratch works properly.
	 * 
	 * Test 1: Positive case, linked to a parent.
	 * Test 2: Negative case. The name of the category is null
	 * Test 3: Negative case. The description of the category is null
	 * Test 4: Positive case, not linked to a parent.
	 * Test 5: Negative case. All required fields are blank
	 */

	@Test
	public void testSaveFromCreateCategory() {
		//Category: name, description, optional parent, expectedException
		final Category parent = this.categoryService.findOne(this.getEntityId("category2"));

		final Object[][] testingData = {

			{
				"testText1", "textDescription", parent, null
			}, {
				null, "textDescription", parent, IllegalArgumentException.class
			}, {
				"testText3", null, parent, IllegalArgumentException.class
			}, {
				"testText4", "Description", null, null
			}, {
				null, null, null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testSaveFromCreateCategoryTemplate((String) testingData[i][0], (String) testingData[i][1], (Category) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	protected void testSaveFromCreateCategoryTemplate(final String name, final String description, final Category parent, final Class<?> expectedException) {

		Class<?> caught;
		String messageError;

		caught = null;
		messageError = null;

		try {
			this.authenticate("user1");

			final Category category = this.categoryService.create();

			category.setName(name);
			category.setDescription(description);
			category.setParent(parent);

			this.categoryService.saveFromCreate(category);

			this.unauthenticate();
			this.categoryService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			messageError = oops.getMessage();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptionsWithMessage(expectedException, caught, messageError);

	}
	
	/**
	 * 
	 * Acme-Rendezvous 2.0: Requirement 11.1
	 * 
	 * An actor who is authenticated as an administrator must be able to:
	 * -Manage the categories of services, which includes listing, creating, updating, 
	 * deleting, and re-organising them in the category hierarchies.
	 * 
	 * These test check that the edition of an existing category from scratch works properly.
	 * 
	 * Test 1: Positive case, linked to a parent.
	 * Test 2: Negative case. The name of the category is null
	 * Test 3: Negative case. The description of the category is null
	 * Test 4: Positive case, not linked to a parent.
	 * Test 5: Negative case. All required fields are blank
	 */
	@Test
	public void testSaveFromEditCategory() {
		//Category: name, description, parent, categoryToEdit, expectedException
		final Category category = this.categoryService.findOne(this.getEntityId("category1"));
		final Category parent = this.categoryService.findOne(this.getEntityId("category2"));

		final Object[][] testingData = {

			{
				"testText1", "textDescription", parent, category, null
			}, {
				null, "textDescription", parent, category, IllegalArgumentException.class
			}, {
				"testText3", null, parent, category, IllegalArgumentException.class
			}, {
				"testText4", "Description", null, category, null
			}, {
				null, null, null, category, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testSaveFromEditCategoryTemplate((String) testingData[i][0], (String) testingData[i][1], (Category) testingData[i][2], (Category) testingData[i][3], (Class<?>) testingData[i][4]);

	}

	protected void testSaveFromEditCategoryTemplate(final String name, final String description, final Category parent, final Category category, final Class<?> expectedException) {

		Class<?> caught;
		String messageError;

		caught = null;
		messageError = null;

		try {
			this.authenticate("admin");

			category.setName(name);
			category.setDescription(description);
			category.setParent(parent);

			this.categoryService.saveFromEdit(category);

			this.unauthenticate();
			this.categoryService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			messageError = oops.getMessage();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptionsWithMessage(expectedException, caught, messageError);

	}

}
