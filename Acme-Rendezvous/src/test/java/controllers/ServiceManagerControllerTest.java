
package controllers;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.View;

import services.ServiceService;
import controllers.manager.ServiceManagerController;
import domain.Service;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml", "classpath:spring/junit.xml"
})
@WebAppConfiguration
public class ServiceManagerControllerTest {

	@Mock
	ServiceService				serviceServiceMock;
	@Mock
	View						view;

	@InjectMocks
	ServiceManagerController	serviceManagerController;
	MockMvc						mockMvc;


	@Before
	public void setup() throws Exception {
		final ServiceManagerController serviceManagerController = new ServiceManagerController();

		MockitoAnnotations.initMocks(this);

		this.mockMvc = MockMvcBuilders.standaloneSetup(serviceManagerController).build();
	}

	@Test
	public void testListAvailableServices() throws Exception {

		final Collection<Service> services2 = this.serviceServiceMock.findAvailableServices();
		final Service service = this.serviceServiceMock.findOne(77);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/service/manager/list.do")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("service/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("requestURI", "service/manager/list.do")).andExpect(MockMvcResultMatchers.model().attribute("services2", services2));

	}
}
