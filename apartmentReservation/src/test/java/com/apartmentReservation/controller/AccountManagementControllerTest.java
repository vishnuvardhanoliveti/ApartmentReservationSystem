//Description: Testing various methods in Account management controller
package com.apartmentReservation.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import com.apartmentReservation.model.User;
import com.apartmentReservation.service.AccountManagementService;
import com.apartmentReservation.service.UserService;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)

public class AccountManagementControllerTest {
	Authentication auth = Mockito.mock(Authentication.class);
	SecurityContext securityContext = Mockito.mock(SecurityContext.class);
	@Mock
	private UserService userservice;
	@Mock
	private BindingResult bindingResult;
	@Mock
	private MockHttpServletRequest request;
	@Mock
	private HttpSession session;
	@Mock
	private AccountManagementService accountManagementService;
	@InjectMocks
	private AccountManagementController accountManagementcontroller;
	//this method is run before all tests
	@Before
	public void setupTest() {
		MockitoAnnotations.initMocks(this);
	}
	//Creating a mocked instance of user object.
	User newUser = User.builder().email("test@gmail.com").firstName("Test").lastName("last")
			.password("password").phoneNumber("646-240-7834").addressLine1("2001 west hickory street")
			.addressLine2("park place apartments").city("texas").state("Texas").zip("76201").build();
	/**
	 * Description:This method is used to testing if user after changing his details will be redirected to following page
	
	 * @Input:mocked request
	 * @Output: This method verifies String that is returned
	 * @author: Kocherla Shaline
	 * Written on:November 5,2018
	 * Modified on: November 6, 2018
	 */
	@Test
	public void testaccountManagament() {
		//mocking session and request attribute
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userName")).thenReturn("Test");
		when(session.getAttribute("userRole")).thenReturn("CUSTOMER");
		when(session.getAttribute("userEmail")).thenReturn("test@gmail.com");
		//Mocking userservice findUserByEmail to return mocked user
		when(userservice.findUserByEmail("test@gmail.com")).thenReturn(newUser);
		ModelAndView model= accountManagementcontroller.accountManagementDisplayForm(request);
		String s=model.getViewName();
		//Assert Statements
		assertEquals("myAccount",s);
		
	}
	/**
	 * Description:This method is used to testing if user after changing his details will be reflected into his page
	
	 * @Input:mocked request and mocked user
	 * @Output: This method verifies model key 
	 * @author: Kocherla Shaline
	 * Written on:November 5,2018
	 * Modified on: November 6, 2018
	 */
	@Test
	public void testaccountManagementPost() {
		//Mocking session and request attribute
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userName")).thenReturn("Test");
		when(session.getAttribute("userRole")).thenReturn("CUSTOMER");
		when(session.getAttribute("userEmail")).thenReturn("test@gmail.com");
		ModelAndView model= accountManagementcontroller.accountManagementSubmitForm(newUser,request);
		String s=model.getViewName();
		//Assert Statements
		assertEquals("myAccount",s);
		assertTrue(model.getModel().containsKey("msg"));
		assertTrue(model.getModel().containsValue("Details updated successfully!"));
	
}/**
	 * Description:This method is used to testing if customer wants to change his password this page is displayed
	
	 * @Input:mocked request 
	 * @Output: This method verifies model view
	 * @author: Kocherla Shaline
	 * Written on:November 9,2018
	 * Modified on: November 10, 2018
	 */
	@Test
	public void testChangePasswordDisplayForm() {
		//Mocking session and request attribute
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userName")).thenReturn("Test");
		when(session.getAttribute("userRole")).thenReturn("CUSTOMER");
		when(session.getAttribute("userEmail")).thenReturn("test@gmail.com");
		//Mocking userservice findUserByEmail to return mocked user
		when(userservice.findUserByEmail("test@gmail.com")).thenReturn(newUser);
		ModelAndView model= accountManagementcontroller.changePasswordDisplayForm(request);
		String s=model.getViewName();
		//Assert Statements
		assertEquals("changePassword",s);
		
	
}
	/**
	 * Description:This method is used to testing if customer wants to change his password it is done succesfully
	
	 * @Input:mocked request and mocked user
	 * @Output: This method verifies model view and model key
	 * @author: Kocherla Shaline
	 * Written on:November 9,2018
	 * Modified on: November 10, 2018
	 */
	@Test
	public void testChangePasswordDisplayFormPost() {
		//Mocking session and request attribute
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userName")).thenReturn("Test");
		when(session.getAttribute("userRole")).thenReturn("CUSTOMER");
		when(session.getAttribute("userEmail")).thenReturn("test@gmail.com");
		ModelAndView model= accountManagementcontroller.changePasswordSubmitForm(newUser,request);
		String s=model.getViewName();
		//Assert Statements
		assertEquals("changePassword",s);
		assertTrue(model.getModel().containsKey("msg"));
		assertTrue(model.getModel().containsValue("Details updated successfully!"));
		
	
}
	
}


