
package controllers.manager;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ManagerService;
import services.StageService;
import controllers.AbstractController;
import domain.Stage;
import domain.Trip;

@Controller
@RequestMapping("/stage/manager")
public class StageManagerController extends AbstractController {

	//Services
	@Autowired
	private StageService	stageService;
	@Autowired
	private ManagerService	managerService;


	//Constructor

	public StageManagerController() {
		super();
	}

	// Creation
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int tripId) {
		ModelAndView result;
		try {
			Stage stage;
			stage = this.stageService.create(tripId);
			result = this.createModelAndView(stage);
		} catch (final Throwable oops) {
			String messageError = "";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();
			result = new ModelAndView("redirect:/trip/manager/list.do");
			result.addObject("message", messageError);

		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int stageId) {
		ModelAndView result = new ModelAndView();
		try {
			final Stage stage = this.stageService.findOne(stageId);
			Assert.isTrue(this.managerService.findByPrincipal().getTrips().contains(stage.getTrip()));
			result = this.editModelAndView(stage);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/trip/manager/list.do");
		}
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Stage stage, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createModelAndView(stage);
		else
			try {
				this.stageService.save(stage);
				result = new ModelAndView("redirect:/stage/list.do?tripId=" + stage.getTrip().getId());
			} catch (final Throwable oops) {
				String messageError = "stage.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createModelAndView(stage, messageError);
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveEdit(@Valid final Stage stage, final BindingResult binding) {
		ModelAndView result = new ModelAndView();

		if (binding.hasErrors())
			result = this.editModelAndView(stage);
		else
			try {
				this.stageService.update(stage);
				result = new ModelAndView("redirect:/stage/list.do?tripId=" + stage.getTrip().getId());
			} catch (final Throwable oops) {
				String messageError = "stage.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.editModelAndView(stage, messageError);
			}

		return result;
	}

	//Delete
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int stageId) {
		ModelAndView result;
		Trip t = new Trip();
		try {
			t = this.stageService.findOne(stageId).getTrip();
			this.stageService.delete(stageId);
			result = new ModelAndView("redirect:/stage/list.do?tripId=" + t.getId());
		} catch (final Throwable oops) {
			String messageError = "stage.delete.error";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();
			result = new ModelAndView("redirect:/trip/manager/list.do");
			result.addObject("message", messageError);
		}
		return result;
	}

	// Ancillaty methods
	protected ModelAndView createModelAndView(final Stage stage) {
		ModelAndView result;

		result = this.createModelAndView(stage, null);

		return result;
	}

	protected ModelAndView createModelAndView(final Stage stage, final String message) {
		ModelAndView result;

		result = new ModelAndView("stage/manager/create");
		result.addObject("stage", stage);
		result.addObject("message", message);

		return result;
	}

	protected ModelAndView editModelAndView(final Stage stage) {
		ModelAndView result;

		result = this.editModelAndView(stage, null);

		return result;
	}

	protected ModelAndView editModelAndView(final Stage stage, final String message) {
		ModelAndView result;

		result = new ModelAndView("stage/manager/edit");
		result.addObject("stage", stage);
		result.addObject("message", message);

		return result;
	}

}
