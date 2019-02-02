//Description: This class is used to test various methods under background verification Admin controller.
package com.apartmentReservation.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import com.apartmentReservation.model.Backgroundverification;
import com.apartmentReservation.repository.BackgroundVerificationAndLeaseAdminRepository;
import com.apartmentReservation.service.BackgroundVerificationAndLeaseAdminService;
import com.apartmentReservation.service.BackgroundVerificationAndLeaseUserService;
import com.apartmentReservation.service.PropertyService;
import com.apartmentReservation.service.UserService;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class BackgroundVerificationAdminTest {
	@Mock
	private UserService userservice;
	@Mock
	private BindingResult bindingResult;
	@InjectMocks
	private BackgroundVerificationAndLeaseAdmin backgroundVerificationAndLeaseAdmin;
	@Mock
	private Backgroundverification backgroundverification;
	@Mock
	private BackgroundVerificationAndLeaseUserService backgroundVerificationAndLeaseUserService;
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpSession session;
	@Mock
	private PropertyService propertyService;
	@Mock
	private BackgroundVerificationAndLeaseAdminRepository backgroundVerificationAndLeaseAdminRepository;
	@Mock
	private BackgroundVerificationAndLeaseAdminService backgroundVerificationAndLeaseAdminService;
	List<Backgroundverification> stringlist = new ArrayList<>();
	Authentication auth = Mockito.mock(Authentication.class);
	SecurityContext securityContext = Mockito.mock(SecurityContext.class);

	@Before

	public void setup() {
		MockitoAnnotations.initMocks(this);
		compute("a@gmail.com", "a", "b", "p", "234", "west", "tex", "us", "3000");
		compute("b@gmail.com", "b", "c", "pa", "2345", "westh", "texe", "usq", "30001");
		compute("c@gmail.com", "c", "d", "pas", "2346", "westi", "texr", "usw", "30002");
		compute("d@gmail.com", "d", "e", "pass", "2347", "westc", "text", "use", "30003");
		compute("e@gmail.com", "e", "f", "passw", "2348", "westo", "texy", "usr", "30004");
		compute("f@gmail.com", "f", "g", "passwo", "2349", "westr", "texu", "ust", "30005");
	}

	public void compute(String e, String f, String l, String p, String ph, String ad1, String c, String s, String z) {
		Backgroundverification backgroundverification = Backgroundverification.builder().email(e).firstname(f)
				.lastname(l).app_bgc_status("accept").ownerSign(null).propertyId(22).phonenumber(ph).address(ad1)
				.build();

		stringlist.add(backgroundverification);
	}

	/*
	 * Description: test if all applications are displayed to the Admin 
	 * input:Httpsession 
	 * output: intended page to be viewed by admin
	 */
	@Test
	public void testForFindAllApplications() {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userName")).thenReturn("Tarun");
		when(session.getAttribute("userRole")).thenReturn("ADMIN");
		when(backgroundVerificationAndLeaseAdminService.findAllApplications()).thenReturn(stringlist);
		ModelAndView model = new ModelAndView();
		model = backgroundVerificationAndLeaseAdmin.findAllapplications(request);
		assertEquals("admin/process_background_applications", model.getViewName());

	}

	/*
	 * Description: test if admin can update status 
	 * input: email id , status,
	 * HttpSession output: checking model attributes updatestatus, otherroommates,numberroommates
	 */
	@Test
	public void testForstatusupdate() {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userName")).thenReturn("Tarun");
		when(session.getAttribute("userRole")).thenReturn("ADMIN");
		Backgroundverification backgroundverification = Backgroundverification.builder().email("tarundevan@gmail.com")
				.firstname("tarun").lastname("devan").emailid_rm1("anoop@gmail.com").emailid_rm2("vishnu@gmail.com")
				.otherroommates("1").build();
		Backgroundverification backgroundverification1 = Backgroundverification.builder().email("anoop@gmail.com")
				.firstname("anoop").lastname("vengula").otherroommates("1").build();
		Backgroundverification backgroundverification2 = Backgroundverification.builder().email("vishnu@gmail.com")
				.firstname("vishnu").lastname("oliveti").otherroommates("2").build();
		when(backgroundVerificationAndLeaseAdminService.findByEmail("tarundevan@gmail.com"))
				.thenReturn(backgroundverification);
		when(backgroundVerificationAndLeaseAdminService.findByEmail("anoop@gmail.com"))
				.thenReturn(backgroundverification1);
		when(backgroundVerificationAndLeaseAdminService.findByEmail("vishnu@gmail.com"))
				.thenReturn(backgroundverification2);
		ModelAndView model = new ModelAndView();
		model = backgroundVerificationAndLeaseAdmin.statusupdate("tarundevan@gmail.com", "accepted", request);
		assertEquals("yes", model.getModel().get("updatestatus"));
		ModelAndView model1 = new ModelAndView();
		model1 = backgroundVerificationAndLeaseAdmin.statusupdate("tarundevan@gmail.com", "rejected", request);
		assertEquals("yes", model1.getModel().get("otherroommates"));

		ModelAndView model2 = new ModelAndView();
		model2 = backgroundVerificationAndLeaseAdmin.statusupdate("tarundevan@gmail.com", "rejected", request);
		assertEquals("2", model2.getModel().get("numberroommates"));

	}

}
