
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
	 * 
	 * Requirement 5.2:
	 * 
	 * Create a rendezvous, which he's implicitly assumed to attend.
	 * Note that a user may edit his or her rendezvouses as long as
	 * they aren't saved them in final mode. Once a rendezvous is saved
	 * in final mode, it cannot be edited or deleted by the creator.
	 * 
	 * 1� Good test -> expected: rendezvous created
	 * 2� Bad test -> cannot create rendezvous with past date
	 * 3� Bad test -> an user not adult cannot create rendezvous with isAdult = true
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
	 * 1� Good test -> expected: rendezvous edited
	 * 2� Bad test -> cannot edit a rendezvous with past date
	 * 3� Bad test -> cannot edit a deleted rendezvous
	 * 4� Bad test -> an admin cannot edit a rendezvous
	 * 5� Bad test -> an user cannot edit rendezvous if this rendezvous isn't own.
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
	 * 1� Good test -> expected: rendezvous deleted
	 * 2� Bad test -> cannot delete a rendezvous with mode final
	 * 3� Bad test -> cannot delete a rendezvous deleted
	 * 4� Bad test -> an manager cannot delete a rendezvous
	 * 5� Bad test -> an user cannot delete rendezvous if this rendezvous isn't own.
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
	 * 1� Good test -> expected: rendezvous deleted from database
	 * 2� Bad test -> an user cannot delete a rendezvous from database
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
	 * 1� Good test -> expected: rendezvous linked
	 * 2� Bad test -> cannot linked the rendezvous because it's already linked
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
	 * 1� Good test -> expected: results shown
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

			Assert.notNull(rendezvouses);

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
	 * 1� Good test -> expected: results shown
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

			Assert.notNull(rendezvouses);

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
	 * 1� Good test -> expected: results shown
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

			Assert.notNull(rendezvouses);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptions(expectedException, caught);
	}
}
