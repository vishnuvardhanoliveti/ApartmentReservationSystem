//Description: this class tests various methods in SendRequestsController controller
package com.apartmentReservation.controller;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import org.mockito.InjectMocks;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import com.apartmentReservation.model.Backgroundverification;
import com.apartmentReservation.model.Properties;
import com.apartmentReservation.model.Requests;
import com.apartmentReservation.model.Role;
import com.apartmentReservation.model.RoommatePreferences;
import com.apartmentReservation.model.User;
import com.apartmentReservation.repository.UserRepository;
import com.apartmentReservation.service.BackgroundVerificationAndLeaseAdminService;
import com.apartmentReservation.service.BackgroundVerificationAndLeaseUserService;
import com.apartmentReservation.service.PropertyService;
import com.apartmentReservation.service.RoommatePreferencesService;
import com.apartmentReservation.service.SendRequestsService;
import com.apartmentReservation.service.UserService;
import com.itextpdf.text.DocumentException;


@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class SendrequestsControllerTest {

	@Mock
	private BindingResult bindingResult;
	Authentication auth = Mockito.mock(Authentication.class);

	SecurityContext securityContext = Mockito.mock(SecurityContext.class);
	 @InjectMocks
	  private SendRequestsController sendRequestsController; 
	@Mock
	 SendRequestsService sendRequestsService;
	 @Mock
	 private UserService userService;
	 @Mock
	 private RoommatePreferencesService roommatePreferencesService;
	 @Mock
	 private  BackgroundVerificationAndLeaseAdminService backgroundVerificationAndLeaseAdminService;
	 @Mock
	 private BackgroundVerificationAndLeaseUserService backgroundVerificationAndLeaseUserService;
	 
	 @Mock
		private PropertyService propertyService;
	 @Mock
	 private UserRepository userRepository;
	 
	 @Mock
		private HttpServletRequest request;
	 @Mock
		private HttpSession session;
	//Creating a mocked instance of user object.
	 User mockUser = User.builder().id(10).email("virat@gmail.com").firstName("Virat").lastName("Kohli").password("sample")
				.phoneNumber("123-456-7678").city("Denton").gender("male").dob("12-1-7890")
				.state("TX").zip("76201").build();
	 User mockUser1 = User.builder().id(10).email("virat@gmail.com").firstName("Virat").lastName("Kohli").password("sample")
				.phoneNumber("123-456-7678").city("Denton").gender("male").dob("1998-02-01")
				.state("TX").zip("76201").build();
	//Creating a mocked instance of BAckgroundverification object.
	 Backgroundverification backgroundverification = Backgroundverification.builder().email("virat@gmail.com")
			 .otherroommates("No").ownerSign("owner").firstname_rm1("shaline").lastname_rm1("kocherla").emailid_rm1("shaline@gmail.com").app_bgc_status("accept").firstname_rm2("tarun").propertyId(1).lastname_rm2("devan").emailid_rm2("devan@gmail.com").build();
	 Backgroundverification backgroundverification1 = Backgroundverification.builder().email("virat@gmail.com")
				.otherroommates("No").app_bgc_status("accept").ownerSign("owner").firstname_rm1(null).lastname_rm1("kocherla").emailid_rm1("shaline@gmail.com").firstname_rm2("tarun").propertyId(1).lastname_rm2("devan").emailid_rm2("devan@gmail.com").build();
	
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
	 Requests request1=Requests.builder().userEmail("web@gmail.com").matchedPercent("80").userDecision("sent").build();
	 List<Requests> Stringlist2 = new ArrayList<>();
	 public void compute2(String email,String percent,String user) {
			Requests request=Requests.builder().userEmail(email).matchedPercent(percent).userDecision(user).build();
			Stringlist2.add(request);
			}
	//Creating a mocked instance of properties object.
	 Properties property=Properties.builder()
				.propertyId(1)
				.propertyName("University Manor")
				.propertyLocation("Stella Strret")
				.aptNumber(1)
	            .build();
	 @Before
		public void setup() {
			MockitoAnnotations.initMocks(this);
			compute2("tarunya@gmail.com","69","user1");
			compute2("shikhara@gmail.com","90","user2");
	 }
	 /**
		 * Description:This method is used to test SendRequest method.		
		 * @Input:id, age, matchscore and mocked request
		 * @Output: This method verifies expected String
		 * @author: Kocherla Shaline
		 * Written on:November 23,2018
		 */
	 @Test
	 public void testforSendRequest()
	 {
		 String id="1";
		 String age="20";
		 String matchscore="70";
		//mocking the requests and session attributes
		 when(request.getSession()).thenReturn(session);
		 when(session.getAttribute("userName")).thenReturn("Virat Kohli");
		 when(session.getAttribute("userRole")).thenReturn("USER");
		 when(session.getAttribute("userEmail")).thenReturn("virat@gmail.com");
		 
		
		 when(userRepository.findById(1)).thenReturn(mockUser);		
		 when(userRepository.findByEmail("virat@gmail.com")).thenReturn(mockUser1);
		 SecurityContextHolder.setContext(securityContext);
		 when(securityContext.getAuthentication()).thenReturn(auth);
		 when(auth.getName()).thenReturn("virat@gmail.com");
		 String output=sendRequestsController.sendRequest(id, age, matchscore, "new",request);
		 //assert statements
		 assertEquals("redirect:/roommatePreferencesSearch",output);
 }
	 /**
		 * Description:This method is used to test ViewRequests method.		
		 * @Input:mocked request
		 * @Output: This method verifies expected view name
		 * @author: Kocherla Shaline
		 * Written on:November 23,2018
		 */
	 @Test
	 public void testforViewRequests()
	 {
		 
		//mocking the requests and session attributes
		 when(request.getSession()).thenReturn(session);
		 when(session.getAttribute("userName")).thenReturn("Virat Kohli");
		 when(session.getAttribute("userRole")).thenReturn("USER"); 
		 SecurityContextHolder.setContext(securityContext);
		 when(securityContext.getAuthentication()).thenReturn(auth);
		 when(auth.getName()).thenReturn("virat@gmail.com");
		 when(sendRequestsService.getReceivedRequests(auth.getName())).thenReturn(Stringlist2);
		 
		 ModelAndView output=sendRequestsController.viewRequests(request);
		 //assert statements
		 
		 assertTrue(output.getModel().containsKey("userName"));
		 assertTrue(output.getModel().containsValue("Virat Kohli"));
		 
		 assertEquals("user/view_requests",output.getViewName());
		 
	 }
	 /**
		 * Description:This method is used to test ViewMoreDetails method.		
		 * @Input:email and mocked request
		 * @Output: This method verifies expected view name
		 * @author: Kocherla Shaline
		 * Written on:November 23,2018
		 */
	 @Test
	 public void testforViewMoreDetails()
	 {
		 String email="virat@gmail.com";
		//mocking the requests and session attributes
		 when(request.getSession()).thenReturn(session);
		 when(session.getAttribute("userName")).thenReturn("Virat Kohli");
		 when(session.getAttribute("userRole")).thenReturn("USER"); 
		 SecurityContextHolder.setContext(securityContext);
		 when(securityContext.getAuthentication()).thenReturn(auth);
		 when(auth.getName()).thenReturn("virat@gmail.com");
		 when(userService.findUserByEmail(auth.getName())).thenReturn(mockUser);
		 when(roommatePreferencesService.findRoommatePreferencesById(mockUser.getId())).thenReturn(mockedroommatePreferences);
		 when(sendRequestsService.getReceivedRequests( auth.getName())).thenReturn(Stringlist2);
		 when(sendRequestsService.checkIfAlreadyRequestSent(email,auth.getName())).thenReturn(request1);
		 ModelAndView output=sendRequestsController.viewMoreDetals(email,request);
		//assert statements
		 assertTrue(output.getModel().containsKey("userName"));
		 assertTrue(output.getModel().containsValue("Virat Kohli"));
		 assertEquals("user/view_requests_more_details",output.getViewName());
		 
	 }
	 /**
		 * Description:This method is used to test SendRequests method.		
		 * @Input:email and mocked request
		 * @Output: This method verifies expected view name
		 * @author: Kocherla Shaline
		 * Written on:November 23,2018
		 */
	 @Test
	 public void testforSendRequests()
	 {
		 List<Requests> viewrequest = null;
		//mocking the requests and session attributes
		 when(request.getSession()).thenReturn(session);
		 when(session.getAttribute("userName")).thenReturn("Virat Kohli");
		 when(session.getAttribute("userRole")).thenReturn("USER"); 
		 SecurityContextHolder.setContext(securityContext);
		 when(securityContext.getAuthentication()).thenReturn(auth);
		 when(auth.getName()).thenReturn("virat@gmail.com");
		
		 when(sendRequestsService.getReceivedRequests( auth.getName())).thenReturn(viewrequest);
		 ModelAndView output=sendRequestsController.sentRequests(request);
		//assert statements
		 assertTrue(output.getModel().containsKey("userName"));
		 assertTrue(output.getModel().containsValue("Virat Kohli"));
		 assertEquals("user/sent_requests",output.getViewName());
		 
	 }
	 /**
		 * Description:This method is used to test UpdateRequestStatus method.		
		 * @Input:email status and mocked request
		 * @Output: This method verifies expected string
		 * @author: Kocherla Shaline
		 * Written on:November 23,2018
		 */
	 @Test
	 public void testforUpdateRequestStatus()
	 {
	     String email="virat@gmail.com";
	     String status="accept";
	   //mocking the requests and session attributes
	     when(request.getSession()).thenReturn(session);
		 SecurityContextHolder.setContext(securityContext);
		 when(securityContext.getAuthentication()).thenReturn(auth);
		 when(auth.getName()).thenReturn("virat@gmail.com"); 
		
		 String output=sendRequestsController.updateRequestStatus(email,status,request);
		 //assert statements
		 assertEquals("redirect:/viewMoreDetails/"+email,output);
		 
	 }
	 /**
		 * Description:This method is used to test Checkforlease method.		
		 * @Input:email 
		 * @Output: This method verifies expected string
		 * @author: Kocherla Shaline
		 * Written on:November 23,2018
		 */
	 @Test
	 public void testforCheckforlease()
	 {
	     String email="virat@gmail.com";
	     when(backgroundVerificationAndLeaseAdminService.findByEmail(email)).thenReturn(null);
		 String output=sendRequestsController.checkforlease(email);
		 //assert statements
		 assertEquals("redirect:/viewMoreDetails/"+email,output);
		 
	 }
	 /**
		 * Description:This method is used to test backgv method.		
		 * @Input:email and request
		 * @Output: This method verifies expected string
		 * @author: Kocherla Shaline
		 * Written on:November 23,2018
		 */
	 @Test
	 public void testforbackgv()
	 {
		 String email="virat@gmail.com";
		  //mocking the requests and session attributes
		 when(request.getSession()).thenReturn(session);
		 when(session.getAttribute("userName")).thenReturn("Virat Kohli");
		 when(session.getAttribute("userRole")).thenReturn("USER"); 
		 when(session.getAttribute("userEmail")).thenReturn("virat@gmail.com");
		 when(session.getAttribute("display_options")).thenReturn("yes");
		 when(session.getAttribute("display_status")).thenReturn("yes");
		 when(session.getAttribute("display_signthelease")).thenReturn("yes"); 
		 when(session.getAttribute("downloadpdf")).thenReturn("1");
		 when(userService.findUserByEmail("virat@gmail.com")).thenReturn(mockUser);
		 
		 
		
		 ModelAndView output=sendRequestsController.backgv(email,request);
		 //assert statements
		 assertTrue(output.getModel().containsKey("userName"));
		 assertTrue(output.getModel().containsValue("Virat Kohli"));
		 assertEquals("user/submit_background_verification_application_leased",output.getViewName());
		 
	 }
	 /**
		 * Description:This method is used to test Backgroundverification method.		
		 * @Input:backgroundverification1,bindingResult,email and request
		 * @Output: This method verifies expected string
		 * @author: Kocherla Shaline
		 * Written on:November 23,2018
		 */
	 @Test
	 public void testforBackgroundverification()
	 {
	     String email="virat@gmail.com";
	     //mocking the requests and session attributes
	     when(request.getSession()).thenReturn(session);
	     when(session.getAttribute("userEmail")).thenReturn("virat@gmail.com");
	     when(userService.findUserByEmail("virat@gmail.com")).thenReturn(mockUser);
	     when(backgroundVerificationAndLeaseAdminService.findByEmail(email)).thenReturn(backgroundverification);
	    when(propertyService.findProperty(1)).thenReturn(property);
	     when(backgroundVerificationAndLeaseUserService.saveUser(backgroundverification)).thenReturn(backgroundverification);
		 String output=sendRequestsController.backgroundverification(backgroundverification1,bindingResult,email,request);
		 //assert statements
		 assertEquals("redirect:/home",output);
		 
	 }
	 /**
		 * Description:This method is used to test Backgroundverificationformupdate method.		
		 * @Input:request
		 * @Output: This method verifies expected string
		 * @author: Kocherla Shaline
		 * Written on:November 23,2018
		 */
	 @Test
	 public void testforBackgroundverificationformupdate()
	 {
	     String email="virat@gmail.com";
	     //mocking the requests and session attributes
	     when(request.getSession()).thenReturn(session);
	     when(session.getAttribute("userEmail")).thenReturn("virat@gmail.com");
	     when(userService.findUserByEmail("virat@gmail.com")).thenReturn(mockUser);
	     when(backgroundVerificationAndLeaseAdminService.findByEmail(email)).thenReturn(backgroundverification);
	     when(backgroundVerificationAndLeaseAdminService.findByEmail(backgroundverification.getEmailid_rm1())).thenReturn(backgroundverification1);
	    when(backgroundVerificationAndLeaseAdminService.findByEmail(backgroundverification.getEmailid_rm2())).thenReturn(backgroundverification);
		 String output=sendRequestsController.backgroundverificationformupdate(request);
		 //assert statements
		 assertEquals("redirect:/home",output);
		 
	 }
	 /**
		 * Description:This method is used to test RoomateRequestHome method.		
		 * @Input:request
		 * @Output: This method verifies expected view name
		 * @author: Kocherla Shaline
		 * Written on:November 23,2018
		 */
	 @Test
	 public void testRoomateRequestHome()
	 {
		//mocking the requests and session attributes
		 when(request.getSession()).thenReturn(session);
		 when(session.getAttribute("userEmail")).thenReturn("virat@gmail.com");
		 when(session.getAttribute("userName")).thenReturn("Virat Kohli");
		 when(session.getAttribute("userRole")).thenReturn("USER"); 
		ModelAndView output= sendRequestsController.roommateRequestHome(request);
		//assertS Stattements
		assertTrue(output.getModel().containsKey("userName"));
		 assertTrue(output.getModel().containsValue("Virat Kohli"));
		 assertEquals("user/roommate_requests",output.getViewName());
		
		 
	 }
	 
	 
}
