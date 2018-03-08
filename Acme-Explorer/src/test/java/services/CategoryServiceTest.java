
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Category;
import domain.Trip;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class CategoryServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private CategoryService	categoryService;


	//Services needed

	// Tests
	@Test
	public void createCategory() {

		super.authenticate("admin");

		final Category newCat = this.categoryService.create();
		Assert.isTrue(!(newCat.getId() > 0));
		this.unauthenticate();

	}

	@Test
	public void saveCategory() {

		this.authenticate("admin");

		final Category newCat = this.categoryService.create();
		
		Assert.isTrue(!(newCat.getId() > 0));

		final Collection<Trip> trips = new ArrayList<Trip>();
		newCat.setName("Amazonas");
		
		newCat.setTrips(trips);

		final Category savedCat = this.categoryService.save(newCat);

		Assert.isTrue(savedCat.getId() > 0);
		Assert.isTrue(savedCat.getName().equals(newCat.getName()));
		//Assert.isTrue(savedCat.getParent().equals(newCat.getParent()));
		Assert.isTrue(savedCat.getTrips().equals(newCat.getTrips()));
		this.unauthenticate();
	}
	@Test
	public void deleteCategory() {
		this.authenticate("admin");
		final Category newCat = this.categoryService.create();

		Assert.isTrue(!(newCat.getId() > 0));

		final Collection<Trip> trips = new ArrayList<Trip>();
		newCat.setName("Amazonas");
		newCat.setParent(null);
		newCat.setTrips(trips);

		final Category savedCat = this.categoryService.save(newCat);

		this.categoryService.delete(savedCat);

		Assert.isNull(this.categoryService.findById(savedCat.getId()));

		this.unauthenticate();
	}

	@Test
	public void browseTripsByCategory() {
		this.authenticate("admin");
		final Category newCat = this.categoryService.create();
		Assert.isTrue(!(newCat.getId() > 0));
		final Collection<Trip> trips = new ArrayList<Trip>();
		newCat.setName("Amazonas");
		newCat.setParent(null);
		newCat.setTrips(trips);

		final Category savedCat = this.categoryService.save(newCat);

		final Collection<Trip> tripsSav = this.categoryService.browseTripsByCategory(savedCat);

		Assert.notNull(tripsSav);
		this.unauthenticate();
	}

	@Test
	public void findCategoryById() {
		this.authenticate("admin");
		final Category newCat = this.categoryService.create();
		Assert.isTrue(!(newCat.getId() > 0));
		final Collection<Trip> trips = new ArrayList<Trip>();
		newCat.setName("Amazonas");
		newCat.setParent(null);
		newCat.setTrips(trips);

		final Category savedCat = this.categoryService.save(newCat);

		final Category retrievedCat = this.categoryService.findById(savedCat.getId());

		Assert.isTrue(savedCat.equals(retrievedCat));
		this.unauthenticate();
	}

	//	@Test
	//	public void referenceTripToCategory() {
	//		this.authenticate("admin");
	//		final Category newCat = this.categoryService.create();
	//		Assert.isTrue(!(newCat.getId() > 0));
	//		final Collection<Trip> trips = new ArrayList<Trip>();
	//		newCat.setName("Amazonas");
	//		newCat.setParent(null);
	//		newCat.setTrips(trips);
	//
	//		final Trip t = this.tripService.findOne(4925);
	//		final Category savedCat = this.categoryService.save(newCat);
	//		this.categoryService.referenceTrip(savedCat, t);
	//
	//		Assert.notNull(savedCat.getTrips());
	//		Assert.notEmpty(savedCat.getTrips());
	//		this.unauthenticate();
	//	}

}
