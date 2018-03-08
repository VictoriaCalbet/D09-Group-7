
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ExplorerService;
import controllers.AbstractController;
import domain.Explorer;

@Controller
@RequestMapping("/explorer/administrator")
public class ExplorerAdministratorController extends AbstractController {

	// Services
	@Autowired
	private ExplorerService	explorerService;


	// Constructor
	public ExplorerAdministratorController() {
		super();
	}

	// Listing
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Explorer> explorers;

		explorers = this.explorerService.findAll();

		result = new ModelAndView("explorer/list");
		result.addObject("explorers", explorers);
		result.addObject("requestURI", "explorer/administrator/list.do");

		return result;
	}
}
