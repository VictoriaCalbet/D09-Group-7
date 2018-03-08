
package services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.StageRepository;
import domain.Manager;
import domain.Stage;
import domain.Trip;

@Service
@Transactional
public class StageService {

	// Managed Repository
	@Autowired
	private StageRepository				stageRepository;

	// Supporting Services
	@Autowired
	private TripService					tripService;
	@Autowired
	private SystemConfigurationService	systemConfigurationService;
	@Autowired
	private ManagerService				managerService;
	@Autowired
	private MessageService				messageService;


	// Constructor

	public StageService() {

		super();
	}

	// Simple CRUD methods

	public Stage findOne(final int stageId) {
		final Stage stage = this.stageRepository.findOne(stageId);
		return stage;
	}

	public Stage create(final int idTrip) {

		final Stage s = new Stage();
		final Trip t = this.tripService.findOne(idTrip);
		Assert.isTrue(this.managerService.findByPrincipal().getTrips().contains(t), "message.error.trip.notManager");
		Assert.isTrue(t.getPublicationDate().after(new Date()), "message.error.stage.published");
		s.setTrip(t);
		return s;
	}
	public Stage save(final Stage s) {

		Assert.notNull(s);
		final Stage stage = this.stageRepository.save(s);

		final Trip t = this.tripService.findOne(s.getTrip().getId());
		//Comprobación que el manager es el logueado
		Assert.isTrue(t.getManager().equals(this.managerService.findByPrincipal()), "message.error.trip.notManager");
		final double vat = ((this.systemConfigurationService.findAll().iterator().next().getVatNumber()) / 100);
		final double total = this.tripService.sumByPriceStage(t.getId());
		final double price = total + (vat * total);
		t.setPrice(price);

		Boolean isSuspicious;
		Manager principal;
		principal = this.managerService.findByPrincipal();

		isSuspicious = principal.getIsSuspicious();
		isSuspicious = isSuspicious || this.messageService.checkSpam(s.getTitle()) || this.messageService.checkSpam(s.getDescription());
		principal.setIsSuspicious(isSuspicious);
		this.managerService.saveFromEdit(principal);

		this.tripService.update(t);
		return stage;
	}

	public void update(final Stage s) {

		//Comprueba que no sea nulo, que exista
		Assert.notNull(s);
		//Lo guardo
		this.stageRepository.save(s);
		final Trip t = this.tripService.findOne(s.getTrip().getId());
		//Comprobación que el manager es el logueado
		Assert.isTrue(t.getManager().equals(this.managerService.findByPrincipal()), "message.error.trip.notManager");
		Assert.isTrue(t.getPublicationDate().after(new Date()), "message.error.stage.published");
		final double vat = (this.systemConfigurationService.findAll().iterator().next().getVatNumber()) / 100;
		final double total = this.tripService.sumByPriceStage(t.getId());
		final double price = total + (vat * total);
		t.setPrice(price);

		Boolean isSuspicious;
		Manager principal;
		principal = this.managerService.findByPrincipal();

		isSuspicious = principal.getIsSuspicious();
		isSuspicious = isSuspicious || this.messageService.checkSpam(s.getTitle()) || this.messageService.checkSpam(s.getDescription());
		principal.setIsSuspicious(isSuspicious);
		this.managerService.saveFromEdit(principal);

		this.tripService.update(t);

	}

	public void delete(final int idStage) {

		//Comprueba que no sea nulo, que exista
		Assert.notNull(this.stageRepository.findOne(idStage));
		//Creo el objeto
		final Stage s = this.stageRepository.findOne(idStage);

		final Trip t = this.tripService.findOne(s.getTrip().getId());
		//Comprobación que el manager es el logueado
		Assert.isTrue(t.getManager().equals(this.managerService.findByPrincipal()), "message.error.trip.notManager");
		Assert.isTrue(t.getPublicationDate().after(new Date()), "message.error.stage.published");
		final double vat = (this.systemConfigurationService.findAll().iterator().next().getVatNumber()) / 100;
		final double total = this.tripService.sumByPriceStage(t.getId()) - s.getPrice();
		final double price = total + (vat * total);

		this.stageRepository.delete(s);
		t.setPrice(price);
		t.getStages().remove(s);
		this.tripService.update(t);

	}

	// Other business methods

}
