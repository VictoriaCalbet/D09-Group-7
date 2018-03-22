/*
 * AdministratorServiceTest.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Announcement;
import domain.Rendezvous;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AnnouncementServiceTest extends AbstractTest {

	// System under test ------------------------------------------------------

	@Autowired
	private AnnouncementService	announcementService;

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private UserService			userService;


	// Tests ------------------------------------------------------------------

	/**
	 * 
	 * Acme-Rendezvous 1.0: Requirement 16.3
	 * 
	 * An actor who is authenticated as a user must be able to:
	 * - Create an announcement regarding one of the rendezvouses that he or she's created previously.
	 * 
	 * Positive test 1: creating an announcement with user
	 * Negative test 2: creating an announcement with admin
	 * Negative test 3: creating an announcement with manager
	 * Negative test 4: creating an announcement with draft rendezvous
	 * Negative test 5: creating an announcement with deleted rendezvous
	 * Negative test 6: creating an announcement with rendezvous that is not owner
	 */

	@Test
	public void testCreateAnnouncementDriver() {
		// principal(actor), rendezvous, title, description, expected exception
		final Object[][] testingData = {
			{
				"user1", "rendezvous1", "title", "description", null
			}, {
				"admin", "rendezvous1", "title", "description", IllegalArgumentException.class
			}, {
				"manager1", "rendezvous1", "title", "description", IllegalArgumentException.class
			}, {
				"user1", "rendezvous3", "title", "description", IllegalArgumentException.class
			}, {
				"user1", "rendezvous2", "title", "description", IllegalArgumentException.class
			}, {
				"user1", "rendezvous5", "title", "description", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testCreateAnnouncementTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}
	protected void testCreateAnnouncementTemplate(final String actor, final String rendezvous, final String title, final String description, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(actor);

			Announcement announcement = null;

			announcement = this.announcementService.create();

			announcement.setTitle(title);
			announcement.setDescription(description);
			announcement.setRendezvous(this.rendezvousService.findOne(this.getEntityId(rendezvous)));

			this.announcementService.saveFromCreate(announcement);
			this.announcementService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptions(expectedException, caught);
	}

	/**
	 * Edit an announcement:
	 * Positive test 1: edit an announcement with user
	 * Negative test 2: edit an announcement with admin
	 * Negative test 3: edit an announcement with manager
	 * Negative test 4: edit an announcement with user that it's not owner
	 */

	@Test
	public void testEditAnnouncementDriver() {
		// principal(actor), announcement bean, title, description, expected exception
		final Object[][] testingData = {
			{
				"user1", "announcement1", "title mod", "description mod", null
			}, {
				"admin", "announcement1", "title mod", "description mod", IllegalArgumentException.class
			}, {
				"manager", "announcement1", "title mod", "description mod", IllegalArgumentException.class
			}, {
				"user1", "announcement4", "title mod", "description mod", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testEditAnnouncementTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}
	protected void testEditAnnouncementTemplate(final String actor, final String announcementBean, final String title, final String description, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(actor);

			Announcement announcement = null;

			announcement = this.announcementService.findOne(this.getEntityId(announcementBean));

			announcement.setTitle(title);
			announcement.setDescription(description);

			this.announcementService.saveFromEdit(announcement);
			this.announcementService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptions(expectedException, caught);
	}

	/**
	 * 
	 * Acme-Rendezvous 1.0: Requirement 17.1
	 * 
	 * An actor who is authenticated as an administrator must be able to:
	 * - Remove an announcement that he or she thinks is inappropriate.
	 * 
	 * Positive test 1: delete an announcement with admin
	 * Negative test 2: delete an announcement with user
	 * Negative test 3: delete an announcement with manager
	 */
	@Test
	public void testDeleteAnnouncementDriver() {
		// principal(actor), announcement bean, expected exception
		final Object[][] testingData = {
			{
				"admin", "announcement1", null
			}, {
				"user1", "announcement1", IllegalArgumentException.class
			}, {
				"manager", "announcement1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testDeleteAnnouncementTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	protected void testDeleteAnnouncementTemplate(final String actor, final String announcementBean, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(actor);

			Announcement announcement = null;

			announcement = this.announcementService.findOne(this.getEntityId(announcementBean));
			this.announcementService.delete(announcement);

			this.announcementService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptions(expectedException, caught);
	}

	/**
	 * 
	 * Acme-Rendezvous 1.0: Requirement 15.1
	 * 
	 * An actor who is not authenticated must be able to:
	 * - List the announcements that are associated with each rendezvous
	 * 
	 * Note: this requirements has been development on controllers.
	 * 
	 * Positive test 1: Anyone can list announcements
	 */

	@Test
	public void testListAnnouncementsAssociatedWithEachRendezvousDriver() {
		// rendezvous bean, expected exception
		final Object[][] testingData = {
			{
				"rendezvous1", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testListAnnouncementsAssociatedWithEachRendezvousTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	public void testListAnnouncementsAssociatedWithEachRendezvousTemplate(final String rendezvous, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			Rendezvous rvs = null;
			Collection<Announcement> announcements = null;

			rvs = this.rendezvousService.findOne(this.getEntityId(rendezvous));
			announcements = rvs.getAnnouncements();

			Assert.isTrue(announcements.size() == 2);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptions(expectedException, caught);
	}

	/**
	 * 
	 * Acme-Rendezvous 1.0: Requirement 16.5
	 * 
	 * An actor who is authenticated as a user must be able to:
	 * - Display a stream of announcements that have been posted to the rendezvouses that he or she's
	 * RSVPd. The announcements must be listed chronologically in descending order.
	 * 
	 * Positive test 1: an user get a stream of announcements
	 */
	@Test
	public void testListStreamAnnouncementsDriver() {
		// rendezvous bean, expected exception
		final Object[][] testingData = {
			{
				"user1", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testListStreamAnnouncementsTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	public void testListStreamAnnouncementsTemplate(final String user, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(user);
			User usr = null;
			Collection<Announcement> announcements = null;

			usr = this.userService.findByPrincipal();
			announcements = this.announcementService.getAnnouncementsPostedAndAcceptedByUser(usr.getId());

			Assert.isTrue(announcements.size() == 3);
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptions(expectedException, caught);
	}
}
