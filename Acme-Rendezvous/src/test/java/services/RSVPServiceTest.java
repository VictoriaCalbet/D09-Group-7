
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
import domain.RSVP;
import domain.Rendezvous;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class RSVPServiceTest extends AbstractTest {

	// The SUT (Service Under Test) -------------------------------------------

	@Autowired
	private RequestService		requestService;

	@Autowired
	private RendezvousService	rendezvousService;

	@Autowired
	private RSVPService			rsvpService;

	@Autowired
	private UserService			userService;


	// Tests ------------------------------------------------------------------
	/**
	 * Acme-Rendezvous 1.0: Requirement 5.4
	 * 
	 * RSVP a rendezvous.
	 * 
	 * Notes: The creator of r1 is user1. User1 and user2 are adults, user3 is not adult
	 * 
	 * Positive test1: An user(not the creator) rsvp a rendezvous
	 * Positive test2: An user rsvp a rendezvous still not rsvped
	 * Positive test3: An young user rsvp a rendezvous not for adults,not draft and not deleted
	 * Positive test4: An adult rsvp a rendezvous only for adults
	 * Negative test1: A young user rsvp a rendezvous only for adults
	 * Negative test2: A user rsvp a draft rendezvous
	 * Negative test3: A user rsvp a deleted rendezvous
	 * Negative test4: An adult user rsvp an adult rendezvous but deleted
	 * Negative test5: An adult user rsvp an adult rendezvous but draft
	 * Negative test6: The creator of the rendezvous try to rsvp his own rendezvous again
	 * Negative test7: A user(not the creator) try to rsvp a rendezvous already rsvped
	 * Negative test8: A user try to rsvp a past rendezvous
	 */
	@Test
	public void testRsvpDriver() {

		final Rendezvous r1 = this.rendezvousService.findOne(this.getEntityId("rendezvous1"));
		final Rendezvous r2 = this.rendezvousService.findOne(this.getEntityId("rendezvous2"));
		final Rendezvous r4 = this.rendezvousService.findOne(this.getEntityId("rendezvous4"));

		final Object testingData[][] = {

			/** userPrincipal, rendezvous, meetingMoment, isAdult, isDraft, isDeleted, exception */
			{
				"user3", r2, new DateTime().plusHours(1).toDate(), false, false, false, null
			}, {
				"user2", r2, new DateTime().plusHours(1).toDate(), true, false, false, null
			}, {
				"user3", r2, new DateTime().plusHours(1).toDate(), true, false, false, IllegalArgumentException.class
			}, {
				"user3", r2, new DateTime().plusHours(1).toDate(), false, true, false, IllegalArgumentException.class
			}, {
				"user3", r2, new DateTime().plusHours(1).toDate(), false, false, true, IllegalArgumentException.class
			}, {
				"user3", r2, new DateTime().plusHours(1).toDate(), true, false, true, IllegalArgumentException.class
			}, {
				"user3", r2, new DateTime().plusHours(1).toDate(), true, true, false, IllegalArgumentException.class
			}, {
				"user1", r1, new DateTime().plusHours(1).toDate(), false, false, false, IllegalArgumentException.class
			}, {
				"user2", r4, new DateTime().plusHours(1).toDate(), false, false, false, IllegalArgumentException.class
			}, {
				"user2", r4, new DateTime(2017, 8, 21, 0, 0).toDate(), false, false, false, IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.testCreateRsvpTemplate((String) testingData[i][0], (Rendezvous) testingData[i][1], (Date) testingData[i][2], (boolean) testingData[i][3], (boolean) testingData[i][4], (boolean) testingData[i][5], (Class<?>) testingData[i][6]);
	}
	protected void testCreateRsvpTemplate(final String username, final Rendezvous rendezvous, final Date meetingMoment, final boolean isAdult, final boolean isDraft, final boolean isDeleted, final Class<?> expectedException) {
		Class<?> caught;
		caught = null;

		try {
			this.authenticate(username);

			final Rendezvous rendezvousToRsvp = rendezvous;
			rendezvousToRsvp.setIsAdultOnly(isAdult);
			rendezvousToRsvp.setIsDraft(isDraft);
			rendezvousToRsvp.setMeetingMoment(meetingMoment);
			rendezvousToRsvp.setIsDeleted(isDeleted);
			this.rendezvousService.save(rendezvousToRsvp);
			this.rsvpService.RSVPaRendezvous(rendezvousToRsvp.getId());
			this.unauthenticate();
			this.requestService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptions(expectedException, caught);
	}

	/**
	 * Requirement 5.4 Acme-Rendezvous
	 * 
	 * Cancel a rendezvous
	 * 
	 * Positive test1: A user cancels a rsvp he already rsvped
	 * Negative test1: A user cancels a rsvp he has not rsvped
	 */

	@Test
	public void testCancelRsvpDriver() {

		final Rendezvous r2 = this.rendezvousService.findOne(this.getEntityId("rendezvous2"));
		final Rendezvous r6 = this.rendezvousService.findOne(this.getEntityId("rendezvous6"));

		final Object testingData[][] = {

			/** userPrincipal, rendezvous, exception */
			{
				"user1", r2, null
			}, {

				"user1", r6, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testCancelRsvpTemplate((String) testingData[i][0], (Rendezvous) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	protected void testCancelRsvpTemplate(final String username, final Rendezvous rendezvous, final Class<?> expectedException) {
		Class<?> caught;
		caught = null;

		try {
			this.authenticate(username);

			final Rendezvous rendezvousToCancelRsvp = rendezvous;

			this.rsvpService.cancelRSVP(rendezvousToCancelRsvp.getId());
			this.requestService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptions(expectedException, caught);
	}

	@Test
	public void testListRsvpDriver() {

		final Object testingData[][] = {

			/** userPrincipal */
			{
				"user1", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testListRsvpTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}
	protected void testListRsvpTemplate(final String username, final Class<?> expectedException) {
		Class<?> caught;
		caught = null;

		try {
			this.authenticate(username);
			final User principal = this.userService.findOne(this.getEntityId(username));
			final Collection<RSVP> rsvps = principal.getRsvps();
			Assert.isTrue(rsvps.size() == 4);
			this.requestService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		} finally {
			this.unauthenticate();
		}

		this.checkExceptions(expectedException, caught);
	}
}
