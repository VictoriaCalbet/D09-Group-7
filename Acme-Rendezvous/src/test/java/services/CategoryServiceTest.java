
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


	@Test
	public void testDeleteCategory() {
		// Comment: text, optional URL picture,rendezvous, expectedException
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

	@Test
	public void testSaveFromCreateCategory() {

		final Category parent = this.categoryService.findOne(this.getEntityId("category2"));

		final Object[][] testingData = {

			{
				"testText1", "textDescription", parent, null
			}, {
				null, "textDescription", parent, IllegalArgumentException.class
			}, {
				"testText3", null, parent, IllegalArgumentException.class
			}, {
				"testText4", "Lalala", null, null
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

	@Test
	public void testSaveFromEditCategory() {

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
				"testText4", "Lalala", null, category, null
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
