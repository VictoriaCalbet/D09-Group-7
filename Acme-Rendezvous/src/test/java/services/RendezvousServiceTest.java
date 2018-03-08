
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Rendezvous;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class RendezvousServiceTest extends AbstractTest {

	@Autowired
	private RendezvousService	rendezvousService;
	@Autowired
	private UserService			userService;


	// Tests ------------------------------------------------------------------

	@Test
	public void testCreate() {
		//login
		this.authenticate("user2");

		Rendezvous r;
		r = this.rendezvousService.create();

		Assert.isTrue(r.getCreator().getUserAccount().getUsername().equals("user2"));

		r.setName("Dolar");
		r.setDescription("descripcion1");
		r.setMeetingMoment(new DateTime().plusDays(10).toDate());
		r.setPicture("http://jsequeiros.com/comprimir-fotografias-e-imagenes-con-microsoft-office-picture-manager.html");
		r.setGpsPoint(null);
		r.setIsAdultOnly(false);
		r.setIsDeleted(false);
		r.setIsDraft(false);

		this.rendezvousService.save(r);

		this.unauthenticate();

	}

	@Test
	public void testSaveFromCreate() {
		this.authenticate("user2");

		Rendezvous r;
		r = this.rendezvousService.create();

		r.setName("Rendezvous34");
		r.setIsAdultOnly(true);

		this.rendezvousService.save(r);

		this.unauthenticate();

	}

	@Test
	/**
	 * Test: list a rendezvouses of user.
	 */
	public void testListRendezvous() {
		//login
		this.authenticate("user2");

		final User u = this.userService.findByPrincipal();

		final Collection<Rendezvous> r = u.getRendezvoussesCreated();
		Assert.isTrue(r.size() >= 0);

		this.unauthenticate();

	}

	@Test
	/**
	 * Test: list all  rendezvouses.
	 */
	public void testListAllRendezvous() {

		final Collection<Rendezvous> r = this.rendezvousService.findAll();
		Assert.notEmpty(r);

	}

	@Test
	/**
	 * Test: delete a rendezvous.
	 */
	public void testDeleteRendezvous() {
		//login
		this.authenticate("user3");

		Rendezvous r;
		r = this.rendezvousService.create();

		Assert.isTrue(r.getCreator().getUserAccount().getUsername().equals("user3"));

		r.setName("Dolar");
		r.setDescription("descripcion1");
		r.setMeetingMoment(new DateTime().plusDays(10).toDate());
		r.setPicture("http://jsequeiros.com/comprimir-fotografias-e-imagenes-con-microsoft-office-picture-manager.html");
		r.setGpsPoint(null);
		r.setIsAdultOnly(false);
		r.setIsDeleted(false);
		r.setIsDraft(true);

		final Rendezvous r1 = this.rendezvousService.save(r);

		final User u = this.userService.findByPrincipal();
		Assert.isTrue(r1.getCreator().equals(u));
		Assert.isTrue(r.getIsDraft());
		Assert.isTrue(!r.getIsDeleted());
		r.setIsDeleted(true);

		this.rendezvousService.delete(r1.getId());
		this.unauthenticate();
	}

}
