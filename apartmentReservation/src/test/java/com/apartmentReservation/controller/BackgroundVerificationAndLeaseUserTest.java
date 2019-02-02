//Description: This class is used to test various methods under background verification user controller.
package com.apartmentReservation.controller;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import com.apartmentReservation.model.Backgroundverification;
import com.apartmentReservation.model.Properties;
import com.apartmentReservation.model.Role;
import com.apartmentReservation.model.User;
import com.apartmentReservation.service.BackgroundVerificationAndLeaseAdminService;
import com.apartmentReservation.service.BackgroundVerificationAndLeaseUserService;
import com.apartmentReservation.service.PropertyService;
import com.apartmentReservation.service.UserService;
import com.itextpdf.text.DocumentException;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BackgroundVerificationAndLeaseUserTest {
	@Mock
	private UserService userservice;
	@Mock
	private BindingResult bindingResult;
	@Mock
	private User user;
	@Autowired
	private BackgroundVerificationAndLeaseUser BackgroundVerificationUserController;
	@Mock
	private BackgroundVerificationAndLeaseUserService backgroundVerificationAndLeaseUserService;
	@Mock
	private MockHttpServletRequest request;
	@Mock
	private HttpSession session;
	@Mock
	private PropertyService propertyService;
	@Mock
	private BackgroundVerificationAndLeaseAdminService backgroundVerificationAndLeaseAdminService;
	Authentication auth = Mockito.mock(Authentication.class);
	SecurityContext securityContext = Mockito.mock(SecurityContext.class);

	@Before
	public void setupTest() {
		MockitoAnnotations.initMocks(this);
	}

	User newUser = User.builder().email("shaline@gmail.com").firstName("shaline").lastName("kocherla")
			.password("shaline").phoneNumber("646-240-7834").addressLine1("2001 west hickory street")
			.addressLine2("park place apartments").city("texas").state("Texas").zip("76201").build();
	Backgroundverification backgroundverification = Backgroundverification.builder().email("tarundevan@gmail.com")
			.ownerSign("owner").propertyId(22).build();
	Backgroundverification backgroundverification2 = Backgroundverification.builder().email("shaline@gmail.com")
			.firstname(null).phonenumber(null).address(null).lastname(null).ownerSign("owner").firstname_rm1("tarun")
			.lastname_rm1("devan").propertyOwner(0).emailid_rm1("tarundevan@gmail.com").propertyId(22).build();
	Properties properties = Properties.builder().propertyId(22).propertyRent(1425)
			.propertyAddress("Trust Drive, Denton").propertyDeposit(400).propertyOwner(1).build();
	Properties properties2 = Properties.builder().propertyId(14).propertyOwner(1).build();

	/*
	 * Description: This method tests whether a customer is able to see the signlease page. 
	 * Input: HttpSession 
	 * Output: Intended pages viewed by customer
	 */
	@Test
	public void testSignTheLease() {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userName")).thenReturn("Tarun Devan");
		when(session.getAttribute("userRole")).thenReturn("CUSTOMER");

		ModelAndView model = BackgroundVerificationUserController.signthelease(request);
		String viewName = model.getViewName();

		assertEquals("user/sign_the_lease", model.getViewName());
	}

	/*
	 * Description: This method tests that if a customer signed the lease and the
	 * pdf is generated successfully
	 * Input: The sign of customer and the HttpSession
	 * Output: The intended page viewed by customer
	 */
	@Test
	public void testCustomersignedthelease() throws Exception, Exception {
		String sign = "Virat";
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userEmail")).thenReturn("tarundevan@gmail.com");
		// when(userservice.findUserByEmail(("tarundevan@gmail.com)").thenReturn()
		when(backgroundVerificationAndLeaseUserService.findUserByEmail("tarundevan@gmail.com"))
				.thenReturn(backgroundverification);
		when(backgroundVerificationAndLeaseAdminService.findByEmail("tarundevan@gmail.com"))
				.thenReturn(backgroundverification);

		when(propertyService.findProperty(backgroundverification.getPropertyId())).thenReturn(properties);
		String view = BackgroundVerificationUserController.signthelease(sign, request);
		assertEquals("redirect:/home", view);
	}

	/*
	 * Description: This method tests to display the backgroundverification Form
	 * input: property id and HttpSession
	 *  output: intended page viewed by customer
	 */
	@Test
	public void testDisplaybackgroundverificationForm() throws Exception, Exception {
		int pid = 14;
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userEmail")).thenReturn("virat@gmail.com");
		when(session.getAttribute("userName")).thenReturn("Virat Kohli");
		when(session.getAttribute("userRole")).thenReturn("CUSTOMER");
		ModelAndView model = BackgroundVerificationUserController.backgv(pid, request);
		assertTrue(model.getModel().containsKey("backgv"));
		assertEquals("user/submit_background_verification_application", model.getViewName());
	}

	/*
	 * Description: Test that user details are getting stored after submitting the background verification form 
	 * input: backgroundverification details of customer, mocked binding results, property Id and the HttpSession 
	 * output: the intended page that is shown to the customer
	 */
	@Test
	public void testforBackgroundverificationsubmitPostmethod() throws Exception, Exception {
		int pid = 14;
		when(request.getSession()).thenReturn(session);
		when(bindingResult.hasErrors()).thenReturn(false);
		when(session.getAttribute("userEmail")).thenReturn("virat@gmail.com");
		when(propertyService.findProperty(pid)).thenReturn(properties);
		when(backgroundVerificationAndLeaseUserService.findUserByEmail("virat@gmail.com"))
				.thenReturn(backgroundverification2);
		when(backgroundVerificationAndLeaseUserService.saveUser(backgroundverification2))
				.thenReturn(backgroundverification2);
		String view2 = BackgroundVerificationUserController.backgroundverification(backgroundverification2,
				bindingResult, pid, request);
		assertEquals("redirect:/home", view2);
	}
}
