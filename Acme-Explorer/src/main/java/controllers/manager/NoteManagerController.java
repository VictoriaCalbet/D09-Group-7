// Tag Controller

package controllers.manager;

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

import services.ManagerService;
import services.NoteService;
import services.TripService;
import controllers.AbstractController;
import domain.Manager;
import domain.Note;
import domain.Trip;

@Controller
@RequestMapping("/note/manager")
public class NoteManagerController extends AbstractController {

	//Services
	@Autowired
	private NoteService		noteService;

	//Services
	@Autowired
	private TripService		tripService;

	//Services
	@Autowired
	private ManagerService	managerService;


	//Constructor
	public NoteManagerController() {

		super();
	}

	//Listing
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final Integer tripId) {

		Assert.notNull(tripId, "note.commit.error");

		ModelAndView result;

		Assert.notNull(this.tripService.findOne(tripId));

		final Collection<Note> notes = this.tripService.findOne(tripId).getNotes();
		result = new ModelAndView("note/manager/list");
		result.addObject("notes", notes);
		result.addObject("requestURI", "note/manager/list.do");
		//result.addObject("tripId", tripId);

		return result;
	}

	//Edit a note

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int noteId) {

		ModelAndView result;
		try {
			//Checks if the manager who's trying to reply is actually the one who should be replying
			final Note note = this.noteService.findOne(noteId);
			Assert.notNull(note, "message.error.note.null");
			final Manager m = this.managerService.findByPrincipal();
			Assert.isTrue(m.equals(note.getTrip().getManager()), "message.error.note.notTheCorrectManager");
			result = this.createEditModelAndView(note);

		} catch (final Throwable oops) {
			String messageError = "message.error.note.notTheCorrectManager";
			if (oops.getMessage().contains("message.error"))
				messageError = oops.getMessage();
			result = new ModelAndView("trip/list");
			final Collection<Trip> trips = this.tripService.findAllPublishedAndNotStarted();
			result.addObject("trips", trips);
			result.addObject("requestURI", "trip/list.do");
			result.addObject("message", messageError);
		}

		return result;
	}

	//Save a note
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Note note, final BindingResult binding) {

		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(note);
		else
			try {

				this.noteService.saveFromReply(note);

				result = new ModelAndView("redirect:/trip/manager/list.do");
			} catch (final Throwable oops) {

				String messageError = "note.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createEditModelAndView(note, messageError);

			}
		return result;
	}

	// Ancillaty methods
	protected ModelAndView createEditModelAndView(final Note note) {

		ModelAndView result;

		result = this.createEditModelAndView(note, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Note note, final String message) {
		ModelAndView result;

		result = new ModelAndView("note/manager/edit");
		result.addObject("note", note);
		result.addObject("message", message);

		return result;
	}

}
