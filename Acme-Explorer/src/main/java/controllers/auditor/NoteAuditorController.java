// Tag Controller

package controllers.auditor;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AuditorService;
import services.NoteService;
import services.TripService;
import controllers.AbstractController;
import domain.Auditor;
import domain.Note;
import domain.Trip;

@Controller
@RequestMapping("/note/auditor")
public class NoteAuditorController extends AbstractController {

	//Services
	@Autowired
	private NoteService		noteService;
	@Autowired
	private TripService		tripService;

	@Autowired
	private AuditorService	auditorService;


	//Constructor
	public NoteAuditorController() {

		super();
	}

	//Listing as a non-admin actor
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		Boolean noTrips = this.tripService.findTripPublished().isEmpty();
		final Auditor auditor = this.auditorService.findByPrincipal();
		final Collection<Note> notes = auditor.getNotes();
		result = new ModelAndView("note/auditor/list");
		result.addObject("notes", notes);
		result.addObject("requestURI", "note/auditor/list.do");
		result.addObject("noTrips", noTrips);
		return result;
	}

	//Create a note

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {

		ModelAndView result;
		Note note;

		note = this.noteService.create();
		result = this.createEditModelAndView(note);

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

				this.noteService.saveFromCreate(note);

				result = new ModelAndView("redirect:list.do");
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
				
				Collection<Trip> trips = this.tripService.findTripPublished();
				
				result = new ModelAndView("note/auditor/edit");
				result.addObject("note", note);
				result.addObject("message", message);
				result.addObject("trips",trips);

				return result;
			}

}
