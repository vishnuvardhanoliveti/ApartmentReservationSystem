//Description: Test various methods in issue user controller 
package com.apartmentReservation.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import com.apartmentReservation.model.Backgroundverification;
import com.apartmentReservation.model.Issue;
import com.apartmentReservation.model.Properties;
import com.apartmentReservation.model.Role;
import com.apartmentReservation.model.User;
import com.apartmentReservation.repository.IssueRepository;
import com.apartmentReservation.repository.UserRepository;
import com.apartmentReservation.service.BackgroundVerificationAndLeaseAdminService;
import com.apartmentReservation.service.BackgroundVerificationAndLeaseUserService;
import com.apartmentReservation.service.IssueUserService;
import com.apartmentReservation.service.PropertyService;
import com.apartmentReservation.service.UserService;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class IssueUserControllerTest {
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpSession session;
	@Mock
	private UserService userService;
	@Mock
	private BindingResult bindingResult;
	@InjectMocks
	private IssueController issueController;
	@Mock
	private IssueRepository issueRepository;
	@Mock
	private IssueUserService issueUserService;
	@Mock
	private BackgroundVerificationAndLeaseUserService backgroundVerificationAndLeaseUserService;
	@Mock
	private BackgroundVerificationAndLeaseAdminService backgroundVerificationAndLeaseAdminService;
	@Mock
	private PropertyService propertyService;
	//Creating a mocked instance of Issue object.
	Issue issue = Issue.builder()
			.issueId(10)
			.issueSubmittedDate("12-12-18")
			.issueStatus("Under Review")
			.propertyOwner(20)
			.build();
	//Creating a mocked instance of role object.
	Role role =Role.builder().role("User").id(10).build();
   Set<Role> roles;
 //Creating a mocked instance of user object.
   User mockUser = User.builder().id(10).email("virat@gmail.com").firstName("Virat").lastName("Kohli").password("sample")
			.phoneNumber("123-456-7678").city("Denton")
			.state("TX").zip("76201").roles(roles).build();
 //Creating a mocked instance of issue object.
   Issue issues = Issue.builder()
			.issueId(1)
			.issueUserEmail("b@gmail.com")
			.issueUserFirstName("Virat")
			.issueUserLastName("Kohli")
			.issueSubmittedDate("12-09-10")
			.issueStatus("Under Review")
			.propertyOwner(10)
			.build();
 //Creating a mocked instance of Backgroundverification object.
   Backgroundverification backgroundverification = Backgroundverification.builder().email("virat@gmail.com")
			.ownerSign("owner").propertyOwner(20).propertyId(22).build();
	List<Issue> StringList = new ArrayList<>();
	public void compute(String e,String email, String status, int c, int p) {
		Issue issue1 = Issue.builder()
				.issueId(c)
				.issueUserEmail(email)
				.issueSubmittedDate(e)
				.issueStatus("Under Review")
				.propertyOwner(p)
				.build();

		StringList.add(issue1);
	}
	//Creating a mocked instance of Properties object.
	Properties properties = Properties.builder().aptNumber(118).propertyId(22).propertyAddress("west hickry street").propertyName("park place apt").build();
	//this method runs before every test
	@Before

public void setup() {
	MockitoAnnotations.initMocks(this);
	compute("12-10-19","a@gmail.com", null, 22,20);
	compute("10-09-20","a@gmail.com", null, 22,20);
	compute("11-08-20","a@gmail.com", null, 22,20);
	compute("03-05-17","a@gmail.com", null, 22,20);
	compute("09-03-19","a@gmail.com", null, 22,20);
	compute("01-02-17","a@gmail.com", null, 22,20);
		
}
	/**
	 * Description:This method is used to test if customer can view home page of issue service portal.
	
	 * @Input:mocked request
	 * @Output: This method viewName of model
	 * @author:Kocherla Shaline
	 * Written on:November 5,2018
	 * Modified on: November 6, 2018
	 */
@Test
public void testIssueHome()
{
	//Mocking Request and session
	when(request.getSession()).thenReturn(session);
	when(session.getAttribute("userEmail")).thenReturn("virat@gmail.com");
	when(session.getAttribute("userName")).thenReturn("Virat Kohli");
	when(session.getAttribute("userRole")).thenReturn("USER");
	when(session.getAttribute("display_options")).thenReturn("Yes");
	when(session.getAttribute("display_status")).thenReturn("Yes");
	when(session.getAttribute("display_signthelease")).thenReturn("Yes");
	when(session.getAttribute("downloadpdf")).thenReturn("Yes");
	//mocking backgroundVerificationAndLeaseAdminService findByEmail to return mocked backgroundverification object
	when(backgroundVerificationAndLeaseAdminService.findByEmail("virat@gmail.com")).thenReturn(backgroundverification);
	ModelAndView model = new ModelAndView();
	 model= issueController.issuehome(request);
	 //Assert Statements
	 assertEquals("user/service_issue_portal_home", model.getViewName());
}
/**
 * Description:This method is used to test if customer can open issue portal.

 * @Input:mocked request
 * @Output: This method viewName of model
 * @author:Kocherla Shaline
 * Written on:November 5,2018
 * Modified on: November 6, 2018
 */
@Test 
public void testOpenIssue()
{
	//Mocking Request and session attributes
	when(request.getSession()).thenReturn(session);
	when(session.getAttribute("userEmail")).thenReturn("virat@gmail.com");
	when(session.getAttribute("userName")).thenReturn("Virat Kohli");
	when(session.getAttribute("userRole")).thenReturn("USER");
	when(session.getAttribute("display_options")).thenReturn("Yes");
	when(session.getAttribute("display_status")).thenReturn("Yes");
	when(session.getAttribute("display_signthelease")).thenReturn("Yes");
	when(session.getAttribute("downloadpdf")).thenReturn("Yes");
	//Mocking issueUserService findByIssueUserEmail to return mocked list of issue list
	when(issueUserService.findByIssueUserEmail("virat@gmail.com")).thenReturn(StringList);
	ModelAndView model = new ModelAndView();
	 model= issueController.openissue(request);
	 //Assert Statements
	 assertEquals("user/service_issue_portal_open_requests", model.getViewName());

}
/**
 * Description:This method is used to test if customer can submit issues in portal.
 * @Input:mocked request
 * @Output: This method viewName of model
 * @author:Kocherla Shaline
 * Written on:November 5,2018
 * Modified on: November 6, 2018
 */
@Test 
public void testSubmitIssue()
{
	//Mocking request and session attributes
	when(request.getSession()).thenReturn(session);
	when(session.getAttribute("userEmail")).thenReturn("virat@gmail.com");
	when(session.getAttribute("userName")).thenReturn("Virat Kohli");
	when(session.getAttribute("userRole")).thenReturn("USER");
	when(session.getAttribute("display_options")).thenReturn("Yes");
	when(session.getAttribute("display_status")).thenReturn("Yes");
	when(session.getAttribute("display_signthelease")).thenReturn("Yes");
	when(session.getAttribute("downloadpdf")).thenReturn("Yes");
	ModelAndView model = new ModelAndView();
	 model= issueController.submitissue(request);
	//Assert statements
	 assertEquals("user/service_issue_portal_create_request", model.getViewName());

}
/**
 * Description:This method is used to test if customers issue is saved.
 * @Input:mocked request, mocked issue, mocked binding result
 * @Output: This method test the return string
 * @author:Kocherla Shaline
 * Written on:November 5,2018
 * Modified on: November 6, 2018
 */
@Test
public void testSubmittedIssue()
{
	//Mocking request and session attributes
	when(request.getSession()).thenReturn(session);
	when(session.getAttribute("userEmail")).thenReturn("virat@gmail.com");
//Mocking backgroundVerificationAndLeaseUserService findUserByEmail to return backgroundverification
 when(backgroundVerificationAndLeaseUserService.findUserByEmail("virat@gmail.com")).thenReturn(backgroundverification);
//mocking userService findUserByEmail to return mocked user
 when(userService.findUserByEmail("virat@gmail.com")).thenReturn(mockUser);
//Mocking propertyService findProperty to return mocked properties
 when(propertyService.findProperty(22)).thenReturn(properties);
	 String output = issueController.submitissued(issue,bindingResult,request);
	 //Assert Statements
	 assertEquals("redirect:/serviceIssuePortalOpenRequests/", output);
}

}