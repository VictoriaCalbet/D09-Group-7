
package controllers.actor;

import java.util.Collection;
import java.util.HashSet;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.FolderService;
import services.MessageService;
import controllers.AbstractController;
import domain.Actor;
import domain.Folder;
import domain.Message;

@Controller
@RequestMapping("/message/actor")
public class MessageActorController extends AbstractController {

	// Services
	@Autowired
	private MessageService	messageService;

	@Autowired
	private FolderService	folderService;

	@Autowired
	private ActorService	actorService;


	// Constructor
	public MessageActorController() {
		super();
	}

	// Listing
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int folderId) {
		final ModelAndView result;
		Collection<Message> messages = new HashSet<>();
		Actor principal;

		principal = this.actorService.findByPrincipal();

		result = new ModelAndView("message/list");

		try {
			final Folder folder = this.folderService.findOne(folderId);
			Assert.notNull(folder);
			Assert.isTrue(principal.getFolders().contains(folder));
			messages = this.messageService.findAllInFolder(folderId);
		} catch (final Throwable oops) {
			String messageError = "folder.commit.error";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();
			result.addObject("message", messageError);
		}

		result.addObject("messages", messages);
		result.addObject("principal", principal);
		result.addObject("requestURI", "message/actor/list.do");

		return result;
	}

	// Creation

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		Message message;

		message = this.messageService.create();
		result = this.createEditModelAndView(message);
		return result;
	}

	// Edition

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int messageId) {
		final ModelAndView result;
		Message message;
		Actor principal;

		principal = this.actorService.findByPrincipal();
		message = this.messageService.findOne(messageId);
		Assert.notNull(message);
		Assert.isTrue(principal.getId() == message.getSender().getId() || principal.getId() == message.getRecipient().getId());

		result = this.createEditModelAndView(message);
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid @ModelAttribute("messageEntity") final Message message, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(message);
		else
			try {
				if (message.getId() > 0)
					this.messageService.saveFromEdit(message);
				else
					this.messageService.saveFromCreate(message);
				result = new ModelAndView("redirect:../../folder/actor/list.do");
			} catch (final Throwable oops) {
				String messageError = "message.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createEditModelAndView(message, messageError);
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Message message, final BindingResult bindingResult) {
		ModelAndView result;

		try {
			this.messageService.delete(message);
			result = new ModelAndView("redirect:../../folder/actor/list.do");
		} catch (final Throwable oops) {
			String messageError = "messge.commit.error";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();
			result = this.createEditModelAndView(message, messageError);
		}

		return result;
	}

	// Ancillaty methods
	protected ModelAndView createEditModelAndView(final Message message) {
		ModelAndView result;

		result = this.createEditModelAndView(message, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Message messageEntity, final String message) {
		ModelAndView result;
		Actor principal;

		Collection<Actor> allPossibleRecipients;
		final Collection<String> priorities = new HashSet<>();
		Collection<Folder> foldersToMove = new HashSet<>();

		principal = this.actorService.findByPrincipal();

		allPossibleRecipients = this.actorService.findAll();
		allPossibleRecipients.remove(principal);

		priorities.add("HIGH");
		priorities.add("NEUTRAL");
		priorities.add("LOW");

		if (principal.getId() == messageEntity.getSender().getId() || principal.getId() == messageEntity.getRecipient().getId())
			foldersToMove = principal.getFolders();

		result = new ModelAndView("message/edit");
		result.addObject("messageEntity", messageEntity);
		result.addObject("message", message);
		result.addObject("recipients", allPossibleRecipients);
		result.addObject("priorities", priorities);
		result.addObject("principal", principal);
		result.addObject("foldersToMove", foldersToMove);
		result.addObject("requestURI", "message/actor/edit.do");

		return result;
	}

}
