
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Category;
import domain.GPSPoint;
import domain.Rendezvous;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class RendezvousServiceTest extends AbstractTest {

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private CategoryService		categoryService;


	// Tests ------------------------------------------------------------------

	/***
	 * Acme-Rendezvous 1.0: Requirement 5.2
	 * 
	 * Create a rendezvous, which he's implicitly assumed to attend.
	 * Note that a user may edit his or her rendezvouses as long as
	 * they aren't saved them in final mode. Once a rendezvous is saved
	 * in final mode, it cannot be edited or deleted by the creator.
	 * 
	 * Positive test 1: Create a rendezvous.
	 * Negative test 2: Create a rendezvous with a past date.
	 * Negative test 3: Create an adult rendezvous by an under 18 user.
	 */
	@Test
	public void testCreateRendezvous() {
		final Object[][] testingData = {
			// principal, name, description, meetingMoment, picture,latitude, longitude, isAdult, isDraft, expected exception
			{
				"user1", "rendezvous1", "description of rendezvous1", new DateTime().plusHours(1).toDate(), "https://goo.gl/UscuZg", 85.8, 102.3, false, false, null
			}, {
				"user1", "rendezvous1", "description of rendezvous1", new DateTime().plusDays(-10).toDate(), "https://goo.gl/UscuZg", 85.8, 102.3, false, false, IllegalArgumentException.class
			}, {
				"user3", "rendezvous1", "description of rendezvous1", new DateTime().plusDays(2).toDate(), "https://goo.gl/UscuZg", 85.8, 100.1, true, false, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createRendezvousTemplated((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Date) testingData[i][3], (String) testingData[i][4], (double) testingData[i][5], (double) testingData[i][6],
				(boolean) testingData[i][7], (boolean) testingData[i][8], (Class<?>) testingData[i][9]);
	}

	protected void createRendezvousTemplated(final String principal, final String name, final String description, final Date meetingMoment, final String picture, final double latitude, final double longitude, final boolean isAdult, final boolean isDraft,
		final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(principal);
			final Rendezvous r = this.rendezvousService.create();
			r.setName(name);
			r.setDescription(description);
			r.setMeetingMoment(meetingMoment);
			r.setPicture(picture);
			final GPSPoint g = new GPSPoint();
			g.setLatitude(latitude);
			g.setLongitude(longitude);
			r.setGpsPoint(g);
			r.setIsAdultOnly(isAdult);
			r.setIsDraft(isDraft);
			this.rendezvousService.save(r);
			this.unauthenticate();
			this.rendezvousService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptions(expectedException, caught);
	}
	/***
	 * 
	 * 
	 * Requirement 5.3 to Acme-Rendezvous
	 * 
	 * Update or delete the rendezvouses that he or she's created.
	 * Deletion is virtual, that is: the information is not removed
	 * from the database, but the rendezvous cannot be updated.
	 * Deleted rendezvouses are flagged as such when they are displayed.
	 * 
	 * Positive test 1: Edit a rendezvous.
	 * Negative test 2: Edit a rendezvous with a past date.
	 * Negative test 3: Edit a deleted rendezvous.
	 * Negative test 4: Edit a rendezvous by the admin.
	 * Negative test 5: Edit a rendezvous that belongs to other user.
	 */
	@Test
	public void testEditRendezvous() {

		final Rendezvous r2 = this.rendezvousService.findOne(this.getEntityId("rendezvous2"));
		final Rendezvous r3 = this.rendezvousService.findOne(this.getEntityId("rendezvous3"));

		final Object[][] testingData = {
			// principal, name, description, meetingMoment, picture,latitude, longitude, isAdult, isDraft, expected exception
			{
				"user1", "rendezvous1", new DateTime().plusHours(1).toDate(), false, r3, null
			}, {
				"user1", "rendezvous1", new DateTime().plusDays(-10).toDate(), false, r3, IllegalArgumentException.class
			}, {
				"user1", "rendezvous1", new DateTime().plusDays(10).toDate(), false, r2, IllegalArgumentException.class
			}, {
				"admin", "rendezvous1", new DateTime().plusDays(10).toDate(), false, r3, IllegalArgumentException.class
			}, {
				"user3", "rendezvous1", new DateTime().plusDays(2).toDate(), false, r3, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editRendezvousTemplated((String) testingData[i][0], (String) testingData[i][1], (Date) testingData[i][2], (boolean) testingData[i][3], (Rendezvous) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	protected void editRendezvousTemplated(final String principal, final String name, final Date meetingMoment, final boolean isAdult, final Rendezvous r, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(principal);
			r.setName(name);
			r.setIsAdultOnly(isAdult);
			r.setMeetingMoment(meetingMoment);
			r.setGpsPoint(new GPSPoint());

			this.rendezvousService.update(r);
			this.unauthenticate();
			this.rendezvousService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptions(expectedException, caught);
	}

	/***
	 * 
	 * 
	 * Requirement 5.3 to Acme-Rendezvous
	 * 
	 * Update or delete the rendezvouses that he or she's created.
	 * Deletion is virtual, that is: the information is not removed
	 * from the database, but the rendezvous cannot be updated.
	 * Deleted rendezvouses are flagged as such when they are displayed.
	 * 
	 * Positive test 1: Delete a rendezvous
	 * Negative test 2: Delete a final rendezvous.
	 * Negative test 3º Delete an already deleted rendezvous.
	 * Negative test 4º Delete a rendezvous logged as an manager.
	 * Negative test 5º Delete a rendezvous that belongs to another user.
	 */
	@Test
	public void testDeleteRendezvous() {

		final Rendezvous r1 = this.rendezvousService.findOne(this.getEntityId("rendezvous1"));
		final Rendezvous r2 = this.rendezvousService.findOne(this.getEntityId("rendezvous2"));
		final Rendezvous r3 = this.rendezvousService.findOne(this.getEntityId("rendezvous3"));

		final Object[][] testingData = {
			//actor, rendezvousId, expected exception
			{
				"user1", r3, null
			}, {
				"user1", r1, IllegalArgumentException.class
			}, {
				"user1", r2, IllegalArgumentException.class
			}, {
				"manager1", r3, IllegalArgumentException.class
			}, {
				"user3", r3, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteRendezvousTemplated((String) testingData[i][0], (Rendezvous) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void deleteRendezvousTemplated(final String principal, final Rendezvous r, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(principal);
			this.rendezvousService.delete(r.getId());
			this.unauthenticate();
			this.rendezvousService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptions(expectedException, caught);
	}

	/***
	 * 
	 * Requirement 6.2 to Acme-Rendezvous
	 * 
	 * Remove a rendezvous that he or she thinks is inappropriate.
	 * 
	 * Positive test 1: Delete a rendezvous from the database by an admin.
	 * Negative test 2: Delete a rendezvous from the database by a user.
	 */
	@Test
	public void testDeleteRendezvousAdmin() {
		final Rendezvous r1 = this.rendezvousService.findOne(this.getEntityId("rendezvous1"));

		final Object[][] testingData = {
			//actor, rendezvousId, expected exception
			{
				"admin", r1, null
			}, {
				"user1", r1, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteRendezvousAdminTemplated((String) testingData[i][0], (Rendezvous) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void deleteRendezvousAdminTemplated(final String principal, final Rendezvous r, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(principal);
			this.rendezvousService.deleteAdmin(r.getId());
			this.unauthenticate();
			this.rendezvousService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptions(expectedException, caught);
	}

	/***
	 * Requirement 16.4 to Acme-Rendezvous
	 * 
	 * Link one of the rendezvouses that he or
	 * she's created to other similar rendezvouses.
	 * 
	 * Positive test 1: Link a rendezvous with another one.
	 * Negative test 2: Link a rendezvous with another one already linked.
	 */
	@Test
	public void testLinkRendezvous() {
		final Rendezvous r1 = this.rendezvousService.findOne(this.getEntityId("rendezvous1"));
		final Rendezvous r3 = this.rendezvousService.findOne(this.getEntityId("rendezvous3"));
		final Rendezvous r2 = this.rendezvousService.findOne(this.getEntityId("rendezvous2"));
		final Rendezvous r4 = this.rendezvousService.findOne(this.getEntityId("rendezvous4"));

		final Object[][] testingData = {
			//actor, rendezvousId, rendezvousId, expected exception
			{
				"user1", r1, r3, null
			}, {
				"user1", r2, r4, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.linkRendezvousTemplated((String) testingData[i][0], (Rendezvous) testingData[i][1], (Rendezvous) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	protected void linkRendezvousTemplated(final String principal, final Rendezvous r1, final Rendezvous r2, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(principal);
			this.rendezvousService.linked(r1, r2);
			this.unauthenticate();
			this.rendezvousService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptions(expectedException, caught);
	}

	/***
	 * 
	 * Requirement 4.3 to Acme-Rendezvous
	 * 
	 * List the rendezvouses in the system and navigate to the
	 * profiles of the corresponding creators and attendants.
	 * 
	 * Testing cases:
	 * Positive test 1: List the rendezvouses.
	 */

	@Test
	public void listRendezvous() {

		final Object testingData[][] = {
			//principal expected exception
			{
				"user1", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listRendezvous((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	protected void listRendezvous(final String principal, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(principal);

			final Collection<Rendezvous> rendezvouses = this.rendezvousService.findRendezvousesLogged(this.actorService.findByPrincipal());
			Assert.isTrue(rendezvouses.size() == 4);

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptions(expectedException, caught);
	}

	/***
	 * 
	 * Requirement 15.2 to Acme-Rendezvous
	 * 
	 * Navigate from a rendezvous to the rendezvouses
	 * that are similar to it.
	 * 
	 * Testing cases:
	 * Positive test 1: List the rendezvouses similar to one.
	 */

	@Test
	public void listLinkedRendezvous() {

		final Rendezvous r1 = this.rendezvousService.findOne(this.getEntityId("rendezvous1"));

		final Object testingData[][] = {
			//principal expected exception
			{
				"user1", r1, null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listLinkedRendezvous((String) testingData[i][0], (Rendezvous) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void listLinkedRendezvous(final String principal, final Rendezvous r, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(principal);

			final Collection<Rendezvous> rendezvouses = this.rendezvousService.findRendezvousSimilarLogged(r.getId());
			Assert.isTrue(rendezvouses.size() == 1);

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptions(expectedException, caught);
	}

	/***
	 * 
	 * Requirement 10.1 to Acme-Rendezvous 2.0
	 * 
	 * List the rendezvouses in the system grouped by category.
	 * 
	 * Testing cases:
	 * Positive test 1: List the rendezvouses that belongs to a category.
	 */

	@Test
	public void listCategoryRendezvous() {

		final Category c1 = this.categoryService.findOne(this.getEntityId("category1"));

		final Object testingData[][] = {
			//principal expected exception
			{
				"user1", c1, null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listCategoryRendezvous((String) testingData[i][0], (Category) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void listCategoryRendezvous(final String principal, final Category c, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(principal);

			final Collection<Rendezvous> rendezvouses = this.rendezvousService.findRendezvousByCategories(c.getId());
			Assert.isTrue(rendezvouses.size() == 2);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptions(expectedException, caught);
	}
}
