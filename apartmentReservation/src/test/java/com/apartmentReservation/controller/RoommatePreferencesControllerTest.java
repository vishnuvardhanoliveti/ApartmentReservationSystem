//Description: This class is used to test various methods under RoommatePreferencesController.

package com.apartmentReservation.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import com.apartmentReservation.model.RoommatePreferences;
import com.apartmentReservation.model.User;
import com.apartmentReservation.repository.RoommatePreferencesRepository;
import com.apartmentReservation.service.PropertyService;
import com.apartmentReservation.service.RoommatePreferencesService;
import com.apartmentReservation.service.SharedApartmentService;
import com.apartmentReservation.service.UserService;
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class RoommatePreferencesControllerTest {
	@Mock
	private MockHttpServletRequest request;
	@Mock
	Map<Integer, List<Map<String, String>>> matchedResults;
	@Mock
	private HttpSession session;
	@Mock
	private ModelMap model1;
	@Mock
	private UserService userService;
	@Mock
	private PropertyService propertyService;
	@Mock
	private RoommatePreferencesService roommatePreferencesService;
	@Mock

	private SharedApartmentService sharedApartmentService;
	@Mock
	private RoommatePreferencesRepository roommatePreferencesRepository;
	@InjectMocks
	private RoommatePreferencesController roommatePreferencesController;
	
	//Creating a mocked instance of roommatePreferences object.
	RoommatePreferences mockedroommatePreferences = RoommatePreferences.builder()
													 .id(1)
													 .userId(1)
													 .ageGroup("15-30")
													 .vegan(true)
													 .gender("Male")
													 .drinking(false)
													 .smoking(false)
													 .pets(true)
													 .location("Avenue D")
													 .build();
	//Creating a mocked instance of user object.
	User user = User.builder()
				 .id(1)
				 .email("virat@gmail.com")
				 .dob("1978-01-01")
				 .build();
	
	Map<Integer, List<Map<String, String>>> mockedMatchedPreferences;
	
	 Map<String,String> preferencesMap = new HashMap<String, String>();
	 //List<Map<String,String>> = 
	//This method runs before every test
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	
	}
	
	/**
	 * Description:This method is used to test roommatePreferencesDisplayForm method.
	
	 * @Input:mocked request
	 * @Output: This method verifies msg and  expected viewName
	 * @author: Vishnu Vardhan Oliveti
	 * Written on:November 2,2018
	 * Modified on: November 6, 2018
	 */
	@Test
	public void testroommatePreferencesDisplayForm() {
		
		//mocking the requests and session attributes
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userName")).thenReturn("Virat Kohli");
		when(session.getAttribute("userRole")).thenReturn("CUSTOMER");
		when(session.getAttribute("userEmail")).thenReturn("virat@gmail.com");
		
		//mocking the user service to return the mocked user
		when(userService.findUserByEmail("virat@gmail.com")).thenReturn(user);
		
		//mocking the roommatePreferences service to return mocked roommatePreferences object.
		when(roommatePreferencesService.findRoommatePreferencesById(1)).thenReturn(mockedroommatePreferences);
		
		ModelAndView model = new ModelAndView();
		model = roommatePreferencesController.roommatePreferencesDisplayForm(request);
		
		//Assert statements
		assertEquals("Preferences are already set.", model.getModel().get("msg"));
		assertEquals("user/roommatePreferences", model.getViewName());
	}
	
	/**
	 * Description:This method is used to test roommatePreferencesSubmitForm method.
	
	 * @Input:mocked request and mocked roommatePreferences object
	 * @Output: This method verifies msg , expected viewName and if the model object contains roommatePreferences as key.
	 * @author: Vishnu Vardhan Oliveti
	 * Written on:November 2,2018
	 * Modified on: November 6, 2018
	 */
	@Test
	public void testroommatePreferencesSubmitForm() {
		//mocking the requests and session attributes
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userName")).thenReturn("Virat Kohli");
		when(session.getAttribute("userRole")).thenReturn("CUSTOMER");
		when(session.getAttribute("userEmail")).thenReturn("virat@gmail.com");
		when(userService.findUserByEmail("virat@gmail.com")).thenReturn(user);
		
		//mocking the roommatePreferences service to return null.
		when(roommatePreferencesService.findRoommatePreferencesById(1)).thenReturn(null);
		
		ModelAndView model = new ModelAndView();
		model = roommatePreferencesController.roommatePreferencesSubmitForm(mockedroommatePreferences,request);
		
		//Assert statements
		assertEquals("Details updated successfully!", model.getModel().get("msg"));
		assertTrue(model.getModel().containsKey("roommatePreferences"));
		assertEquals("user/roommatePreferences", model.getViewName());
	}
	/**
	 * Description:This method is used to test roommatePreferencesDisplayForm method.
	 * @Input:mocked request
	 * @Output: This method verifies msg and  expected viewName
	 * @author: Vishnu Vardhan Oliveti
	 * Written on:November 2,2018
	 * Modified on: November 6, 2018
	 */
	@Test
	public void testroommatePreferencesSearchDisplayForm() {
		//mocking the requests and session attributes
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userName")).thenReturn("Virat Kohli");
		when(session.getAttribute("userRole")).thenReturn("CUSTOMER");
		when(session.getAttribute("userEmail")).thenReturn("virat@gmail.com");
		
		when(userService.findUserByEmail("virat@gmail.com")).thenReturn(user);
		when(roommatePreferencesService.findRoommatePreferencesById(1)).thenReturn(mockedroommatePreferences) ;
		when(roommatePreferencesService.getMatchedPreferences(mockedroommatePreferences,"newRoommateSearch")).thenReturn(mockedMatchedPreferences);
		
		ModelAndView model = new ModelAndView();
		model = roommatePreferencesController.roommatePreferencesSearchDisplayForm(request);
		//assert Statements
		assertTrue(model.getModel().containsKey("matchedResults"));
		assertEquals("user/roommatePreferencesSearch", model.getViewName());
	}
	/**
	 * Description:This method is used to test VisitngCustomerRoommateSearchDisplayForm method.
	
	 * @Input:mocked request
	 * @Output: This method verifies expected viewName
	 * @author: Kocherla Shaline
	 * Written on:November 23,2018
	 */
	@Test
	public void testVisitngCustomerRoommateSearchDisplayForm() {
		//mocking the requests and session attributes
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userName")).thenReturn("Virat Kohli");
		ModelAndView model = new ModelAndView();
		model = roommatePreferencesController.visitngCustomerRoommateSearchDisplayForm(request);
		//assert Statements
		assertTrue(model.getModel().containsKey("userName"));
		 assertTrue(model.getModel().containsValue("Virat Kohli"));
		assertEquals("user/roommatePreferencesSearchVisitingCustomer", model.getViewName());
		
	}
	/**
	 * Description:This method is used to test VisitngCustomerRoommateSearchSubmitForm method.
	
	 * @Input:mockedroommatePreferences and mocked request
	 * @Output: This method verifies expected viewName
	 * @author: Kocherla Shaline
	 * Written on:November 23,2018
	 */
	@Test
	public void testVisitingCustomerRoommateSearchSubmitForm() {
		//mocking the requests and session attributes
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userName")).thenReturn("Virat Kohli");
		ModelAndView model = new ModelAndView();
		model = roommatePreferencesController.visitingCustomerRoommateSearchSubmitForm(mockedroommatePreferences,request);
		//assert Statements
		assertTrue(model.getModel().containsKey("userName"));
		 assertTrue(model.getModel().containsValue("Virat Kohli"));
		assertEquals("user/roommatePreferencesSearchVisitingCustomer", model.getViewName());
	
	}
	/**
	 * Description:This method is used to test SetDataInModel method.
	
	 * @Input:id, string, mockedroommatePreferences and mocked request
	 * @Output: This method verifies expected String
	 * @author: Kocherla Shaline
	 * Written on:November 23,2018
	 */
	@Test
	public void testSetDataInModel() {
		int id=1;
		String s="sharedPropertiesSearch";
		//mocking the requests and session attributes
		when(request.getSession()).thenReturn(session);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userEmail")).thenReturn("virat@gmail.com");
		
		when(userService.findUserByEmail("virat@gmail.com")).thenReturn(user);
		when(roommatePreferencesService.findRoommatePreferencesById(user.getId())).thenReturn(mockedroommatePreferences);
		when(roommatePreferencesService.getMatchedPreferences(mockedroommatePreferences,s)).thenReturn(matchedResults);
		when(sharedApartmentService.addPropertyDetailsToResults(matchedResults)).thenReturn(matchedResults);
		String output= roommatePreferencesController.setDataInModal(id,s,request, model1);
		//assert Statements
		assertEquals("template :: roommatePreferenceModal",output);
	
	}
}

