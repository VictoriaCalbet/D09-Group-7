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

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.View;

import services.ServiceService;
import utilities.AbstractTest;
import controllers.user.RequestUserController;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(MockitoJUnitRunner.class)
@Transactional
public class RequestUserControllerTest extends AbstractTest {

	@Mock
	ServiceService			mockServiceService;
	@Mock
	View					view;
	@InjectMocks
	RequestUserController	requestUserController;
	MockMvc					mockmvc;


	@Before
	public void onSetUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.mockmvc = MockMvcBuilders.standaloneSetup(this.requestUserController).build();
	}
}
