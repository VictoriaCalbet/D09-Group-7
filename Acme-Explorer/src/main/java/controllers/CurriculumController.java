
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CurriculumService;
import services.RangerService;
import domain.Curriculum;
import domain.EducationRecord;
import domain.EndorserRecord;
import domain.MiscellaneousRecord;
import domain.ProfessionalRecord;
import domain.Ranger;

@Controller
@RequestMapping("/curriculum")
public class CurriculumController extends AbstractController {

	@Autowired
	CurriculumService	curriculumService;
	@Autowired
	RangerService		rangerService;


	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam(required = false) final Integer rangerId) {

		ModelAndView result;
		Ranger ranger;
		Integer byPrincipal = 0;
		if (rangerId == null) {
			byPrincipal = 1;
			ranger = this.rangerService.findByPrincipal();
			result = this.displayModel(ranger, byPrincipal);
		} else {
			ranger = this.rangerService.findOne(rangerId);
			//Esto comprueba que el ranger existe
			if (ranger == null)
				result = new ModelAndView("misc/forbidden");
			else
				result = this.displayModel(ranger, byPrincipal);
		}

		return result;
	}

	public ModelAndView displayModel(final Ranger principal, final Integer edit) {
		ModelAndView result;
		Curriculum curriculum;
		curriculum = principal.getCurriculum();
		result = new ModelAndView("curriculum/display");

		if (curriculum != null) {
			Collection<EndorserRecord> endorserRecords;
			endorserRecords = curriculum.getEndorserRecords();
			Collection<ProfessionalRecord> professionalRecords;
			professionalRecords = curriculum.getProfessionalRecords();
			Collection<EducationRecord> educationRecords;
			educationRecords = curriculum.getEducationRecords();
			Collection<MiscellaneousRecord> miscellaneousRecords;
			miscellaneousRecords = curriculum.getMiscellaneousRecords();
			result.addObject("endorserRecords", endorserRecords);
			result.addObject("professionalRecords", professionalRecords);
			result.addObject("educationRecords", educationRecords);
			result.addObject("miscellaneousRecords", miscellaneousRecords);

			result.addObject("curriculum", curriculum);
			result.addObject("canEdit", edit);
			result.addObject("requestURI", "curriculum/display.do");
		} else if (edit.equals(1))
			result = new ModelAndView("redirect:/personalRecord/ranger/create.do");

		return result;

	}

}
