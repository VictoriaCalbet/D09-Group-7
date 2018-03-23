
package controllers;

import java.util.Collection;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import services.RendezvousService;
import services.UserService;
import utilities.AbstractTest;
import domain.Rendezvous;
import domain.User;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class RendezvousControllerTest extends AbstractTest {

	private MockMvc					mockMvc;

	@Autowired
	private RendezvousController	rendezvousController;

	@Autowired
	private UserService				userService;

	@Autowired
	private RendezvousService		rendezvousService;


	@Override
	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.rendezvousController).build();
	}

	@Test
	public void testListRendezvouses() throws Exception {

		this.authenticate("user1");

		final User principal = this.userService.findByPrincipal();
		final Collection<Rendezvous> rendezvouses = this.rendezvousService.findAllPrincipalRsvps(principal.getId());

		this.mockMvc.perform(MockMvcRequestBuilders.get("/rendezvous/list")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("rendezvous/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("requestURI", "rendezvous/list.do")).andExpect(MockMvcResultMatchers.model().attribute("principalRendezvouses", Matchers.hasSize(rendezvouses.size())));
		this.unauthenticate();
	}

	@Test
	public void testListCategory() throws Exception {

		this.mockMvc.perform(((MockMvcRequestBuilders.post("/rendezvous/listCategory").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))));

	}
}
