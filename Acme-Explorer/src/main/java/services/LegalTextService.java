
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.LegalTextRepository;
import domain.Administrator;
import domain.LegalText;
import domain.Trip;

@Service
@Transactional
public class LegalTextService {

	// Managed Repository
	@Autowired
	private LegalTextRepository	legalTextRepository;

	// Supporting Services
	@Autowired
	private MessageService		messageService;
	@Autowired
	private AdministratorService	administratorService;


	// Constructor

	public LegalTextService() {
		super();
	}

	// Simple CRUD methods

	public LegalText create() {
		Administrator admin = this.administratorService.findByPrincipal();
		Assert.notNull(admin,"message.error.legaltext.login");
		final LegalText tl = new LegalText();
		tl.setIsDraft(true);
		final Collection<Trip> trips = new ArrayList<Trip>();
		tl.setTrips(trips);
		//This is placed here so that the create method doesn't give a null attribute to the controller. However, the moment of registration is updated in the save
		//method, so that the date which persists is the one at the moment of saving the legal text.
		tl.setMomentRegistered(new Date(System.currentTimeMillis() - 1));
		
		return tl;
	}

	public Collection<LegalText> findAll() {

		return this.legalTextRepository.findAll();
	}

	public LegalText findOne(final LegalText tl) {

		return this.legalTextRepository.findOne(tl.getId());
	}

	public LegalText findOneById(final int tl) {

		return this.legalTextRepository.findOne(tl);
	}

	public LegalText save(final LegalText tl) {
		Administrator admin = this.administratorService.findByPrincipal();
		Boolean isSuspicious;
		
		Assert.notNull(admin,"message.error.legaltext.login");
		Assert.notNull(tl,"message.error.legaltext.null");
		Assert.notNull(tl.getTitle(),"message.error.legaltext.title");
		Assert.notNull(tl.getBody(),"message.error.legaltext.body");
		Assert.notNull(tl.getNumberLaw(),"message.error.legaltext.numberlaw");
		
		isSuspicious = admin.getIsSuspicious();
		isSuspicious = isSuspicious || this.messageService.checkSpam(tl.getTitle()) || this.messageService.checkSpam(tl.getBody()) || this.messageService.checkSpam(tl.getNumberLaw());
		admin.setIsSuspicious(isSuspicious);

		this.administratorService.saveFromEdit(admin);
		
		tl.setMomentRegistered(new Date(System.currentTimeMillis() - 1));

		final LegalText savedLt = this.legalTextRepository.save(tl);

		return savedLt;

	}

	public LegalText edit(final LegalText tl) {
		Administrator admin = this.administratorService.findByPrincipal();
		Boolean isSuspicious;
		
		Assert.notNull(admin,"message.error.legaltext.login");
		Assert.isTrue(tl.getId() > 0);
		LegalText legalT = this.findOne(tl);
		Assert.isTrue(legalT.getIsDraft()==true,"message.error.legaltext.draft");
		Assert.notNull(tl.getTitle(),"message.error.legaltext.title");
		Assert.notNull(tl.getBody(),"message.error.legaltext.body");
		Assert.notNull(tl.getNumberLaw(),"message.error.legaltext.numberlaw");
		
		isSuspicious = admin.getIsSuspicious();
		isSuspicious = isSuspicious || this.messageService.checkSpam(tl.getTitle()) || this.messageService.checkSpam(tl.getBody()) || this.messageService.checkSpam(tl.getNumberLaw());
		admin.setIsSuspicious(isSuspicious);
		
		this.administratorService.saveFromEdit(admin);
		
		final LegalText savedLt = this.legalTextRepository.save(tl);

		return savedLt;
	}

	public void delete(final LegalText tl) {
		Administrator admin = this.administratorService.findByPrincipal();
		Assert.notNull(admin,"message.error.legaltext.login");
		
		LegalText legalT = this.findOne(tl);
		Assert.isTrue(legalT.getIsDraft()==true,"message.error.legaltext.draft");
		this.legalTextRepository.delete(tl);
	}

	// Other business methods

	public Collection<LegalText> getFinalLegalTexts() {

		return this.legalTextRepository.findFinalLegalTexts();
	}

	public Integer getNumberOfReferences(final LegalText tl) {

		return tl.getTrips().size();
	}

	//C-14.6.11 A table with the number of times that each legal texts been referenced
	public Collection<LegalText> legalTextName() {
		return this.legalTextRepository.legalTextName();
	}

}
