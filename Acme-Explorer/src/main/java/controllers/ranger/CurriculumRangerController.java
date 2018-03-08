
package controllers.ranger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.CurriculumService;
import services.RangerService;
import domain.Curriculum;

@Controller
@RequestMapping("/curriculum/ranger")
public class CurriculumRangerController {

	@Autowired
	CurriculumService	curriculumService;
	@Autowired
	RangerService		rangerService;


	@RequestMapping(value = "delete", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Curriculum curriculum, final BindingResult binding) {
		ModelAndView result;
		try {
			this.curriculumService.delete(curriculum);
			result = new ModelAndView("redirect:/");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/curriculum/display.do");
			result.addObject("message", "curriculum.commit.error");
		}
		return result;
	}

}
