
package controllers.sponsor;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import services.SponsorService;
import services.SponsorshipService;
import services.TripService;
import controllers.AbstractController;
import domain.CreditCard;
import domain.Sponsor;
import domain.Sponsorship;
import domain.Trip;

@Controller
@RequestMapping("/sponsorship/sponsor")
public class SponsorshipSponsorController extends AbstractController {

	@Autowired
	private SponsorshipService	sponsorshipService;
	@Autowired
	private SponsorService		sponsorService;
	@Autowired
	private TripService			tripService;


	public SponsorshipSponsorController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Sponsorship> sponsorships;
		Sponsor principal;
		principal = this.sponsorService.findByPrincipal();
		sponsorships = principal.getSponsorships();

		result = new ModelAndView("sponsorship/list");
		result.addObject("sponsorships", sponsorships);
		result.addObject("requestURI", "sponsorship/sponsor/list.do");
		return result;
	}
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		Sponsorship sponsorship;
		sponsorship = this.sponsorshipService.create();
		result = this.createEditModelAndView(sponsorship);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int sponsorshipId) {
		ModelAndView result;

		Sponsorship sponsorship;
		sponsorship = this.sponsorshipService.findOne(sponsorshipId);
		Assert.notNull(sponsorship);
		if (this.checkIsNotCorrectSponsor(sponsorship))
			result = new ModelAndView("misc/forbidden");
		else
			result = this.createEditModelAndView(sponsorship);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Sponsorship sponsorship, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(sponsorship);
		else
			try {
				this.sponsorshipService.save(sponsorship);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				String error = "sponsorship.commit.error";
				if (oops.getMessage().contains("message.error"))
					error = oops.getMessage();
				result = this.createEditModelAndView(sponsorship, error);
			}
		return result;

	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Sponsorship sponsorship, final BindingResult binding) {
		ModelAndView result;

		try {
			this.sponsorshipService.delete(sponsorship);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			String error = "sponsorship.commit.error";
			if (oops.getMessage().contains("message.error"))
				error = oops.getMessage();
			result = this.createEditModelAndView(sponsorship, error);
		}
		return result;
	}

	//Auxiliar method
	protected ModelAndView createEditModelAndView(final Sponsorship sponsorship) {
		ModelAndView result;
		result = this.createEditModelAndView(sponsorship, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Sponsorship sponsorship, final String messageCode) {
		ModelAndView result;
		CreditCard creditCard;
		if (sponsorship.getCreditCard() == null)
			creditCard = null;
		else
			creditCard = sponsorship.getCreditCard();

		result = new ModelAndView("sponsorship/edit");
		result.addObject("creditCard", creditCard);

		if (sponsorship.getId() == 0) {
			Collection<Trip> trips;
			trips = this.tripService.findAllPublishedAndNotStarted();
			result.addObject("trips", trips);

		}

		result.addObject("sponsorship", sponsorship);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "sponsorship/sponsor/edit.do");
		return result;
	}

	protected Boolean checkIsNotCorrectSponsor(final Sponsorship sponsorship) {

		Sponsor principal;
		principal = this.sponsorService.findByPrincipal();

		final Collection<Authority> authorities;
		authorities = principal.getUserAccount().getAuthorities();
		Boolean isNotCorrectSponsor = true;
		for (final Authority a : authorities)
			if (a.getAuthority().equals("SPONSOR")) {
				isNotCorrectSponsor = false;
				break;
			}
		if (!isNotCorrectSponsor) {
			Collection<Sponsorship> sponsorships;
			sponsorships = principal.getSponsorships();
			isNotCorrectSponsor = !sponsorships.contains(sponsorship);
		}
		return isNotCorrectSponsor;

	}

}
