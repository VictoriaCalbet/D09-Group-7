
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
import services.MiscellaneousRecordService;
import services.RangerService;
import controllers.AbstractController;
import domain.Curriculum;
import domain.MiscellaneousRecord;
import domain.Ranger;

@Controller
@RequestMapping("/miscellaneousRecord/ranger")
public class MiscellaneousRecordRangerController extends AbstractController {

	@Autowired
	MiscellaneousRecordService	miscellaneousRecordService;
	@Autowired
	RangerService				rangerService;


	@RequestMapping(value = "create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		MiscellaneousRecord miscellaneousRecord;
		miscellaneousRecord = this.miscellaneousRecordService.create();
		result = this.createEditModelAndView(miscellaneousRecord);
		return result;

	}
	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int miscellaneousRecordId) {
		final ModelAndView result;
		MiscellaneousRecord miscellaneousRecord;
		miscellaneousRecord = this.miscellaneousRecordService.findOne(miscellaneousRecordId);
		if (miscellaneousRecord != null) {
			if (this.checkIsNotCorrectRanger(miscellaneousRecord))
				result = new ModelAndView("misc/forbidden");
			else
				result = this.createEditModelAndView(miscellaneousRecord);
		} else
			result = new ModelAndView("misc/forbidden");
		return result;

	}
	@RequestMapping(value = "edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final MiscellaneousRecord miscellaneousRecord, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors())
			result = this.createEditModelAndView(miscellaneousRecord);
		else
			try {
				this.miscellaneousRecordService.save(miscellaneousRecord);
				result = new ModelAndView("redirect:/curriculum/display.do");
			} catch (final Throwable oops) {
				String error = "miscellaneousRecord.commit.error";
				if (oops.getMessage().contains("message.error"))
					error = oops.getMessage();
				result = this.createEditModelAndView(miscellaneousRecord, error);
			}
		return result;
	}
	@RequestMapping(value = "edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final MiscellaneousRecord miscellaneousRecord, final BindingResult binding) {
		ModelAndView result;
		try {
			this.miscellaneousRecordService.delete(miscellaneousRecord);
			result = new ModelAndView("redirect:/curriculum/display.do");
		} catch (final Throwable oops) {
			String error = "professioanalRecord.commit.error";
			if (oops.getMessage().contains("message.error"))
				error = oops.getMessage();
			result = this.createEditModelAndView(miscellaneousRecord, error);
		}
		return result;
	}
	protected ModelAndView createEditModelAndView(final MiscellaneousRecord miscellaneousRecord) {
		ModelAndView result;
		result = this.createEditModelAndView(miscellaneousRecord, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final MiscellaneousRecord miscellaneousRecord, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("miscellaneousRecord/edit");
		result.addObject("miscellaneousRecord", miscellaneousRecord);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "miscellaneousRecord/ranger/edit.do");
		return result;
	}
	protected Boolean checkIsNotCorrectRanger(final MiscellaneousRecord miscellaneousRecord) {

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
			Collection<MiscellaneousRecord> miscellaneousRecords;

			try {
				miscellaneousRecords = curriculum.getMiscellaneousRecords();
				isNotCorrectRanger = !miscellaneousRecords.contains(miscellaneousRecord);
			} catch (final Throwable oops) {
				isNotCorrectRanger = true;
			}

		}
		return isNotCorrectRanger;

	}

}
