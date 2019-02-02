//Description: This class tests controllers in PropertyController
package com.apartmentReservation.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.ModelAndView;
import com.apartmentReservation.model.Properties;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PropertyControllerTest { 
	@Mock
	private MockHttpServletRequest request;
	@Mock
	private HttpSession session;
	@Autowired
	private PropertyController propertyController;
	Authentication auth = Mockito.mock(Authentication.class);
	SecurityContext securityContext = Mockito.mock(SecurityContext.class);
	
	@Before
	public void setupTest() {
		MockitoAnnotations.initMocks(this);
	}


	/*
	 * Description: Test if the application displays all apartments
	 * input:HttpSession 
	 * output: checking model attributes propertiesSize and model key prop
	 */
	@Test
	public void testForDisplayingAllApts() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userName")).thenReturn("Vishnu Vardhan Oliveti");
		when(session.getAttribute("userRole")).thenReturn("CUSTOMER");
		ModelAndView model = new ModelAndView();
		model = propertyController.displayProperties(request);
		assertEquals(90, model.getModel().get("propertiesSize"));
		assertTrue(model.getModel().containsKey("prop"));
	}

	/*
	 * Description: Test to get preferred apartments by settings some preferences.
	 * input: propertyFilter output:checking model attribute filteredPropertiesSize,
	 * model key prop
	 */
	@Test
	public void testForDisplayingAptsByPreferences() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userName")).thenReturn("Vishnu Vardhan Oliveti");
		when(session.getAttribute("userRole")).thenReturn("CUSTOMER");
		Properties propertyFilter = Properties.builder().propertyType("Apartment").propertyBedrooms(2)
				.propertyLocation("Select").propertyBath(0).propertyRent(0).propertyUtility(2).propertyPets(2).build();
		Properties propertyFilter2 = Properties.builder().propertyType("Apartment").propertyBedrooms(0)
				.propertyLocation("Select").propertyBath(0).propertyRent(0).propertyUtility(1).propertyPets(2).build();
		Properties propertyFilter3 = Properties.builder().propertyType("Select").propertyBedrooms(0)
				.propertyLocation("Select").propertyBath(0).propertyRent(1000).propertyUtility(2).propertyPets(2)
				.build();
		ModelAndView model = new ModelAndView();
		model = propertyController.findApts(propertyFilter,request);
		assertTrue(model.getModel().containsKey("prop"));
		assertEquals(17, model.getModel().get("filteredPropertiesSize"));
		ModelAndView model2 = new ModelAndView();
		model2 = propertyController.findApts(propertyFilter2,request);
		assertEquals(26, model2.getModel().get("filteredPropertiesSize"));
		ModelAndView model3 = new ModelAndView();
		model3 = propertyController.findApts(propertyFilter3,request);
		assertEquals(4, model3.getModel().get("filteredPropertiesSize"));
	}

	/*
	 * Description: Testing for the property description when we pass a parameter as
	 * the propertyId. input: property id and HttpSession output: intended page to
	 * be displayed and viewed by customer
	 */
	@Test
	public void testForPropertyDescription() {
		when(securityContext.getAuthentication()).thenReturn(auth);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userName")).thenReturn("Vishnu Vardhan Oliveti");
		when(session.getAttribute("userRole")).thenReturn("CUSTOMER");
		SecurityContextHolder.setContext(securityContext);
		when(auth.getName()).thenReturn("vishnu@gmail.com");
		ModelAndView model = propertyController.propertyDescription(2, request);
		assertEquals("user/propertyDescription", model.getViewName());
	}
	
	@Test
	public void testForPropertyRatingsForSingleProperty() {
		when(securityContext.getAuthentication()).thenReturn(auth);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userName")).thenReturn("Vishnu Vardhan Oliveti");
		when(session.getAttribute("userRole")).thenReturn("CUSTOMER");
		SecurityContextHolder.setContext(securityContext);
		when(auth.getName()).thenReturn("vishnu@gmail.com");
		Double avgRating = propertyController.getRatingsForSingleProperty("Double Tree");
		ModelAndView model = propertyController.propertyDescription(6, request);
		assertEquals(avgRating,model.getModel().get("ratingByPropertyName"));
	}
}
