
package controllers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AuditService;
import domain.Audit;

@Controller
@RequestMapping("/audit")
public class AuditController extends AbstractController {

	//Services

	@Autowired
	private AuditService	auditService;


	//Constructor

	public AuditController() {
		super();
	}

	//Listing

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int tripId) {
		final ModelAndView result;
		Collection<Audit> audits = new ArrayList<Audit>();
		Boolean readable;
		readable = false;
		audits = this.auditService.findAllFinalAudits(tripId);
		result = new ModelAndView("audit/list");
		result.addObject("audits", audits);
		result.addObject("readable", readable);
		result.addObject("requestURI", "audit/list.do");
		result.addObject("tripId", tripId);
		return result;
	}
}
