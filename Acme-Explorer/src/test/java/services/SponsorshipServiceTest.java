
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.CreditCard;
import domain.Sponsorship;
import domain.Trip;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class SponsorshipServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private SponsorshipService	sponsorshipService;
	@Autowired
	private TripService			tripService;
	@Autowired
	private SponsorService		sponsorService;


	// Tests
	@Test
	public void testCreate() {
		Sponsorship sponsorship;
		sponsorship = this.sponsorshipService.create();
		Assert.notNull(sponsorship);
		Assert.isNull(sponsorship.getBannerUrl());
		Assert.isNull(sponsorship.getInfoPage());
		Assert.isNull(sponsorship.getCreditCard());
	}
	@Test
	public void testSave() {

		this.authenticate("sponsor1");
		Sponsorship sponsorship;
		sponsorship = this.sponsorshipService.create();
		sponsorship.setBannerUrl("http://www.estoesunbanner.com/banner1");
		sponsorship.setInfoPage("http://www.estoesunapaginadeinformacion.com/informacion1");
		final CreditCard creditCard = new CreditCard();
		creditCard.setBrandName("VISA");
		creditCard.setCvv(123);
		creditCard.setExpirationMonth(8);
		creditCard.setExpirationYear(2020);
		creditCard.setHolderName("Manuel Gómez García");
		creditCard.setNumber("4242424242424242");
		sponsorship.setCreditCard(creditCard);
		sponsorship.setTrip(this.tripService.findAllPublishedAndNotStarted().iterator().next());
		this.unauthenticate();
		//Sponsorship
		this.authenticate("sponsor1");
		final Sponsorship savedSponsorship = this.sponsorshipService.save(sponsorship);

		Assert.notNull(savedSponsorship.getBannerUrl());
		Assert.notNull(savedSponsorship.getInfoPage());
		Assert.notNull(savedSponsorship.getCreditCard().getBrandName());
		Assert.notNull(savedSponsorship.getCreditCard().getCvv());
		Assert.notNull(savedSponsorship.getCreditCard().getExpirationMonth());
		Assert.notNull(savedSponsorship.getCreditCard().getExpirationYear());
		Assert.notNull(savedSponsorship.getCreditCard().getHolderName());
		Assert.notNull(savedSponsorship.getCreditCard().getNumber());

		//Las siguientes dos linea prueban findOne y findAll
		Assert.isTrue(this.sponsorshipService.findOne(savedSponsorship.getId()).equals(savedSponsorship));
		Assert.isTrue(this.sponsorshipService.findAll().contains(savedSponsorship));
		Assert.isTrue(this.sponsorService.findByPrincipal().getSponsorships().contains(savedSponsorship));

		this.unauthenticate();
	}
	@Test
	public void testGetRandomSponsorshipByTripId() {
		final Trip tripInDB = this.tripService.findAllPublishedAndNotStarted().iterator().next();
		Sponsorship sponsorship1;
		sponsorship1 = this.sponsorshipService.create();
		sponsorship1.setBannerUrl("http://www.estoesunbanner.com/banner1");
		sponsorship1.setInfoPage("http://www.estoesunapaginadeinformacion.com/informaciï¿½n1");
		final CreditCard creditCard1 = new CreditCard();
		creditCard1.setBrandName("VISA");
		creditCard1.setCvv(200);
		creditCard1.setExpirationMonth(8);
		creditCard1.setExpirationYear(2020);
		creditCard1.setHolderName("Manuel Gómez García");
		creditCard1.setNumber("4677343800604200584");
		sponsorship1.setCreditCard(creditCard1);
		sponsorship1.setTrip(tripInDB);

		this.authenticate("sponsor1");
		this.sponsorshipService.save(sponsorship1);
		this.unauthenticate();

		Sponsorship randomSponsorship;
		randomSponsorship = this.sponsorshipService.getRandomSponsorshipByTripId(tripInDB.getId());
		Assert.notNull(randomSponsorship);
		this.unauthenticate();

	}
}
