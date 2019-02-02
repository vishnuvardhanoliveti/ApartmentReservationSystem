//Description: This class is used to test various methods under UserController.
package com.apartmentReservation.controller;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import com.apartmentReservation.model.User;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
	@Mock
	private BindingResult bindingResult;
	@Mock
	private HttpServletRequest request;
	Authentication auth = Mockito.mock(Authentication.class);
	SecurityContext securityContext = Mockito.mock(SecurityContext.class);
	@Autowired
	private UserController userController;

	@Before
	public void setupTest() {
		MockitoAnnotations.initMocks(this);
	}

	User newUser = User.builder().email("virat@gmail.com").firstName("Virat").lastName("Kohli").password("sample")
			.phoneNumber("123-456-7678").addressLine1("University Manor").addressLine2("Stella Street").city("Denton")
			.state("TX").zip("76201").build();
	User newUser2 = User.builder().email("vishnu@gmail.com").firstName("Virat").lastName("Kohli").password("sample")
			.phoneNumber("123-456-7678").addressLine1("University Manor").addressLine2("Stella Street").city("Denton")
			.state("TX").zip("76201").id(30).build();

	/*
	 * Description: This method tests whether a customer is registered successfully if the form has no errors. 
	 *  input: user and mocked binding result 
	 *  output:checking model containing key msg
	 */
	@Test
	public void testRegisterUser() {
		when(bindingResult.hasErrors()).thenReturn(false);
		ModelAndView model = userController.createUser(newUser, bindingResult);
		String viewName = model.getViewName();
		assertEquals("user/signup", model.getViewName());
		assertTrue(model.getModel().containsKey("msg"));
		assertTrue(model.getModel().containsValue("User has been registered successfully!"));
	}

	/*
	 * Description: This method tests that if there are any binding errors in the form then the customer is not registered. 
	 * input: user and user input and mocked binding result 
	 * output:checking model containing key error2
	 */
	@Test
	public void testRegisterUserWithBindingErrors() {
		when(bindingResult.hasErrors()).thenReturn(true);
		ModelAndView model = userController.createUser(newUser, bindingResult);
		assertTrue(model.getModel().containsKey("error2"));
		assertTrue(model.getModel().containsValue("Binding errors"));
	}

	/*
	 * Description: This method tests that if a customer uses already existing email
	 * to register then his registration fails. input: user and mocked binding
	 * results output: checking model key error
	 */
	@Test
	public void testRegisterUserWithAlreadyExistingEmail() {
		ModelAndView model = userController.createUser(newUser, bindingResult);
		assertTrue(model.getModel().containsKey("error"));
		assertTrue(model.getModel().containsValue("This email already exists!"));
	}

	/*
	 * Description: Testing that the customer is redirected to correct home page i.e
	 * user/home and checking that asserts displays all properties. input:
	 * HttpSession output: checking model view user/home and model key prop
	 */
	@Test
	public void testRedirectUserHomePage() {
		when(securityContext.getAuthentication()).thenReturn(auth);
		SecurityContextHolder.setContext(securityContext);
		when(auth.getName()).thenReturn("vishnu@gmail.com");
		ModelAndView model = userController.defaultAfterLogin(request);
		assertEquals("user/home", model.getViewName());
		assertTrue(model.getModel().containsKey("prop"));
	}

	/*
	 * Description: Testing that the admin is redirected to correct home page i.e
	 * admin/home and checking that asserts displays his userRole. input:
	 * HttpSession output:checking model viewname admin/home and model value ADMIN
	 */
	@Test
	public void testRedirectAdminHomePage() {
		when(securityContext.getAuthentication()).thenReturn(auth);
		SecurityContextHolder.setContext(securityContext);
		when(auth.getName()).thenReturn("tarun@gmail.com");
		ModelAndView model = userController.defaultAfterLogin(request);
		assertEquals("admin/home", model.getViewName());
		assertTrue(model.getModel().containsValue("ADMIN"));
	}

	/*
	 * Description: Testing that the owner is redirected to correct home page i.e
	 * admin/home and checking that asserts displays his userRole. input:
	 * HttpSession output: checking model viewname owner/home and model value OWNER
	 */
	@Test
	public void testRedirectOwnerHomePage() {
		when(securityContext.getAuthentication()).thenReturn(auth);
		SecurityContextHolder.setContext(securityContext);
		when(auth.getName()).thenReturn("kumar@gmail.com");
		ModelAndView model = userController.defaultAfterLogin(request);
		assertEquals("owner/home", model.getViewName());
		assertTrue(model.getModel().containsValue("OWNER"));
	}

	/*
	 * Description: Testing Index page request of the application input: Httpsession
	 * output: intended page to be displayed
	 */
	@Test
	public void testIndexPageRequest() {
		String view = userController.login(request);
		assertEquals("redirect:/home", view);
	}

	/*
	 * Description: Testing login request page with asserts input: -when customer
	 * tries to log in output: checking model view name user/login
	 */
	@Test
	public void testLoginRequest() {
		ModelAndView model = userController.login();
		assertEquals("user/login", model.getViewName());
	}

	/*
	 * Description: Testing Signup page with asserts input: -when customer tries to
	 * register output: checking model view name
	 */
	@Test
	public void testRegisterFormRequest() {
		ModelAndView model = userController.signup();
		assertEquals("user/signup", model.getViewName());
	}

	/*
	 * Description: Testing access denied page with asserts. input: -when customer
	 * tries to access unauthorized web pages output: checking model view name
	 * errors/access_denied
	 */
	@Test
	public void testForAccessDenied() {
		ModelAndView model = userController.accessDenied();
		assertEquals("errors/access_denied", model.getViewName());
	}

}
