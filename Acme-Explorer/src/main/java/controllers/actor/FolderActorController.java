
package controllers.actor;

import java.util.Collection;
import java.util.HashSet;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.FolderService;
import controllers.AbstractController;
import domain.Actor;
import domain.Folder;

@Controller
@RequestMapping("/folder/actor")
public class FolderActorController extends AbstractController {

	// Services
	@Autowired
	private FolderService	folderService;

	@Autowired
	private ActorService	actorService;


	// Constructor
	public FolderActorController() {
		super();
	}

	// Listing
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final Integer folderId) {
		final ModelAndView result = new ModelAndView("folder/list");
		Collection<Folder> folders = new HashSet<>();
		Actor principal;

		if (folderId == null) {
			principal = this.actorService.findByPrincipal();
			folders = this.folderService.findAllRootFoldersByActorId(principal.getId());
		} else
			try {
				folders = this.folderService.findAllChildrenByFolderId(folderId); // TODO Try-catch
			} catch (final Throwable oops) {
				String messageError = "folder.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result.addObject("message", messageError);
			}

		// result = new ModelAndView("folder/list");
		result.addObject("folders", folders);
		result.addObject("requestURI", "folder/actor/list.do");

		return result;
	}

	// Creation

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		Folder folder;

		folder = this.folderService.create();
		result = this.createEditModelAndView(folder);
		return result;
	}

	// Edition

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int folderId) {
		ModelAndView result;
		Folder folder;
		Actor principal;

		principal = this.actorService.findByPrincipal();

		folder = this.folderService.findOne(folderId);
		Assert.notNull(folder);
		Assert.isTrue(principal.getFolders().contains(folder));
		result = this.createEditModelAndView(folder);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Folder folder, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(folder);
		else
			try {
				if (folder.getId() > 0)
					this.folderService.saveFromEdit(folder);
				else
					this.folderService.saveFromCreate(folder);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				String messageError = "folder.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createEditModelAndView(folder, messageError);
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Folder folder, final BindingResult binding) {
		ModelAndView result;

		try {
			this.folderService.delete(folder);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			String messageError = "folder.commit.error";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();
			result = this.createEditModelAndView(folder, messageError);
		}

		return result;
	}

	// Ancillaty methods
	protected ModelAndView createEditModelAndView(final Folder folder) {
		ModelAndView result;

		result = this.createEditModelAndView(folder, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Folder folder, final String message) {
		ModelAndView result;
		final Collection<Folder> parent;
		Actor principal;

		principal = this.actorService.findByPrincipal();
		parent = principal.getFolders();
		parent.remove(folder);

		result = new ModelAndView("folder/edit");
		result.addObject("folder", folder);
		result.addObject("message", message);
		result.addObject("parent", parent);

		return result;
	}

}
