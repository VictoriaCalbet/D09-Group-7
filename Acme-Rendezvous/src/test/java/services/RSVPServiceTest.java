
package services;

import java.util.Date;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Rendezvous;

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
	 * Create and save a new service
	 * Test 1: Positive case --> expected: Request a services Successful
	 * Test 2: Negative case. Fail requesting a service
	 */

	@Test
	public void testCreateRsvpDriver() {

		final Rendezvous r1 = this.rendezvousService.findOne(this.getEntityId("rendezvous1"));
		final Rendezvous r2 = this.rendezvousService.findOne(this.getEntityId("rendezvous2"));
		final Rendezvous r4 = this.rendezvousService.findOne(this.getEntityId("rendezvous4"));

		//The creator of r1 is user1
		// User1 and user2 are adults, user3 is not adult
		final Object testingData[][] = {

			//userPrincipal, rendezvous, meetingMoment, isAdult, isDraft, isDeleted, exception

			{
				//Positive test1: An user(not the creator) rsvp a rendezvous
				//Positive test2: An user rsvp a rendezvous still not rsvped
				//Positive test3: An young user rsvp a rendezvous not for adults,not draft and not deleted

				"user3", r2, new DateTime().plusHours(1).toDate(), false, false, false, null
			}, {
				//Positive test4: An adult rsvp a rendezvous only for adults
				"user2", r2, new DateTime().plusHours(1).toDate(), true, false, false, null
			},

			{
				//Negative test5: A young user rsvp a rendezvous only for adults
				"user3", r2, new DateTime().plusHours(1).toDate(), true, false, false, IllegalArgumentException.class
			},

			{
				//Negative test6: A user rsvp a draft rendezvous
				"user3", r2, new DateTime().plusHours(1).toDate(), false, true, false, IllegalArgumentException.class
			},

			{
				//Negative test7: A user rsvp a deleted rendezvous
				"user3", r2, new DateTime().plusHours(1).toDate(), false, false, true, IllegalArgumentException.class
			}, {
				//Negative test8: An adult user rsvp an adult rendezvous but deleted
				"user3", r2, new DateTime().plusHours(1).toDate(), true, false, true, IllegalArgumentException.class
			}, {
				//Negative test9: An adult user rsvp an adult rendezvous but draft
				"user3", r2, new DateTime().plusHours(1).toDate(), true, true, false, IllegalArgumentException.class
			}, {
				//Negative test10: The creator of the rendezvous try to rsvp his own rendezvous again
				"user1", r1, new DateTime().plusHours(1).toDate(), false, false, false, IllegalArgumentException.class
			}, {
				//Negative test11: A user(not the creator) try to rsvp a rendezvous already rsvped
				"user2", r4, new DateTime().plusHours(1).toDate(), false, false, false, IllegalArgumentException.class
			}

			, {
				//Negative test12: A user try to rsvp a past rendezvous
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
}
