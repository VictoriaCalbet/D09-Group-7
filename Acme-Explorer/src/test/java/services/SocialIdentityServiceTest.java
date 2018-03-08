
package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Actor;
import domain.SocialIdentity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class SocialIdentityServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private SocialIdentityService	socialIdentityService;

	@Autowired
	private ActorService			actorService;


	// Tests
	@Test
	public void testCreate1() {
		this.authenticate("sponsor1");

		SocialIdentity socialIdentityToCreate;

		socialIdentityToCreate = this.socialIdentityService.create();

		Assert.isTrue(!(socialIdentityToCreate.getId() > 0));
		Assert.isNull(socialIdentityToCreate.getNick());
		Assert.isNull(socialIdentityToCreate.getName());
		Assert.isNull(socialIdentityToCreate.getLink());
		Assert.isNull(socialIdentityToCreate.getPhoto());

		this.unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreate2() {
		SocialIdentity socialIdentityToCreate;

		socialIdentityToCreate = this.socialIdentityService.create();

		Assert.isTrue(!(socialIdentityToCreate.getId() > 0));
		Assert.isNull(socialIdentityToCreate.getNick());
		Assert.isNull(socialIdentityToCreate.getName());
		Assert.isNull(socialIdentityToCreate.getLink());
		Assert.isNull(socialIdentityToCreate.getPhoto());
	}

	@Test
	public void testSaveFromCreate1() {
		// Create the first socialIdentity
		this.authenticate("sponsor1");

		SocialIdentity socialIdentityToCreate;

		socialIdentityToCreate = this.socialIdentityService.create();

		Assert.isTrue(!(socialIdentityToCreate.getId() > 0));
		Assert.isNull(socialIdentityToCreate.getNick());
		Assert.isNull(socialIdentityToCreate.getName());
		Assert.isNull(socialIdentityToCreate.getLink());
		Assert.isNull(socialIdentityToCreate.getPhoto());

		this.unauthenticate();

		// Save the first socialIdentity.
		this.authenticate("sponsor1");

		final SocialIdentity socialIdentityToSave;
		Actor principal;
		Collection<SocialIdentity> principalSocialIdentities;
		final Integer principalSocialIdentitiesPreSave;
		final Integer principalSocialIdentitiesPostSave;

		socialIdentityToCreate.setNick("sponsor1first");
		socialIdentityToCreate.setName("Sponsor 1 first");
		socialIdentityToCreate.setLink("http://google.es");
		socialIdentityToCreate.setPhoto("http://google.es");

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		principalSocialIdentities = principal.getSocialIdentities();
		Assert.notNull(principalSocialIdentities);
		principalSocialIdentitiesPreSave = principalSocialIdentities.size();
		Assert.notNull(principalSocialIdentitiesPreSave);

		socialIdentityToSave = this.socialIdentityService.saveFromCreate(socialIdentityToCreate);

		Assert.isTrue(socialIdentityToSave.getNick().equals(socialIdentityToCreate.getNick()));
		Assert.isTrue(socialIdentityToSave.getName().equals(socialIdentityToCreate.getName()));
		Assert.isTrue(socialIdentityToSave.getLink().equals(socialIdentityToCreate.getLink()));
		Assert.isTrue(socialIdentityToSave.getPhoto().equals(socialIdentityToCreate.getPhoto()));

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		principalSocialIdentities = principal.getSocialIdentities();
		Assert.notNull(principalSocialIdentities);
		principalSocialIdentitiesPostSave = principalSocialIdentities.size();
		Assert.notNull(principalSocialIdentitiesPreSave);

		Assert.isTrue(principalSocialIdentitiesPostSave - principalSocialIdentitiesPreSave == 1);
		Assert.isTrue(principalSocialIdentities.contains(socialIdentityToSave));

		this.unauthenticate();

		// Create the second social identity.
		this.authenticate("sponsor1");

		SocialIdentity socialIdentityToCreate2;

		socialIdentityToCreate2 = this.socialIdentityService.create();

		Assert.isTrue(!(socialIdentityToCreate2.getId() > 0));
		Assert.isNull(socialIdentityToCreate2.getNick());
		Assert.isNull(socialIdentityToCreate2.getName());
		Assert.isNull(socialIdentityToCreate2.getLink());
		Assert.isNull(socialIdentityToCreate2.getPhoto());

		this.unauthenticate();

		// Save the second socialIdentity.
		this.authenticate("sponsor1");

		final SocialIdentity socialIdentityToSave2;
		Actor principal2;
		Collection<SocialIdentity> principalSocialIdentities2;
		final Integer principalSocialIdentitiesPreSave2;
		final Integer principalSocialIdentitiesPostSave2;

		socialIdentityToCreate2.setNick("sponsor1second");
		socialIdentityToCreate2.setName("Sponsor 1 second");
		socialIdentityToCreate2.setLink("http://google.es");
		socialIdentityToCreate2.setPhoto("http://google.es");

		principal2 = this.actorService.findByPrincipal();
		Assert.notNull(principal2);
		principalSocialIdentities2 = principal2.getSocialIdentities();
		Assert.notNull(principalSocialIdentities2);
		principalSocialIdentitiesPreSave2 = principalSocialIdentities2.size();
		Assert.notNull(principalSocialIdentitiesPreSave2);

		socialIdentityToSave2 = this.socialIdentityService.saveFromCreate(socialIdentityToCreate2);

		Assert.isTrue(socialIdentityToSave2.getNick().equals(socialIdentityToCreate2.getNick()));
		Assert.isTrue(socialIdentityToSave2.getName().equals(socialIdentityToCreate2.getName()));
		Assert.isTrue(socialIdentityToSave2.getLink().equals(socialIdentityToCreate2.getLink()));
		Assert.isTrue(socialIdentityToSave2.getPhoto().equals(socialIdentityToCreate2.getPhoto()));

		principal2 = this.actorService.findByPrincipal();
		Assert.notNull(principal2);
		principalSocialIdentities2 = principal2.getSocialIdentities();
		Assert.notNull(principalSocialIdentities2);
		principalSocialIdentitiesPostSave2 = principalSocialIdentities2.size();
		Assert.notNull(principalSocialIdentitiesPreSave2);

		Assert.isTrue(principalSocialIdentitiesPostSave2 - principalSocialIdentitiesPreSave2 == 1);
		Assert.isTrue(principalSocialIdentities2.contains(socialIdentityToSave2));

		this.unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSaveFromCreate2() {
		// Create the first socialIdentity
		this.authenticate("sponsor1");

		SocialIdentity socialIdentityToCreate;

		socialIdentityToCreate = this.socialIdentityService.create();

		Assert.isTrue(!(socialIdentityToCreate.getId() > 0));
		Assert.isNull(socialIdentityToCreate.getNick());
		Assert.isNull(socialIdentityToCreate.getName());
		Assert.isNull(socialIdentityToCreate.getLink());
		Assert.isNull(socialIdentityToCreate.getPhoto());

		this.unauthenticate();

		// Save the first socialIdentity.
		final SocialIdentity socialIdentityToSave;
		Actor principal;
		Collection<SocialIdentity> principalSocialIdentities;
		final Integer principalSocialIdentitiesPreSave;
		final Integer principalSocialIdentitiesPostSave;

		socialIdentityToCreate.setNick("sponsor1first");
		socialIdentityToCreate.setName("Sponsor 1 first");
		socialIdentityToCreate.setLink("http://google.es");
		socialIdentityToCreate.setPhoto("http://google.es");

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		principalSocialIdentities = principal.getSocialIdentities();
		Assert.notNull(principalSocialIdentities);
		principalSocialIdentitiesPreSave = principalSocialIdentities.size();
		Assert.notNull(principalSocialIdentitiesPreSave);

		socialIdentityToSave = this.socialIdentityService.saveFromCreate(socialIdentityToCreate);

		Assert.isTrue(socialIdentityToSave.getNick().equals(socialIdentityToCreate.getNick()));
		Assert.isTrue(socialIdentityToSave.getName().equals(socialIdentityToCreate.getName()));
		Assert.isTrue(socialIdentityToSave.getLink().equals(socialIdentityToCreate.getLink()));
		Assert.isTrue(socialIdentityToSave.getPhoto().equals(socialIdentityToCreate.getPhoto()));

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		principalSocialIdentities = principal.getSocialIdentities();
		Assert.notNull(principalSocialIdentities);
		principalSocialIdentitiesPostSave = principalSocialIdentities.size();
		Assert.notNull(principalSocialIdentitiesPreSave);

		Assert.isTrue(principalSocialIdentitiesPostSave - principalSocialIdentitiesPreSave == 1);
		Assert.isTrue(principalSocialIdentities.contains(socialIdentityToSave));

	}

	@Test
	public void testSaveFromEdit1() {
		// Create the first socialIdentity
		this.authenticate("sponsor1");

		SocialIdentity socialIdentityToCreate;

		socialIdentityToCreate = this.socialIdentityService.create();

		Assert.isTrue(!(socialIdentityToCreate.getId() > 0));
		Assert.isNull(socialIdentityToCreate.getNick());
		Assert.isNull(socialIdentityToCreate.getName());
		Assert.isNull(socialIdentityToCreate.getLink());
		Assert.isNull(socialIdentityToCreate.getPhoto());

		this.unauthenticate();

		// Save the first socialIdentity.
		this.authenticate("sponsor1");

		final SocialIdentity socialIdentityToSave;
		Actor principal;
		Collection<SocialIdentity> principalSocialIdentities;
		final Integer principalSocialIdentitiesPreSave;
		final Integer principalSocialIdentitiesPostSave;

		socialIdentityToCreate.setNick("sponsor1first");
		socialIdentityToCreate.setName("Sponsor 1 First");
		socialIdentityToCreate.setLink("http://google.es");
		socialIdentityToCreate.setPhoto("http://google.es");

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		principalSocialIdentities = principal.getSocialIdentities();
		Assert.notNull(principalSocialIdentities);
		principalSocialIdentitiesPreSave = principalSocialIdentities.size();
		Assert.notNull(principalSocialIdentitiesPreSave);

		socialIdentityToSave = this.socialIdentityService.saveFromCreate(socialIdentityToCreate);

		Assert.isTrue(socialIdentityToSave.getNick().equals(socialIdentityToCreate.getNick()));
		Assert.isTrue(socialIdentityToSave.getName().equals(socialIdentityToCreate.getName()));
		Assert.isTrue(socialIdentityToSave.getLink().equals(socialIdentityToCreate.getLink()));
		Assert.isTrue(socialIdentityToSave.getPhoto().equals(socialIdentityToCreate.getPhoto()));

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		principalSocialIdentities = principal.getSocialIdentities();
		Assert.notNull(principalSocialIdentities);
		principalSocialIdentitiesPostSave = principalSocialIdentities.size();
		Assert.notNull(principalSocialIdentitiesPreSave);

		Assert.isTrue(principalSocialIdentitiesPostSave - principalSocialIdentitiesPreSave == 1);
		Assert.isTrue(principalSocialIdentities.contains(socialIdentityToSave));

		this.unauthenticate();

		// Update the socialIdentity.
		this.authenticate("sponsor1");

		final SocialIdentity socialIdentityToRetrieve;

		socialIdentityToSave.setName("Sponsor 1 First Updated");

		socialIdentityToRetrieve = this.socialIdentityService.saveFromEdit(socialIdentityToSave);

		principal = null;
		principalSocialIdentities = null;

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		principalSocialIdentities = principal.getSocialIdentities();
		Assert.notNull(principalSocialIdentities);

		Assert.isTrue(principalSocialIdentities.contains(socialIdentityToRetrieve));

		this.unauthenticate();

	}

	@Test(expected = IllegalArgumentException.class)
	public void testSaveFromEdit2() {
		// Create the first socialIdentity
		this.authenticate("sponsor1");

		SocialIdentity socialIdentityToCreate;

		socialIdentityToCreate = this.socialIdentityService.create();

		Assert.isTrue(!(socialIdentityToCreate.getId() > 0));
		Assert.isNull(socialIdentityToCreate.getNick());
		Assert.isNull(socialIdentityToCreate.getName());
		Assert.isNull(socialIdentityToCreate.getLink());
		Assert.isNull(socialIdentityToCreate.getPhoto());

		this.unauthenticate();

		// Save the first socialIdentity.
		this.authenticate("sponsor1");

		final SocialIdentity socialIdentityToSave;
		Actor principal;
		Collection<SocialIdentity> principalSocialIdentities;
		final Integer principalSocialIdentitiesPreSave;
		final Integer principalSocialIdentitiesPostSave;

		socialIdentityToCreate.setNick("sponsor1first");
		socialIdentityToCreate.setName("Sponsor 1 First");
		socialIdentityToCreate.setLink("http://google.es");
		socialIdentityToCreate.setPhoto("http://google.es");

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		principalSocialIdentities = principal.getSocialIdentities();
		Assert.notNull(principalSocialIdentities);
		principalSocialIdentitiesPreSave = principalSocialIdentities.size();
		Assert.notNull(principalSocialIdentitiesPreSave);

		socialIdentityToSave = this.socialIdentityService.saveFromCreate(socialIdentityToCreate);

		Assert.isTrue(socialIdentityToSave.getNick().equals(socialIdentityToCreate.getNick()));
		Assert.isTrue(socialIdentityToSave.getName().equals(socialIdentityToCreate.getName()));
		Assert.isTrue(socialIdentityToSave.getLink().equals(socialIdentityToCreate.getLink()));
		Assert.isTrue(socialIdentityToSave.getPhoto().equals(socialIdentityToCreate.getPhoto()));

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		principalSocialIdentities = principal.getSocialIdentities();
		Assert.notNull(principalSocialIdentities);
		principalSocialIdentitiesPostSave = principalSocialIdentities.size();
		Assert.notNull(principalSocialIdentitiesPreSave);

		Assert.isTrue(principalSocialIdentitiesPostSave - principalSocialIdentitiesPreSave == 1);
		Assert.isTrue(principalSocialIdentities.contains(socialIdentityToSave));

		this.unauthenticate();

		// Update the socialIdentity.
		final SocialIdentity socialIdentityToRetrieve;

		socialIdentityToSave.setName("Sponsor 1 First Updated");

		socialIdentityToRetrieve = this.socialIdentityService.saveFromEdit(socialIdentityToSave);

		principal = null;
		principalSocialIdentities = null;

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		principalSocialIdentities = principal.getSocialIdentities();
		Assert.notNull(principalSocialIdentities);

		Assert.isTrue(principalSocialIdentities.contains(socialIdentityToRetrieve));

	}

	@Test(expected = IllegalArgumentException.class)
	public void testSaveFromEdit3() {
		// Create the first socialIdentity
		this.authenticate("sponsor1");

		SocialIdentity socialIdentityToCreate;

		socialIdentityToCreate = this.socialIdentityService.create();

		Assert.isTrue(!(socialIdentityToCreate.getId() > 0));
		Assert.isNull(socialIdentityToCreate.getNick());
		Assert.isNull(socialIdentityToCreate.getName());
		Assert.isNull(socialIdentityToCreate.getLink());
		Assert.isNull(socialIdentityToCreate.getPhoto());

		this.unauthenticate();

		// Save the first socialIdentity.
		this.authenticate("sponsor1");

		final SocialIdentity socialIdentityToSave;
		Actor principal;
		Collection<SocialIdentity> principalSocialIdentities;
		final Integer principalSocialIdentitiesPreSave;
		final Integer principalSocialIdentitiesPostSave;

		socialIdentityToCreate.setNick("sponsor1first");
		socialIdentityToCreate.setName("Sponsor 1 First");
		socialIdentityToCreate.setLink("http://google.es");
		socialIdentityToCreate.setPhoto("http://google.es");

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		principalSocialIdentities = principal.getSocialIdentities();
		Assert.notNull(principalSocialIdentities);
		principalSocialIdentitiesPreSave = principalSocialIdentities.size();
		Assert.notNull(principalSocialIdentitiesPreSave);

		socialIdentityToSave = this.socialIdentityService.saveFromCreate(socialIdentityToCreate);

		Assert.isTrue(socialIdentityToSave.getNick().equals(socialIdentityToCreate.getNick()));
		Assert.isTrue(socialIdentityToSave.getName().equals(socialIdentityToCreate.getName()));
		Assert.isTrue(socialIdentityToSave.getLink().equals(socialIdentityToCreate.getLink()));
		Assert.isTrue(socialIdentityToSave.getPhoto().equals(socialIdentityToCreate.getPhoto()));

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		principalSocialIdentities = principal.getSocialIdentities();
		Assert.notNull(principalSocialIdentities);
		principalSocialIdentitiesPostSave = principalSocialIdentities.size();
		Assert.notNull(principalSocialIdentitiesPreSave);

		Assert.isTrue(principalSocialIdentitiesPostSave - principalSocialIdentitiesPreSave == 1);
		Assert.isTrue(principalSocialIdentities.contains(socialIdentityToSave));

		this.unauthenticate();

		// Update the socialIdentity.
		this.authenticate("sponsor2");

		final SocialIdentity socialIdentityToRetrieve;

		socialIdentityToSave.setName("Sponsor 1 First Updated");

		socialIdentityToRetrieve = this.socialIdentityService.saveFromEdit(socialIdentityToSave);

		principal = null;
		principalSocialIdentities = null;

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		principalSocialIdentities = principal.getSocialIdentities();
		Assert.notNull(principalSocialIdentities);

		Assert.isTrue(principalSocialIdentities.contains(socialIdentityToRetrieve));

		this.unauthenticate();

	}

	@Test
	public void testDelete1() {
		// Create the first socialIdentity
		this.authenticate("sponsor1");

		SocialIdentity socialIdentityToCreate;

		socialIdentityToCreate = this.socialIdentityService.create();

		Assert.isTrue(!(socialIdentityToCreate.getId() > 0));
		Assert.isNull(socialIdentityToCreate.getNick());
		Assert.isNull(socialIdentityToCreate.getName());
		Assert.isNull(socialIdentityToCreate.getLink());
		Assert.isNull(socialIdentityToCreate.getPhoto());

		this.unauthenticate();

		// Save the first socialIdentity.
		this.authenticate("sponsor1");

		final SocialIdentity socialIdentityToSave;
		Actor principal;
		Collection<SocialIdentity> principalSocialIdentities;
		final Integer principalSocialIdentitiesPreSave;
		final Integer principalSocialIdentitiesPostSave;

		socialIdentityToCreate.setNick("sponsor1first");
		socialIdentityToCreate.setName("Sponsor 1 First");
		socialIdentityToCreate.setLink("http://google.es");
		socialIdentityToCreate.setPhoto("http://google.es");

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		principalSocialIdentities = principal.getSocialIdentities();
		Assert.notNull(principalSocialIdentities);
		principalSocialIdentitiesPreSave = principalSocialIdentities.size();
		Assert.notNull(principalSocialIdentitiesPreSave);

		socialIdentityToSave = this.socialIdentityService.saveFromCreate(socialIdentityToCreate);

		Assert.isTrue(socialIdentityToSave.getNick().equals(socialIdentityToCreate.getNick()));
		Assert.isTrue(socialIdentityToSave.getName().equals(socialIdentityToCreate.getName()));
		Assert.isTrue(socialIdentityToSave.getLink().equals(socialIdentityToCreate.getLink()));
		Assert.isTrue(socialIdentityToSave.getPhoto().equals(socialIdentityToCreate.getPhoto()));

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		principalSocialIdentities = principal.getSocialIdentities();
		Assert.notNull(principalSocialIdentities);
		principalSocialIdentitiesPostSave = principalSocialIdentities.size();
		Assert.notNull(principalSocialIdentitiesPreSave);

		Assert.isTrue(principalSocialIdentitiesPostSave - principalSocialIdentitiesPreSave == 1);
		Assert.isTrue(principalSocialIdentities.contains(socialIdentityToSave));

		this.unauthenticate();

		// Delete the socialIdentity.
		this.authenticate("sponsor1");

		this.socialIdentityService.delete(socialIdentityToSave);

		principal = null;
		principalSocialIdentities = null;

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		principalSocialIdentities = principal.getSocialIdentities();
		Assert.notNull(principalSocialIdentities);

		Assert.isTrue(!principalSocialIdentities.contains(socialIdentityToSave));

		this.unauthenticate();

	}

	@Test(expected = IllegalArgumentException.class)
	public void testDelete2() {
		// Create the first socialIdentity
		this.authenticate("sponsor1");

		SocialIdentity socialIdentityToCreate;

		socialIdentityToCreate = this.socialIdentityService.create();

		Assert.isTrue(!(socialIdentityToCreate.getId() > 0));
		Assert.isNull(socialIdentityToCreate.getNick());
		Assert.isNull(socialIdentityToCreate.getName());
		Assert.isNull(socialIdentityToCreate.getLink());
		Assert.isNull(socialIdentityToCreate.getPhoto());

		this.unauthenticate();

		// Save the first socialIdentity.
		this.authenticate("sponsor1");

		final SocialIdentity socialIdentityToSave;
		Actor principal;
		Collection<SocialIdentity> principalSocialIdentities;
		final Integer principalSocialIdentitiesPreSave;
		final Integer principalSocialIdentitiesPostSave;

		socialIdentityToCreate.setNick("sponsor1first");
		socialIdentityToCreate.setName("Sponsor 1 First");
		socialIdentityToCreate.setLink("http://google.es");
		socialIdentityToCreate.setPhoto("http://google.es");

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		principalSocialIdentities = principal.getSocialIdentities();
		Assert.notNull(principalSocialIdentities);
		principalSocialIdentitiesPreSave = principalSocialIdentities.size();
		Assert.notNull(principalSocialIdentitiesPreSave);

		socialIdentityToSave = this.socialIdentityService.saveFromCreate(socialIdentityToCreate);

		Assert.isTrue(socialIdentityToSave.getNick().equals(socialIdentityToCreate.getNick()));
		Assert.isTrue(socialIdentityToSave.getName().equals(socialIdentityToCreate.getName()));
		Assert.isTrue(socialIdentityToSave.getLink().equals(socialIdentityToCreate.getLink()));
		Assert.isTrue(socialIdentityToSave.getPhoto().equals(socialIdentityToCreate.getPhoto()));

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		principalSocialIdentities = principal.getSocialIdentities();
		Assert.notNull(principalSocialIdentities);
		principalSocialIdentitiesPostSave = principalSocialIdentities.size();
		Assert.notNull(principalSocialIdentitiesPreSave);

		Assert.isTrue(principalSocialIdentitiesPostSave - principalSocialIdentitiesPreSave == 1);
		Assert.isTrue(principalSocialIdentities.contains(socialIdentityToSave));

		this.unauthenticate();

		// Delete the socialIdentity.
		this.socialIdentityService.delete(socialIdentityToSave);

		principal = null;
		principalSocialIdentities = null;

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		principalSocialIdentities = principal.getSocialIdentities();
		Assert.notNull(principalSocialIdentities);

		Assert.isTrue(!principalSocialIdentities.contains(socialIdentityToSave));

	}

	@Test(expected = IllegalArgumentException.class)
	public void testDelete3() {
		// Create the first socialIdentity
		this.authenticate("sponsor1");

		SocialIdentity socialIdentityToCreate;

		socialIdentityToCreate = this.socialIdentityService.create();

		Assert.isTrue(!(socialIdentityToCreate.getId() > 0));
		Assert.isNull(socialIdentityToCreate.getNick());
		Assert.isNull(socialIdentityToCreate.getName());
		Assert.isNull(socialIdentityToCreate.getLink());
		Assert.isNull(socialIdentityToCreate.getPhoto());

		this.unauthenticate();

		// Save the first socialIdentity.
		this.authenticate("sponsor1");

		final SocialIdentity socialIdentityToSave;
		Actor principal;
		Collection<SocialIdentity> principalSocialIdentities;
		final Integer principalSocialIdentitiesPreSave;
		final Integer principalSocialIdentitiesPostSave;

		socialIdentityToCreate.setNick("sponsor1first");
		socialIdentityToCreate.setName("Sponsor 1 First");
		socialIdentityToCreate.setLink("http://google.es");
		socialIdentityToCreate.setPhoto("http://google.es");

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		principalSocialIdentities = principal.getSocialIdentities();
		Assert.notNull(principalSocialIdentities);
		principalSocialIdentitiesPreSave = principalSocialIdentities.size();
		Assert.notNull(principalSocialIdentitiesPreSave);

		socialIdentityToSave = this.socialIdentityService.saveFromCreate(socialIdentityToCreate);

		Assert.isTrue(socialIdentityToSave.getNick().equals(socialIdentityToCreate.getNick()));
		Assert.isTrue(socialIdentityToSave.getName().equals(socialIdentityToCreate.getName()));
		Assert.isTrue(socialIdentityToSave.getLink().equals(socialIdentityToCreate.getLink()));
		Assert.isTrue(socialIdentityToSave.getPhoto().equals(socialIdentityToCreate.getPhoto()));

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		principalSocialIdentities = principal.getSocialIdentities();
		Assert.notNull(principalSocialIdentities);
		principalSocialIdentitiesPostSave = principalSocialIdentities.size();
		Assert.notNull(principalSocialIdentitiesPreSave);

		Assert.isTrue(principalSocialIdentitiesPostSave - principalSocialIdentitiesPreSave == 1);
		Assert.isTrue(principalSocialIdentities.contains(socialIdentityToSave));

		this.unauthenticate();

		// Delete the socialIdentity.
		this.authenticate("sponsor2");

		this.socialIdentityService.delete(socialIdentityToSave);

		principal = null;
		principalSocialIdentities = null;

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		principalSocialIdentities = principal.getSocialIdentities();
		Assert.notNull(principalSocialIdentities);

		Assert.isTrue(!principalSocialIdentities.contains(socialIdentityToSave));

		this.unauthenticate();

	}
}
