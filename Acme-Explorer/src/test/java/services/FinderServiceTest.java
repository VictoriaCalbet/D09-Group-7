
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Finder;
import domain.Trip;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class FinderServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private FinderService	finderService;
	@Autowired
	private ExplorerService	explorerService;


	// Tests
	@Test
	/**
	 * Positive test: Correct Finder create and save
	 */
	public void testCreateAndSave() {
		this.authenticate("explorer1");

		final Finder finder = this.finderService.create();
		Assert.isNull(finder.getEndDate());
		Assert.isNull(finder.getStartDate());
		Assert.isNull(finder.getMoment());
		Assert.isNull(finder.getMaxPrice());
		Assert.isNull(finder.getMinPrice());
		Assert.isTrue(finder.getKeyWord().equals(""));
		Assert.notNull(finder.getFound());
		Assert.isTrue(finder.getFound().isEmpty());

		finder.setKeyWord("Trip");
		finder.setMaxPrice(4000.0);
		finder.setMinPrice(1000.0);
		finder.setExplorer(this.explorerService.findByPrincipal());
		final Finder saved = this.finderService.save(finder);
		Assert.notNull(this.finderService.findFinderByExplorerPrincipal().equals(saved));
		this.unauthenticate();
	}

	@Test
	public void testGetTripsFound() {
		this.authenticate("explorer1");

		final Finder finder = this.finderService.create();
		finder.setKeyWord("Trip");
		finder.setMaxPrice(4000.0);
		finder.setMinPrice(1000.0);
		finder.setExplorer(this.explorerService.findByPrincipal());
		final Finder saved = this.finderService.save(finder);

		for (final Trip t : this.finderService.getTripsFound(saved)) {
			Assert.isTrue(t.getPrice() >= 1000.0);
			Assert.isTrue(t.getPrice() <= 4000.0);
			Assert.isTrue(t.getTitle().contains("Trip") || t.getDescription().contains("Trip"));
		}

		Assert.isTrue(saved.getId() != 0);

		this.unauthenticate();
	}
}
