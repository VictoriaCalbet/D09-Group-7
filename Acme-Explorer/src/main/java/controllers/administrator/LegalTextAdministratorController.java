// Tag Controller

package controllers.administrator;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.LegalTextService;
import controllers.AbstractController;
import domain.LegalText;

@Controller
@RequestMapping("/legaltext/administrator")
public class LegalTextAdministratorController extends AbstractController {

	//Services
	@Autowired
	private LegalTextService	legalTextService;


	//Constructor
	public LegalTextAdministratorController() {

		super();
	}

	//Listing as a non-admin actor
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		final Collection<LegalText> legaltexts = this.legalTextService.findAll();
		result = new ModelAndView("legaltext/list");
		result.addObject("legaltexts", legaltexts);
		result.addObject("requestURI", "legaltext/administrator/list.do");

		return result;
	}

	//Create a legaltext

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {

		ModelAndView result;
		LegalText legaltext;

		legaltext = this.legalTextService.create();
		result = this.createEditModelAndView(legaltext);

		return result;

	}

	//Edit a legaltext

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int legaltextId) {

		ModelAndView result;

		try {
			final LegalText legaltext = this.legalTextService.findOneById(legaltextId);
			Assert.notNull(legaltext, "message.error.legaltext.null");
			Assert.isTrue(legaltext.getIsDraft() == true, "message.error.legaltext.draft");
			result = this.createEditModelAndView(legaltext);
		} catch (final Throwable oops) {

			String messageError = "message.error.legaltext.null";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();
			final Collection<LegalText> legaltexts = this.legalTextService.findAll();
			result = new ModelAndView("legaltext/list");
			result.addObject("legaltexts", legaltexts);
			result.addObject("requestURI", "legaltext/administrator/list.do");
			result.addObject("message", messageError);

		}
		return result;
	}

	//Save a legaltext
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid @ModelAttribute("legaltext") final LegalText legaltext, final BindingResult binding) {

		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(legaltext);
		else
			try {
				if (legaltext.getId() <= 0)
					this.legalTextService.save(legaltext);
				else
					this.legalTextService.edit(legaltext);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {

				String messageError = "legaltext.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createEditModelAndView(legaltext, messageError);

			}

		return result;
	}

	//Delete a legaltext
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final LegalText legaltext, final BindingResult binding) {

		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(legaltext);
		else
			try {

				this.legalTextService.delete(legaltext);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {

				String messageError = "legaltext.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createEditModelAndView(legaltext, messageError);

			}
		return result;
	}

	// Ancillaty methods
	protected ModelAndView createEditModelAndView(final LegalText legaltext) {

		ModelAndView result;

		result = this.createEditModelAndView(legaltext, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final LegalText legaltext, final String message) {
		ModelAndView result;

		result = new ModelAndView("legaltext/edit");
		result.addObject("legaltext", legaltext);
		result.addObject("message", message);

		return result;
	}

}
