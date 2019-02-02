/*Description: Testing various methods in issueOwner controller
 * @author: Kocherla Shaline
 * Written on:November 5,2018
 * Modified on: November 6, 2018
 */
package com.apartmentReservation.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.servlet.ModelAndView;

import com.apartmentReservation.model.Issue;
import com.apartmentReservation.model.Role;
import com.apartmentReservation.model.User;
import com.apartmentReservation.service.IssueAdminService;
import com.apartmentReservation.service.UserService;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class IssueOwnerControllerTest {
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpSession session;
	@Mock
	private UserService userService;
	@InjectMocks
	private IssueOwnerController issueOwnerController;
	
	@Mock
	private IssueAdminService issueAdminService;
	List<Issue> StringList = new ArrayList<>();
	//Creating a mocked instance of issue object.
	Issue issue = Issue.builder()
			.issueId(10)
			.issueSubmittedDate("12-12-18")
			.issueStatus("solved")
			.build();
	//Creating a mocked instance of role object.
	Role role =Role.builder().role("Admin").id(10).build();
   Set<Role> roles;
 //Creating a mocked instance of User object.
   User mockUser = User.builder().id(20).email("virat@gmail.com").firstName("Virat").lastName("Kohli").password("sample")
			.phoneNumber("123-456-7678").addressLine1("University Manor").addressLine2("Stella Street").city("Denton")
			.state("TX").zip("76201").roles(roles).build();
 //Creating a mocked instance of Issue object.
   Issue issues = Issue.builder()
			.issueId(1)
			.issueUserEmail("b@gmail.com")
			.issueSubmittedDate("12-09-10")
			.issueStatus("Under Review")
			.propertyOwner(20)
			.build();

	
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
	//this method is run before any test
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
	 * Description:This method is used to test if owner can find all the issues submitted to his properties in his portal.
	 * @Input:mocked request
	 * @Output: This method verifies the view name of the model
	 * @author: Kocherla Shaline
	 * Written on:November 5,2018
	 * Modified on: November 6, 2018
	 */
@Test
public void testFindAllApplications()
{   //mocking the requests and session attributes
	when(request.getSession()).thenReturn(session);
	when(session.getAttribute("userName")).thenReturn("Virat Kohli");
	when(session.getAttribute("userRole")).thenReturn("ADMIN");
	when(session.getAttribute("userEmail")).thenReturn("virat@gmail.com");
	//mocking userService findUserByEmail to return mock user
	when(userService.findUserByEmail("virat@gmail.com")).thenReturn(mockUser);
	//mocking issueAdminService findByPropertyOwnerId to return list of mocked issues
	when(issueAdminService.findByPropertyOwnerId(20)).thenReturn(StringList);
	ModelAndView model = new ModelAndView();
	model = issueOwnerController.findAllapplications(request);
	//Assert Statements
	assertEquals("owner/view_all_issues", model.getViewName());
}
/**
 * Description:This method is used to test if owner can see the issue in detail
 * @Input:mocked request and mocked issue id
 * @Output: This method verifies the view name of the model
 * @author: Kocherla Shaline
 * Written on:November 5,2018
 * Modified on: November 6, 2018
 */

@Test
public void testIssueStatusIndetailview()
{
	//mocking the requests and session attributes
	when(request.getSession()).thenReturn(session);
	when(session.getAttribute("userName")).thenReturn("Virat Kohli");
	when(session.getAttribute("userRole")).thenReturn("ADMIN");
	
	//mocking issueAdminService findById to return mocked issue 
	when(issueAdminService.findById(1)).thenReturn(issues);
	ModelAndView model = issueOwnerController.issuestatusupdate(1,request);
	
	//Assert Statements
	assertEquals("owner/issue_more_details", model.getViewName());
}

}
