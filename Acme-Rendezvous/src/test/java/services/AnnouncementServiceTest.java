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

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Announcement;

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


	// Tests ------------------------------------------------------------------

	@Test
	public void testCreateAnnouncementDriver() {
		// principal(actor), rendezvous, title, description, expected exception
		final Object[][] testingData = {
			{
				// Positive test 1: creating an announcement with user
				"user1", "rendezvous1", "title", "description", null
			}, {
				// Negative test 2: creating an announcement with admin
				"admin", "rendezvous1", "title", "description", IllegalArgumentException.class
			}, {
				// Negative test 3: creating an announcement with manager
				"manager1", "rendezvous1", "title", "description", IllegalArgumentException.class
			}, {
				// Negative test 4: creating an announcement with draft rendezvous 
				"user1", "rendezvous3", "title", "description", IllegalArgumentException.class
			}, {
				// Negative test 5: creating an announcement with deleted rendezvous
				"user1", "rendezvous2", "title", "description", IllegalArgumentException.class
			}, {
				// Negative test 6: creating an announcement with rendezvous that is not owner
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
			Announcement result = null;

			announcement = this.announcementService.create();

			announcement.setTitle(title);
			announcement.setDescription(description);
			announcement.setRendezvous(this.rendezvousService.findOne(this.getEntityId(rendezvous)));

			result = this.announcementService.saveFromCreate(announcement);
			this.announcementService.flush();

			Assert.notNull(result);

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptions(expectedException, caught);
	}
	@Test
	public void testEditAnnouncementDriver() {
		// principal(actor), announcement bean, title, description, expected exception
		final Object[][] testingData = {
			{
				// Positive test 1: edit an announcement with user
				"user1", "announcement1", "title mod", "description mod", null
			}, {
				// Negative test 2: edit an announcement with admin
				"admin", "announcement1", "title mod", "description mod", IllegalArgumentException.class
			}, {
				// Negative test 3: edit an announcement with manager
				"manager", "announcement1", "title mod", "description mod", IllegalArgumentException.class
			}, {
				// Negative test 4: edit an announcement with admin
				"user1", "announcement1", "title mod", "description mod", IllegalArgumentException.class
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
			Announcement result = null;

			announcement = this.announcementService.findOne(this.getEntityId(announcementBean));

			announcement.setTitle(title);
			announcement.setDescription(description);

			result = this.announcementService.saveFromEdit(announcement);
			this.announcementService.flush();

			Assert.notNull(result);

			Assert.notNull(result);
			Assert.isTrue(result.getTitle().equals(title));
			Assert.isTrue(result.getDescription().equals(description));

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptions(expectedException, caught);
	}

	@Test
	public void testDeleteAnnouncementDriver() {
		// principal(actor), announcement bean, expected exception
		final Object[][] testingData = {
			{
				// Positive test 1: delete an announcement with admin
				"admin", "announcement1", null
			}, {
				// Negative test 2: delete an announcement with user
				"user1", "announcement1", IllegalArgumentException.class
			}, {
				// Negative test 3: delete an announcement with manager
				"manager", "announcement1", IllegalArgumentException.class
			}, {
				// Negative test 4: delete an announcement with admin
				"user1", "announcement1", IllegalArgumentException.class
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
			final Announcement result = null;

			announcement = this.announcementService.findOne(this.getEntityId(announcementBean));
			this.announcementService.delete(announcement);

			this.announcementService.flush();

			Assert.isNull(result);

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptions(expectedException, caught);
	}

}
