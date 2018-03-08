
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ContactEmergencyRepository;
import domain.ContactEmergency;
import domain.Explorer;

@Service
@Transactional
public class ContactEmergencyService {

	// Managed Repository
	@Autowired
	private ContactEmergencyRepository	contactEmergencyRepository;

	// Supporting Services
	@Autowired
	private ExplorerService				explorerService;

	@Autowired
	private MessageService				messageService;

	@Autowired
	private ActorService				actorService;


	// Constructor

	public ContactEmergencyService() {
		super();
	}

	// Simple CRUD methods
	public ContactEmergency create() {

		Explorer principal;
		principal = this.explorerService.findByPrincipal();
		Assert.notNull(principal, "message.error.contactEmergency.login");
		Assert.isTrue(this.explorerService.checkExplorer(principal), "message.error.contactEmergency.authority");

		ContactEmergency result;

		result = new ContactEmergency();

		return result;
	}

	private ContactEmergency save(final ContactEmergency contactEmergency) {
		Assert.notNull(contactEmergency, "message.error.contactEmergency.null");

		ContactEmergency result;

		result = this.contactEmergencyRepository.save(contactEmergency);

		return result;
	}

	public ContactEmergency saveFromCreate(final ContactEmergency contactEmergency) {
		Assert.notNull(contactEmergency, "message.error.contactEmergency.null");

		ContactEmergency result;
		Collection<ContactEmergency> explorerContactEmergencies;
		Boolean isSuspicious;

		Explorer principal;
		principal = this.explorerService.findByPrincipal();
		Assert.notNull(principal, "message.error.contactEmergency.login");
		Assert.isTrue(this.explorerService.checkExplorer(principal), "message.error.contactEmergency.authority");

		// Add CC if PN phone
		if (contactEmergency.getPhone().matches("^(\\d{4,})$")) {
			final String updatedPhone = this.actorService.updatePhoneFromString(contactEmergency.getPhone());
			contactEmergency.setPhone(updatedPhone);

		}

		result = this.save(contactEmergency);

		explorerContactEmergencies = principal.getContactEmergencies();
		Assert.notNull(explorerContactEmergencies, "message.error.contactEmergency.explorer.list");

		explorerContactEmergencies.add(result);
		principal.setContactEmergencies(explorerContactEmergencies);

		isSuspicious = principal.getIsSuspicious();
		isSuspicious = isSuspicious || this.messageService.checkSpam(result.getName()) || this.messageService.checkSpam(result.getEmail()) || this.messageService.checkSpam(result.getPhone());
		principal.setIsSuspicious(isSuspicious);

		this.explorerService.saveFromEdit(principal);

		return result;
	}

	public ContactEmergency saveFromEdit(final ContactEmergency contactEmergency) {
		Assert.notNull(contactEmergency, "message.error.contactEmergency.null");

		ContactEmergency result;
		Collection<ContactEmergency> explorerContactEmergencies;
		Boolean isSuspicious;

		Explorer principal;
		principal = this.explorerService.findByPrincipal();
		Assert.notNull(principal, "message.error.contactEmergency.login");
		Assert.isTrue(this.explorerService.checkExplorer(principal), "message.error.contactEmergency.authority");

		Assert.isTrue(principal.getContactEmergencies().contains(contactEmergency), "message.error.contactEmergency.explorer.owner");

		// Add CC if PN phone
		if (contactEmergency.getPhone().matches("^(\\d{4,})$")) {
			final String updatedPhone = this.actorService.updatePhoneFromString(contactEmergency.getPhone());
			contactEmergency.setPhone(updatedPhone);

		}

		result = this.save(contactEmergency);

		explorerContactEmergencies = principal.getContactEmergencies();
		Assert.notNull(explorerContactEmergencies, "message.error.contactEmergency.explorer.list");

		for (final ContactEmergency c : explorerContactEmergencies)
			if (c.getId() == result.getId()) {
				explorerContactEmergencies.remove(c);
				break;
			}

		explorerContactEmergencies.add(result);
		principal.setContactEmergencies(explorerContactEmergencies);

		isSuspicious = principal.getIsSuspicious();
		isSuspicious = isSuspicious || this.messageService.checkSpam(result.getName()) || this.messageService.checkSpam(result.getEmail()) || this.messageService.checkSpam(result.getPhone());
		principal.setIsSuspicious(isSuspicious);

		this.explorerService.saveFromEdit(principal);

		return result;
	}

	public void delete(final ContactEmergency contactEmergency) {
		Assert.notNull(contactEmergency, "message.error.contactEmergency.null");

		Collection<ContactEmergency> explorerContactEmergencies;

		Explorer principal;
		principal = this.explorerService.findByPrincipal();
		Assert.notNull(principal, "message.error.contactEmergency.login");
		Assert.isTrue(this.explorerService.checkExplorer(principal), "message.error.contactEmergency.authority");

		Assert.isTrue(principal.getContactEmergencies().contains(contactEmergency), "message.error.contactEmergency.explorer.owner");

		this.contactEmergencyRepository.delete(contactEmergency);

		explorerContactEmergencies = principal.getContactEmergencies();
		explorerContactEmergencies.remove(contactEmergency);
		principal.setContactEmergencies(explorerContactEmergencies);

		this.explorerService.saveFromEdit(principal);
	}

	// Other business methods
	public Collection<ContactEmergency> findAll() {
		Collection<ContactEmergency> result;

		result = this.contactEmergencyRepository.findAll();

		return result;
	}

	public ContactEmergency findOne(final int contactEmergencyId) {
		ContactEmergency result;

		result = this.contactEmergencyRepository.findOne(contactEmergencyId);

		return result;
	}

	public Integer count() {
		Integer result;

		result = (int) this.contactEmergencyRepository.count();

		return result;
	}

}
