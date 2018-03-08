
package controllers.explorer;

import java.util.ArrayList;
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

import services.ExplorerService;
import services.SurvivalClassService;
import services.TripService;
import controllers.AbstractController;
import domain.Application;
import domain.Explorer;
import domain.SurvivalClass;
import domain.Trip;

@Controller
@RequestMapping("/survivalClass/explorer")
public class SurvivalClassExplorerController extends AbstractController {

	//Services
	@Autowired
	private SurvivalClassService	survivalClassService;

	@Autowired
	private ExplorerService			explorerService;

	@Autowired
	private TripService				tripService;


	//Constructor

	public SurvivalClassExplorerController() {
		super();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int tripId) {
		final ModelAndView result;
		Collection<SurvivalClass> survivalClasses = new ArrayList<SurvivalClass>();
		final Explorer principal = this.explorerService.findByPrincipal();
		final Trip trip = this.tripService.findOne(tripId);
		//Trips que serán mostrados en la tabla
		survivalClasses = trip.getSurvivalClasses();

		//Aquí se crearán una serie de collections con el fin de conseguir la lista de 
		//clases de supervivencia a las que el explorer registrado puede asistir, y pasársela a
		//la vista para que trate cada una de ellas de forma que aparezca el enlace
		//de asistencia a dicha clase en el caso de que sea posible, que no sea "asistible"
		//o que ya se haya asistido
		final Collection<Trip> tripsExplorerApplicationAccepted = new ArrayList<Trip>();
		final Collection<Trip> tripsExplorerApplicationAcceptedNotEnded = new ArrayList<Trip>();
		final Collection<Trip> tripsNotEnded = this.tripService.findTripsNotEnded();
		Collection<Application> applicationsFiltered = new ArrayList<Application>();
		final Collection<SurvivalClass> tripSurvivalClasses = new ArrayList<SurvivalClass>();
		final Collection<Integer> enrolableSurvivalClasses = new ArrayList<Integer>();

		//explorer-> applications accepted-->trips not over--> survivalClasses
		//Aplicaciones de explorer
		applicationsFiltered = principal.getApplications();
		//Trips de las aplicaciones de explorer aceptadas
		for (final Application a : applicationsFiltered)
			if (a.getStatus().contains("ACCEPTED"))
				tripsExplorerApplicationAccepted.add(a.getTrip());

		//Trips que aún no han terminado a los que pertenecen las aplicaciones de explorer aceptadas
		for (final Trip t : tripsExplorerApplicationAccepted)
			if (tripsNotEnded.contains(t))
				tripsExplorerApplicationAcceptedNotEnded.add(t);

		for (final Trip t : tripsExplorerApplicationAcceptedNotEnded)
			tripSurvivalClasses.addAll(t.getSurvivalClasses());

		for (final SurvivalClass sc : tripSurvivalClasses)
			if (survivalClasses.contains(sc))
				enrolableSurvivalClasses.add(sc.getId());

		result = new ModelAndView("survivalClass/explorer/list");//tiles
		result.addObject("principal", principal);
		result.addObject("survivalClasses", survivalClasses);
		result.addObject("requestURI", "survivalClass/list.do");//vista+.do
		result.addObject("tripId", tripId);
		result.addObject("enrolableSurvivalClasses", enrolableSurvivalClasses);

		return result;

	}

	@RequestMapping(value = "/enrolAssure", method = RequestMethod.GET)
	public ModelAndView enrolAssure(@RequestParam final int survivalClassId) {
		final ModelAndView result;
		SurvivalClass sc;

		sc = this.survivalClassService.findOne(survivalClassId);
		Assert.notNull(sc);

		result = this.createEditModelAndView(sc);
		result.addObject(survivalClassId);

		return result;
	}

	@RequestMapping(value = "/enrol", method = RequestMethod.POST, params = "save")
	public ModelAndView enrol(@Valid final SurvivalClass sc, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(sc);
		else
			try {
				this.survivalClassService.enrolASurvivalClass(sc.getId());
				result = new ModelAndView("redirect:/trip/list.do");

			} catch (final Throwable oops) {

				String messageError = "survivalClass.commit.error";
				if (oops.getMessage().contains("message.error"))
					messageError = oops.getMessage();
				result = this.createEditModelAndView(sc, messageError);

			}

		return result;
	}
	//Ancillary methods
	protected final ModelAndView createEditModelAndView(final SurvivalClass sc) {
		ModelAndView result;

		result = this.createEditModelAndView(sc, null);

		return result;
	}

	protected final ModelAndView createEditModelAndView(final SurvivalClass survivalClass, final String messageCode) {
		ModelAndView result;
		result = new ModelAndView("survivalClass/explorer/enrol");
		result.addObject("survivalClass", survivalClass);

		result.addObject("message", messageCode);
		result.addObject("requestURI", "survivalClass/explorer/enrol.do");
		return result;
	}

	protected ModelAndView enrolModelAndView(final SurvivalClass survivalClass) {
		ModelAndView result;

		result = this.enrolModelAndView(survivalClass, null);

		return result;
	}

	protected ModelAndView enrolModelAndView(final SurvivalClass survivalClass, final String message) {
		ModelAndView result;
		final Explorer principal = this.explorerService.findByPrincipal();
		final Collection<Explorer> explorers = survivalClass.getExplorers();
		explorers.add(principal);
		survivalClass.setExplorers(explorers);

		result = new ModelAndView("survivalClass/explorer/enrol");
		result.addObject("survivalClass", survivalClass);
		result.addObject("message", message);
		result.addObject("explorers", explorers);
		return result;
	}
}
