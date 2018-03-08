
package controllers.ranger;

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
import services.PersonalRecordService;
import services.RangerService;
import controllers.AbstractController;
import domain.Curriculum;
import domain.PersonalRecord;
import domain.Ranger;

@Controller
@RequestMapping("/personalRecord/ranger")
public class PersonalRecordRangerController extends AbstractController {

	@Autowired
	PersonalRecordService	personalRecordService;
	@Autowired
	RangerService			rangerService;


	@RequestMapping(value = "create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		PersonalRecord personalRecord;
		personalRecord = this.personalRecordService.create();
		result = this.createEditModelAndView(personalRecord);
		return result;

	}
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int personalRecordId) {
		final ModelAndView result;
		PersonalRecord personalRecord;
		personalRecord = this.personalRecordService.findOne(personalRecordId);
		if (personalRecord != null) {
			Assert.notNull(personalRecord);
			if (this.checkIsNotCorrectRanger(personalRecord))
				result = new ModelAndView("misc/forbidden");
			else
				result = this.createEditModelAndView(personalRecord);
		} else
			result = new ModelAndView("misc/forbidden");
		return result;

	}
	@RequestMapping(value = "edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final PersonalRecord personalRecord, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors())
			result = this.createEditModelAndView(personalRecord);
		else
			try {
				this.personalRecordService.save(personalRecord);
				result = new ModelAndView("redirect:/curriculum/display.do");
			} catch (final Throwable oops) {
				String error = "personalRecord.commit.error";
				if (oops.getMessage().contains("message.error"))
					error = oops.getMessage();
				result = this.createEditModelAndView(personalRecord, error);
			}
		return result;
	}
	protected ModelAndView createEditModelAndView(final PersonalRecord personalRecord) {
		ModelAndView result;
		result = this.createEditModelAndView(personalRecord, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final PersonalRecord personalRecord, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("personalRecord/edit");
		result.addObject("personalRecord", personalRecord);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "personalRecord/ranger/edit.do");
		if (personalRecord.getName() == null)
			result.addObject("fromWhere", false);
		else
			result.addObject("fromWhere", true);
		return result;
	}
	protected Boolean checkIsNotCorrectRanger(final PersonalRecord personalRecord) {

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
			PersonalRecord personalRecord2;
			try {
				personalRecord2 = curriculum.getPersonalRecord();
				isNotCorrectRanger = !personalRecord.equals(personalRecord2);
			} catch (final Throwable oops) {
				isNotCorrectRanger = true;
			}
		}
		return isNotCorrectRanger;

	}

}
