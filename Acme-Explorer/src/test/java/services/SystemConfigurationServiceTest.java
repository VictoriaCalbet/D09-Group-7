
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.SystemConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class SystemConfigurationServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private SystemConfigurationService	systemConfigurationService;


	// Tests
	@Test
	public void testCreateSave() {

		this.authenticate("admin");
		final SystemConfiguration sc = this.systemConfigurationService.create();
		sc.setBannerUrl("https://www.merriam-webster.com/dictionary/blae");
		sc.setWelcomeMessageEnglish("hi");

		final SystemConfiguration saved = this.systemConfigurationService.save(sc);
		Assert.isTrue(saved.getId() != 0);
		Assert.isTrue(saved.getSpamWords().contains("viagra"));
		this.unauthenticate();
	}

	@Test
	public void testChangeFinderNumber() {

		this.authenticate("admin");
		final SystemConfiguration sc = this.systemConfigurationService.findMain();
		this.systemConfigurationService.changeDefaultFinderNumber(9);
		Assert.isTrue(sc.getDefaultFinderNumber() == 9);
		this.unauthenticate();
	}

	@Test
	public void testChangeDefaultCacheTime() {

		this.authenticate("admin");
		final SystemConfiguration sc = this.systemConfigurationService.findMain();
		this.systemConfigurationService.changeDefaultCacheTime(24);
		Assert.isTrue(sc.getDefaultCacheTime() == 24);
		this.unauthenticate();
	}
}
