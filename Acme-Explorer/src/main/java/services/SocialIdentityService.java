
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.SocialIdentityRepository;
import domain.Actor;
import domain.SocialIdentity;

@Service
@Transactional
public class SocialIdentityService {

	// Managed Repository
	@Autowired
	private SocialIdentityRepository	socialIdentityRepository;

	// Supporting Services
	@Autowired
	private ActorService				actorService;

	@Autowired
	private MessageService				messageService;


	// Constructor

	public SocialIdentityService() {
		super();
	}

	// Simple CRUD methods
	public SocialIdentity create() {
		Assert.isTrue(this.actorService.checkLogin(), "message.error.socialIdentity.login");

		SocialIdentity result;

		result = new SocialIdentity();

		return result;
	}

	private SocialIdentity save(final SocialIdentity socialIdentity) {
		Assert.notNull(socialIdentity, "message.error.socialIdentity.notNull");

		SocialIdentity result;

		result = this.socialIdentityRepository.save(socialIdentity);

		return result;
	}

	public SocialIdentity saveFromCreate(final SocialIdentity socialIdentity) {
		Assert.isTrue(this.actorService.checkLogin(), "message.error.socialIdentity.login");
		Assert.notNull(socialIdentity, "message.error.socialIdentity.notNull");

		SocialIdentity result;
		Actor principal;
		Collection<SocialIdentity> socialIdentities;
		Boolean isSuspicious;

		result = this.save(socialIdentity);

		// Add SocialIdentity to the current actor
		principal = this.actorService.findByPrincipal();
		socialIdentities = principal.getSocialIdentities();
		socialIdentities.add(result);

		isSuspicious = principal.getIsSuspicious();
		isSuspicious = isSuspicious || this.messageService.checkSpam(result.getName()) || this.messageService.checkSpam(result.getNick()) || this.messageService.checkSpam(result.getLink()) || this.messageService.checkSpam(result.getPhoto());
		principal.setIsSuspicious(isSuspicious);

		this.actorService.save(principal);

		return result;
	}
	public SocialIdentity saveFromEdit(final SocialIdentity socialIdentity) {
		Assert.isTrue(this.actorService.checkLogin(), "message.error.socialIdentity.login");
		Assert.notNull(socialIdentity, "message.error.socialIdentity.notNull");
		// Check SocialIdentity owner.
		Assert.isTrue(this.checkOwner(socialIdentity), "message.error.socialIdentity.owner");

		final SocialIdentity result;
		Actor principal;
		Boolean isSuspicious;

		result = this.save(socialIdentity);

		principal = this.actorService.findByPrincipal();
		isSuspicious = principal.getIsSuspicious();
		isSuspicious = isSuspicious || this.messageService.checkSpam(result.getName()) || this.messageService.checkSpam(result.getNick()) || this.messageService.checkSpam(result.getLink()) || this.messageService.checkSpam(result.getPhoto());
		principal.setIsSuspicious(isSuspicious);

		this.actorService.save(principal);

		return result;
	}

	public void delete(final SocialIdentity socialIdentity) {
		Assert.isTrue(this.actorService.checkLogin(), "message.error.socialIdentity.login");
		Assert.notNull(socialIdentity, "message.error.socialIdentity.notNull");
		// Check SocialIdentity owner.
		Assert.isTrue(this.checkOwner(socialIdentity), "message.error.socialIdentity.owner");

		Actor principal;
		Collection<SocialIdentity> principalSocialIdentities;

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal, "message.error.socialIdentity.principal");
		principalSocialIdentities = principal.getSocialIdentities();
		Assert.notNull(principalSocialIdentities, "message.error.socialIdentity.list");

		principalSocialIdentities.remove(socialIdentity);
		principal.setSocialIdentities(principalSocialIdentities);
		this.actorService.save(principal);

		this.socialIdentityRepository.delete(socialIdentity);
	}

	// Other business methods
	public Collection<SocialIdentity> findAll() {
		Collection<SocialIdentity> result;

		result = this.socialIdentityRepository.findAll();

		return result;
	}

	public SocialIdentity findOne(final int socialIdentityId) {
		SocialIdentity result;

		result = this.socialIdentityRepository.findOne(socialIdentityId);

		return result;
	}

	public Integer count() {
		Integer result;

		result = (int) this.socialIdentityRepository.count();

		return result;
	}

	private boolean checkOwner(final SocialIdentity socialIdentity) {
		Assert.notNull(socialIdentity);

		boolean result = false;
		Actor principal;
		final Collection<SocialIdentity> principalSocialIdentities;

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		principalSocialIdentities = principal.getSocialIdentities();
		Assert.notNull(principalSocialIdentities);

		for (final SocialIdentity s : principalSocialIdentities)
			if (s.equals(socialIdentity)) {
				result = true;
				break;
			}

		return result;
	}

}
