/*
 * SampleTest.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.View;

import services.ServiceService;
import services.UserService;
import utilities.AbstractTest;
import controllers.user.RequestUserController;
import domain.Service;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(MockitoJUnitRunner.class)
@WebAppConfiguration
public class ServicetUserControllerTest extends AbstractTest {

	@Mock
	ServiceService			mockServiceService;
	@Mock
	View					view;
	@InjectMocks
	RequestUserController	requestUserController;
	MockMvc					mockmvc;

	@Autowired
	UserService				userService;


	@Before
	public void onSetUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.mockmvc = MockMvcBuilders.standaloneSetup(this.requestUserController).build();
	}

	@Test
	public void testListAvailableServices() throws Exception {

		final Collection<Service> services = this.mockServiceService.findAvailableServices();

		this.mockmvc.perform(get
//		mockmvc.perform(get("/service/user/list.do"))
//		.andExpect(status().ok()).andExpect(view()
//		.name("service/list")).andExpect(model()
//		.attribute("requestURI", "service/user/list.do"))
//		.andExpect(model().attribute("services", services));

		//		

	}
}
