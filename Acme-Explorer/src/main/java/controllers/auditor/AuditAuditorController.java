
package controllers.auditor;

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

import services.AuditService;
import services.AuditorService;
import services.TripService;
import controllers.AbstractController;
import domain.Audit;
import domain.Auditor;
import domain.Trip;

@Controller
@RequestMapping("/audit/auditor")
public class AuditAuditorController extends AbstractController {

	//Services
	@Autowired
	private AuditService	auditService;
	@Autowired
	private AuditorService	auditorService;
	@Autowired
	private TripService		tripService;


	//Constructor

	public AuditAuditorController() {
		super();
	}

	//Listing

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		Collection<Audit> audits;
		Auditor principal;

		principal = this.auditorService.findByPrincipal();
		audits = principal.getAudits();

		Boolean readable;
		readable = true;

		result = new ModelAndView("audit/auditor/list");
		result.addObject("principal", principal);
		result.addObject("readable", readable);
		result.addObject("audits", audits);
		result.addObject("requestURI", "audit/auditor/list.do");
		return result;

	}
	//Creating 
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Audit audit;

		audit = this.auditService.create();
		result = this.createEditModelAndView(audit);

		return result;

	}

	//EDITIONS
	//Editing
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int auditId) {
		final ModelAndView result;
		Audit audit;
		final Auditor principal = this.auditorService.findByPrincipal();
		audit = this.auditService.findOne(auditId);

		Assert.isTrue(principal.getAudits().contains(audit), "message.error.audit.auditor");
		Assert.isTrue(audit.getIsDraft() == true, "message.error.audit.draft");

		Assert.notNull(audit);

		result = this.createEditModelAndView(audit);
		result.addObject(auditId);

		return result;
	}

	//Saving //TODO
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Audit audit, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(audit);
		else
			try {
				if (audit.getId() > 0)
					this.auditService.saveFromEdit(audit);
				else
					this.auditService.saveFromCreate(audit);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				String messageError = "audit.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createEditModelAndView(audit, messageError);
			}

		return result;
	}

	//Deleting//TODO
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Audit audit, final BindingResult binding) {
		ModelAndView result;

		final Auditor principal = this.auditorService.findByPrincipal();
		Assert.isTrue(principal.getAudits().contains(audit));
		Assert.isTrue(audit.getIsDraft() == true, "message.error.audit.draft");

		try {

			this.auditService.delete(audit);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			String messageError = "audit.commit.error";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();
			result = this.createEditModelAndView(audit, messageError);
		}

		return result;
	}

	//Ancillary methods

	protected ModelAndView createEditModelAndView(final Audit audit) {
		ModelAndView result;

		result = this.createEditModelAndView(audit, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Audit audit, final String messageCode) {
		ModelAndView result;
		Collection<Trip> trips;
		final Auditor principal = this.auditorService.findByPrincipal();
		trips = this.tripService.findAllPublishedAndNotStarted();

		result = new ModelAndView("audit/auditor/edit");
		result.addObject("audit", audit);
		result.addObject("principal", principal);
		result.addObject("message", messageCode);
		result.addObject("trips", trips);
		return result;
	}
}
