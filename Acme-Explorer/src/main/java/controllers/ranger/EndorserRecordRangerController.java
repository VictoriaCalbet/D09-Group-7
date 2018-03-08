
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
import services.EndorserRecordService;
import services.RangerService;
import controllers.AbstractController;
import domain.Curriculum;
import domain.EndorserRecord;
import domain.Ranger;

@Controller
@RequestMapping("/endorserRecord/ranger")
public class EndorserRecordRangerController extends AbstractController {

	@Autowired
	EndorserRecordService	endorserRecordService;
	@Autowired
	RangerService			rangerService;


	@RequestMapping(value = "create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		EndorserRecord endorserRecord;
		endorserRecord = this.endorserRecordService.create();
		result = this.createEditModelAndView(endorserRecord);
		return result;

	}
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int endorserRecordId) {
		final ModelAndView result;
		EndorserRecord endorserRecord;
		endorserRecord = this.endorserRecordService.findOne(endorserRecordId);
		if (endorserRecord != null) {
			if (this.checkIsNotCorrectRanger(endorserRecord))
				result = new ModelAndView("misc/forbidden");
			else
				result = this.createEditModelAndView(endorserRecord);
		} else
			result = new ModelAndView("misc/forbidden");
		return result;

	}
	@RequestMapping(value = "edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final EndorserRecord endorserRecord, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors())
			result = this.createEditModelAndView(endorserRecord);
		else
			try {
				this.endorserRecordService.save(endorserRecord);
				result = new ModelAndView("redirect:/curriculum/display.do");
			} catch (final Throwable oops) {
				String error = "endorserRecord.commit.error";
				if (oops.getMessage().contains("message.error"))
					error = oops.getMessage();
				result = this.createEditModelAndView(endorserRecord, error);
			}
		return result;
	}
	@RequestMapping(value = "edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final EndorserRecord endorserRecord, final BindingResult binding) {
		ModelAndView result;
		try {
			this.endorserRecordService.delete(endorserRecord);
			result = new ModelAndView("redirect:/curriculum/display.do");
		} catch (final Throwable oops) {
			String error = "professioanalRecord.commit.error";
			if (oops.getMessage().contains("message.error"))
				error = oops.getMessage();
			result = this.createEditModelAndView(endorserRecord, error);
		}
		return result;
	}
	protected ModelAndView createEditModelAndView(final EndorserRecord endorserRecord) {
		ModelAndView result;
		result = this.createEditModelAndView(endorserRecord, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final EndorserRecord endorserRecord, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("endorserRecord/edit");
		result.addObject("endorserRecord", endorserRecord);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "endorserRecord/ranger/edit.do");
		return result;
	}

	protected Boolean checkIsNotCorrectRanger(final EndorserRecord endorserRecord) {

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
			Collection<EndorserRecord> endorserRecords;
			try {
				endorserRecords = curriculum.getEndorserRecords();
				isNotCorrectRanger = !endorserRecords.contains(endorserRecord);
			} catch (final Throwable oops) {
				isNotCorrectRanger = true;
			}
		}
		return isNotCorrectRanger;

	}

}
