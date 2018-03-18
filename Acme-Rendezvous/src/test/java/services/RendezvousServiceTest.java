
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


	// Tests ------------------------------------------------------------------

	/***
	 * Create rendezvous
	 * 1º Good test -> expected: rendezvous created
	 * 2º Bad test -> cannot create rendezvous with past date
	 * 3º Bad test -> an user not adult cannot create rendezvous with isAdult = true
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
		}
		this.checkExceptions(expectedException, caught);
	}
	/***
	 * Edit rendezvous
	 * 1º Good test -> expected: rendezvous edited
	 * 2º Bad test -> cannot edit a rendezvous with past date
	 * 3º Bad test -> cannot edit a deleted rendezvous
	 * 4º Bad test -> an admin cannot edit a rendezvous
	 * 5º Bad test -> an user cannot edit rendezvous if this rendezvous isn't own.
	 */
	@Test
	public void testEditRendezvous() {
		final Object[][] testingData = {
			// principal, name, description, meetingMoment, picture,latitude, longitude, isAdult, isDraft, expected exception
			{
				"user1", "rendezvous1", new DateTime().plusHours(1).toDate(), false, 82, null
			}, {
				"user1", "rendezvous1", new DateTime().plusDays(-10).toDate(), false, 82, IllegalArgumentException.class
			}, {
				"user1", "rendezvous1", new DateTime().plusDays(10).toDate(), false, 81, IllegalArgumentException.class
			}, {
				"admin", "rendezvous1", new DateTime().plusDays(10).toDate(), false, 83, IllegalArgumentException.class
			}, {
				"user3", "rendezvous1", new DateTime().plusDays(2).toDate(), false, 82, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editRendezvousTemplated((String) testingData[i][0], (String) testingData[i][1], (Date) testingData[i][2], (boolean) testingData[i][3], (int) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	protected void editRendezvousTemplated(final String principal, final String name, final Date meetingMoment, final boolean isAdult, final int rendezvousId, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(principal);
			final Rendezvous r = this.rendezvousService.findOne(rendezvousId);
			r.setName(name);
			r.setIsAdultOnly(isAdult);
			r.setMeetingMoment(meetingMoment);
			r.setGpsPoint(new GPSPoint());

			this.rendezvousService.update(r);
			this.unauthenticate();
			this.rendezvousService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expectedException, caught);
	}

	/***
	 * Delete rendezvous
	 * 1º Good test -> expected: rendezvous deleted
	 * 2º Bad test -> cannot delete a rendezvous with mode final
	 * 3º Bad test -> cannot delete a rendezvous deleted
	 * 4º Bad test -> an manager cannot delete a rendezvous
	 * 5º Bad test -> an user cannot delete rendezvous if this rendezvous isn't own.
	 */
	@Test
	public void testDeleteRendezvous() {
		final Object[][] testingData = {
			//actor, rendezvousId, expected exception
			{
				"user1", 82, null
			}, {
				"user1", 80, IllegalArgumentException.class
			}, {
				"user1", 81, IllegalArgumentException.class
			}, {
				"manager1", 82, IllegalArgumentException.class
			}, {
				"user3", 82, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteRendezvousTemplated((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void deleteRendezvousTemplated(final String principal, final int rendezvousId, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(principal);
			this.rendezvousService.delete(rendezvousId);
			this.unauthenticate();
			this.rendezvousService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expectedException, caught);
	}

	/***
	 * Delete rendezvousAdmin
	 * 1º Good test -> expected: rendezvous deleted from database
	 * 2º Bad test -> an user cannot delete a rendezvous from database
	 */
	@Test
	public void testDeleteRendezvousAdmin() {
		final Object[][] testingData = {
			//actor, rendezvousId, expected exception
			{
				"admin", 80, null
			}, {
				"user1", 80, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteRendezvousAdminTemplated((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void deleteRendezvousAdminTemplated(final String principal, final int rendezvousId, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(principal);
			this.rendezvousService.deleteAdmin(rendezvousId);
			this.unauthenticate();
			this.rendezvousService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expectedException, caught);
	}

	/***
	 * Link rendezvous
	 * 1º Good test -> expected: rendezvous linked
	 * 2º Bad test -> cannot linked the rendezvous because it's already linked
	 */
	@Test
	public void testLinkRendezvous() {
		final Object[][] testingData = {
			//actor, rendezvousId, rendezvousId, expected exception
			{
				"user1", 80, 82, null
			}, {
				"user1", 80, 84, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.linkRendezvousTemplated((String) testingData[i][0], (int) testingData[i][1], (int) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	protected void linkRendezvousTemplated(final String principal, final int rendezvousId, final int rendezvousLinkedToId, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(principal);
			final Rendezvous r1 = this.rendezvousService.findOne(rendezvousId);
			final Rendezvous r2 = this.rendezvousService.findOne(rendezvousLinkedToId);
			this.rendezvousService.linked(r1, r2);
			this.unauthenticate();
			this.rendezvousService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expectedException, caught);
	}

	/***
	 * List rendezvous
	 * Testing cases:
	 * 1º Good test -> expected: results shown
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
		}

		this.checkExceptions(expectedException, caught);
	}

	/***
	 * List linked rendezvous
	 * Testing cases:
	 * 1º Good test -> expected: results shown
	 */

	@Test
	public void listLinkedRendezvous() {

		final Object testingData[][] = {
			//principal expected exception
			{
				"user1", 80, null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listLinkedRendezvous((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void listLinkedRendezvous(final String principal, final int rendezvousId, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(principal);

			final Collection<Rendezvous> rendezvouses = this.rendezvousService.findRendezvousSimilarLogged(rendezvousId);

			Assert.notNull(rendezvouses);

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expectedException, caught);
	}

	/***
	 * List rendezvous by category
	 * Testing cases:
	 * 1º Good test -> expected: results shown
	 */

	@Test
	public void listCategoryRendezvous() {

		final Object testingData[][] = {
			//principal expected exception
			{
				"user1", 90, null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listCategoryRendezvous((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	protected void listCategoryRendezvous(final String principal, final int categoryId, final Class<?> expectedException) {
		Class<?> caught = null;

		try {
			this.authenticate(principal);

			final Collection<Rendezvous> rendezvouses = this.rendezvousService.findRendezvousByCategories(categoryId);

			Assert.notNull(rendezvouses);

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expectedException, caught);
	}
}
