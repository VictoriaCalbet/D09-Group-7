
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.SponsorshipRepository;
import domain.Sponsor;
import domain.Sponsorship;
import domain.Trip;

@Service
@Transactional
public class SponsorshipService {

	// Managed Repository
	@Autowired
	private SponsorshipRepository	sponsorshipRepository;

	// Supporting Services
	@Autowired
	private TripService				tripService;

	@Autowired
	private SponsorService			sponsorService;
	@Autowired
	private MessageService			messageService;
	@Autowired
	private ActorService			actorService;


	// Constructor

	public SponsorshipService() {
		super();
	}

	// Simple CRUD methods
	public Sponsorship create() {
		return new Sponsorship();
	}

	//Trip only is needed when create a new sponsorship
	public Sponsorship save(final Sponsorship sponsorship) {

		Sponsor principal;
		principal = this.sponsorService.findByPrincipal();
		Collection<Sponsorship> sponsorships;
		sponsorships = principal.getSponsorships();
		Assert.notNull(principal);
		Assert.notNull(sponsorship.getCreditCard());
		Assert.isTrue(this.actorService.checkCreditCard(sponsorship.getCreditCard()));
		final Boolean edit = this.sponsorshipRepository.findOne(sponsorship.getId()) != null;
		if (edit)
			Assert.isTrue(sponsorships.contains(sponsorship));
		else {
			Assert.isTrue(sponsorship.getTrip().getStartMoment().after(new Date(System.currentTimeMillis())));
			Assert.isTrue(sponsorship.getTrip().getPublicationDate().before(new Date(System.currentTimeMillis())));
		}
		Assert.notNull(this.sponsorService);
		Assert.notNull(sponsorship);
		Assert.notNull(sponsorship.getBannerUrl());
		Assert.notNull(sponsorship.getInfoPage());
		Assert.notNull(sponsorship.getCreditCard().getBrandName());
		Assert.notNull(sponsorship.getCreditCard().getHolderName());
		Assert.notNull(sponsorship.getCreditCard().getNumber());
		Assert.notNull(sponsorship.getTrip());
		final Sponsorship savedSponsorship = this.sponsorshipRepository.save(sponsorship);
		//checking is supicious
		final Collection<String> allStrings = new ArrayList<String>();
		allStrings.add(savedSponsorship.getBannerUrl());
		allStrings.add(savedSponsorship.getInfoPage());
		allStrings.add(savedSponsorship.getCreditCard().getBrandName());
		allStrings.add(savedSponsorship.getCreditCard().getHolderName());
		allStrings.add(savedSponsorship.getCreditCard().getNumber());
		Boolean isSuspicious;
		isSuspicious = this.isSuspicious(allStrings);
		if (isSuspicious)
			principal.setIsSuspicious(true);
		//end check
		Trip trip;
		trip = savedSponsorship.getTrip();
		if (!edit) {
			principal.getSponsorships().add(savedSponsorship);

			Assert.notNull(trip);
			Trip tripInDB;
			tripInDB = this.tripService.findOne(trip.getId());
			Assert.notNull(tripInDB);
			tripInDB.getSponsorships().add(savedSponsorship);
			this.tripService.saveByOtherActors(tripInDB);
		}
		this.sponsorService.saveFromEdit(principal);
		return savedSponsorship;

	}
	public void delete(final Sponsorship sponsorship) {
		Sponsor principal;
		principal = this.sponsorService.findByPrincipal();
		Assert.notNull(principal);
		Assert.notNull(sponsorship);

		Sponsorship sponsorshipInDB;
		sponsorshipInDB = this.sponsorshipRepository.findOne(sponsorship.getId());
		Assert.notNull(sponsorship);
		this.isTheCorrectSponsor(sponsorship, principal);

		principal.getSponsorships().remove(sponsorshipInDB);
		this.sponsorService.saveFromEdit(principal);
		Trip tripInDB;
		tripInDB = this.tripService.findOneBySponsorshipId(sponsorship.getId());
		if (tripInDB != null) {
			tripInDB.getSponsorships().remove(sponsorshipInDB);
			this.tripService.saveByOtherActors(tripInDB);
		}
		this.sponsorshipRepository.delete(sponsorshipInDB);

	}
	public Sponsorship findOne(final int sponsorshipId) {
		return this.sponsorshipRepository.findOne(sponsorshipId);
	}
	public List<Sponsorship> findAll() {
		return this.sponsorshipRepository.findAll();
	}

	// Other business methods

	//En base al id de un viaje, busca dicho viaje y de sus sponsorships escoge uno aleatorio.
	public Sponsorship getRandomSponsorshipByTripId(final int tripId) {
		final List<Sponsorship> sponsorships = new ArrayList<Sponsorship>(this.tripService.findOne(tripId).getSponsorships());
		final int nSponsorships = sponsorships.size();
		Sponsorship s = null;
		if (nSponsorships != 0) {
			final Random random = new Random();
			final int randomIndex = random.nextInt(nSponsorships);
			s = sponsorships.get(randomIndex);
		}

		return s;
	}
	public void isTheCorrectSponsor(final Sponsorship sponsorship, final Sponsor principal) {
		Boolean sameSponsor = false;
		Collection<Sponsorship> sponsorships;
		sponsorships = principal.getSponsorships();
		for (final Sponsorship s : sponsorships)
			if (s.getId() == sponsorship.getId())
				sameSponsor = true;
		Assert.isTrue(sameSponsor);
	}
	private Boolean isSuspicious(final Collection<String> texts) {
		Boolean isSuspicious = false;
		for (final String text : texts)
			if (text != null) {
				final Boolean check = this.messageService.checkSpam(text);
				if (check) {
					isSuspicious = true;
					break;
				}
			}
		return isSuspicious;
	}
}
