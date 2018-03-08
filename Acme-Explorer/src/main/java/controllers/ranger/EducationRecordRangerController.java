
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
import services.EducationRecordService;
import services.RangerService;
import controllers.AbstractController;
import domain.Curriculum;
import domain.EducationRecord;
import domain.Ranger;

@Controller
@RequestMapping("/educationRecord/ranger")
public class EducationRecordRangerController extends AbstractController {

	@Autowired
	EducationRecordService	educationRecordService;
	@Autowired
	RangerService			rangerService;


	@RequestMapping(value = "create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		EducationRecord educationRecord;
		educationRecord = this.educationRecordService.create();
		result = this.createEditModelAndView(educationRecord);
		return result;

	}
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int educationRecordId) {
		final ModelAndView result;
		EducationRecord educationRecord;
		educationRecord = this.educationRecordService.findOne(educationRecordId);
		if (educationRecord != null) {
			if (this.checkIsNotCorrectRanger(educationRecord))
				result = new ModelAndView("misc/forbidden");
			else
				result = this.createEditModelAndView(educationRecord);
		} else
			result = new ModelAndView("misc/forbidden");
		return result;

	}
	@RequestMapping(value = "edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final EducationRecord educationRecord, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors())
			result = this.createEditModelAndView(educationRecord);
		else
			try {
				this.educationRecordService.save(educationRecord);
				result = new ModelAndView("redirect:/curriculum/display.do");
			} catch (final Throwable oops) {
				String error = "educationRecord.commit.error";
				if (oops.getMessage().contains("message.error"))
					error = oops.getMessage();
				result = this.createEditModelAndView(educationRecord, error);
			}
		return result;
	}
	@RequestMapping(value = "edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final EducationRecord educationRecord, final BindingResult binding) {
		ModelAndView result;
		try {
			this.educationRecordService.delete(educationRecord);
			result = new ModelAndView("redirect:/curriculum/display.do");
		} catch (final Throwable oops) {
			String error = "professioanalRecord.commit.error";
			if (oops.getMessage().contains("message.error"))
				error = oops.getMessage();
			result = this.createEditModelAndView(educationRecord, error);
		}
		return result;
	}
	protected ModelAndView createEditModelAndView(final EducationRecord educationRecord) {
		ModelAndView result;
		result = this.createEditModelAndView(educationRecord, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final EducationRecord educationRecord, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("educationRecord/edit");
		result.addObject("educationRecord", educationRecord);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "educationRecord/ranger/edit.do");
		return result;
	}

	protected Boolean checkIsNotCorrectRanger(final EducationRecord educationRecord) {

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
			Collection<EducationRecord> educationRecords;
			try {
				educationRecords = curriculum.getEducationRecords();
				isNotCorrectRanger = !educationRecords.contains(educationRecord);
			} catch (final Throwable oops) {
				isNotCorrectRanger = true;
			}

		}
		return isNotCorrectRanger;

	}

}
