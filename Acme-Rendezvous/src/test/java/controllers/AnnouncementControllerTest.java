
package controllers;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.View;

import services.AnnouncementService;
import domain.Announcement;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml", "classpath:spring/junit.xml"
})
@WebAppConfiguration
@Transactional
public class AnnouncementControllerTest {

	@Mock
	View					view;
	@Mock
	AnnouncementService		announcementService;

	@InjectMocks
	AnnouncementController	announcementController;

	MockMvc					mockMvc;


	@Before
	public void onSetUp() throws Exception {

		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.announcementController).build();

	}

	@Test
	public void testListAvailableServices() throws Exception {
		final AnnouncementService as = Mockito.mock(AnnouncementService.class);
		final Collection<Announcement> announcements = as.findAll();
		final Announcement announcement = as.findOne(77);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/service/manager/list.do")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("service/list"))
			.andExpect(MockMvcResultMatchers.model().attribute("requestURI", "service/manager/list.do")).andExpect(MockMvcResultMatchers.model().attribute("announcements", announcements));

	}
	//	@Test
	//	public void testListCreatedServices() throws Exception {
	//
	//		final Manager manager = this.mockManagerService.findOne(74);
	//
	//		final Collection<Service> servicios = manager.getServices();
	//
	//		this.mockMvc.perform(MockMvcRequestBuilders.get("/service/manager/list-created.do")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("service/list"))
	//			.andExpect(MockMvcResultMatchers.model().attribute("requestURI", "service/manager/list.do")).andExpect(MockMvcResultMatchers.model().attribute("services", servicios));
	//
	//	}
}
