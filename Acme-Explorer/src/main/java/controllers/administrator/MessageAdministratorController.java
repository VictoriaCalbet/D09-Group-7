
package controllers.administrator;

import java.util.Collection;
import java.util.HashSet;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.MessageService;
import controllers.AbstractController;
import domain.Message;

@Controller
@RequestMapping("/message/administrator")
public class MessageAdministratorController extends AbstractController {

	// Services
	@Autowired
	private MessageService	messageService;


	// Constructor
	public MessageAdministratorController() {
		super();
	}

	// Creation
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Message message;

		message = this.messageService.create();
		result = this.createEditModelAndView(message);

		return result;
	}

	// Edition
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid @ModelAttribute("messageEntity") final Message message, final BindingResult binding) {
		ModelAndView result;
		boolean bindingError;

		if (binding.hasFieldErrors("recipient"))
			bindingError = binding.getErrorCount() > 1;
		else
			bindingError = binding.getErrorCount() > 0;

		if (bindingError)
			result = this.createEditModelAndView(message);
		else
			try {
				final String subject = message.getSubject();
				final String body = message.getBody();
				final String priority = message.getPriority();
				this.messageService.broadcastNotification(subject, body, priority);
				result = new ModelAndView("redirect:../../folder/actor/list.do");
			} catch (final Throwable oops) {
				String messageError = "message.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createEditModelAndView(message, messageError);
			}

		return result;
	}

	// Ancillary methods.
	protected ModelAndView createEditModelAndView(final Message message) {
		ModelAndView result;

		result = this.createEditModelAndView(message, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Message messageEntity, final String message) {
		ModelAndView result;
		final Collection<String> priorities = new HashSet<>();

		priorities.add("HIGH");
		priorities.add("NEUTRAL");
		priorities.add("LOW");

		result = new ModelAndView("message/edit");
		result.addObject("messageEntity", messageEntity);
		result.addObject("message", message);
		result.addObject("priorities", priorities);
		result.addObject("requestURI", "message/administrator/edit.do");

		return result;
	}
}
