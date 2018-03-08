
package controllers.ranger;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import services.ProfessionalRecordService;
import services.RangerService;
import controllers.AbstractController;
import domain.Curriculum;
import domain.ProfessionalRecord;
import domain.Ranger;

@Controller
@RequestMapping("/professionalRecord/ranger")
public class ProfessionalRecordRangerController extends AbstractController {

	@Autowired
	ProfessionalRecordService	professionalRecordService;
	@Autowired
	RangerService				rangerService;


	@RequestMapping(value = "create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		ProfessionalRecord professionalRecord;
		professionalRecord = this.professionalRecordService.create();
		result = this.createEditModelAndView(professionalRecord);
		return result;

	}
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int professionalRecordId) {
		final ModelAndView result;
		ProfessionalRecord professionalRecord;
		professionalRecord = this.professionalRecordService.findOne(professionalRecordId);
		if (professionalRecord != null) {
			if (this.checkIsNotCorrectRanger(professionalRecord))
				result = new ModelAndView("misc/forbidden");
			else
				result = this.createEditModelAndView(professionalRecord);
		} else
			result = new ModelAndView("misc/forbidden");
		return result;

	}
	@RequestMapping(value = "edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final ProfessionalRecord professionalRecord, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors())
			result = this.createEditModelAndView(professionalRecord);
		else
			try {
				this.professionalRecordService.save(professionalRecord);
				result = new ModelAndView("redirect:/curriculum/display.do");
			} catch (final Throwable oops) {
				String error = "professionalRecord.commit.error";
				if (oops.getMessage().contains("message.error"))
					error = oops.getMessage();
				result = this.createEditModelAndView(professionalRecord, error);
			}
		return result;
	}
	@RequestMapping(value = "edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final ProfessionalRecord professionalRecord, final BindingResult binding) {
		ModelAndView result;
		try {
			this.professionalRecordService.delete(professionalRecord);
			result = new ModelAndView("redirect:/curriculum/display.do");
		} catch (final Throwable oops) {
			String error = "professioanalRecord.commit.error";
			if (oops.getMessage().contains("message.error"))
				error = oops.getMessage();
			result = this.createEditModelAndView(professionalRecord, error);
		}
		return result;
	}
	protected ModelAndView createEditModelAndView(final ProfessionalRecord professionalRecord) {
		ModelAndView result;
		result = this.createEditModelAndView(professionalRecord, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final ProfessionalRecord professionalRecord, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("professionalRecord/edit");
		result.addObject("professionalRecord", professionalRecord);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "professionalRecord/ranger/edit.do");
		return result;
	}
	protected Boolean checkIsNotCorrectRanger(final ProfessionalRecord professionalRecord) {

		Ranger principal;
		principal = this.rangerService.findByPrincipal();

		final Collection<Authority> authorities;
		authorities = principal.getUserAccount().getAuthorities();
		Boolean isNotCorrectRanger = true;
		for (final Authority a : authorities)
			if (a.getAuthority().equals("RANGER")) {
				isNotCorrectRanger = false;
				break;
			}
		if (!isNotCorrectRanger) {
			Curriculum curriculum;
			curriculum = principal.getCurriculum();
			Collection<ProfessionalRecord> professionalRecords;
			try {
				professionalRecords = curriculum.getProfessionalRecords();
				isNotCorrectRanger = !professionalRecords.contains(professionalRecord);
			} catch (final Throwable oops) {
				isNotCorrectRanger = true;
			}

		}
		return isNotCorrectRanger;

	}
}
