//Description: Testing various methods in IssueAdmin Controller 
package com.apartmentReservation.controller;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
import com.apartmentReservation.repository.UserRepository;
import com.apartmentReservation.service.IssueAdminService;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class IssueAdminControllerTest {
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpSession session;
	@Mock
	private UserRepository userRepository;
	@InjectMocks
	private IssueAdminController issueAdminController;
	
	@Mock
	private IssueAdminService issueAdminService;
	List<Issue> StringList = new ArrayList<>();
	
	//Creating a mocked instance of issue object.
	Issue issue = Issue.builder()
			.issueId(10)
			.issueSubmittedDate("12-12-18")
			.issueStatus(null)
			.build();
   Role role =Role.builder().role("Admin").id(10).build();
   Set<Role> roles;
	User mockUser = User.builder().id(10).email("virat@gmail.com").firstName("Virat").lastName("Kohli").password("sample")
			.phoneNumber("123-456-7678").addressLine1("University Manor").addressLine2("Stella Street").city("Denton")
			.state("TX").zip("76201").roles(roles).build();
	Issue issues = Issue.builder()
			.issueId(1)
			.issueUserEmail("b@gmail.com")
			.issueSubmittedDate("12-09-10")
			.issueStatus("Under Review")
			.propertyOwner(10)
			.build();
	
	Issue newissues = Issue.builder()
			.issueId(1)
			.issueUserEmail("b@gmail.com")
			.issueSubmittedDate("12-09-10")
			.issueStatus("InProgress")
			.propertyOwner(10)
			.build();
	Issue newissues1 = Issue.builder()
			.issueId(1)
			.issueUserEmail("b@gmail.com")
			.issueSubmittedDate("12-09-10")
			.issueStatus("Solved")
			.propertyOwner(10)
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
	//this method is run before every test
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
	 * Description: This method is used to test if Admin can find all applications .
	 * @Input:mocked request
	 * @Output: This method verifies msg and  expected viewName
	 * @author: Kocherla Shaline
	 * Written on:November 5,2018
	 * Modified on: November 6, 2018
	 */
	@Test
public void testFindAllApplications()
{
	//Mocking request and session attributes
	when(request.getSession()).thenReturn(session);
	when(session.getAttribute("userName")).thenReturn("Virat Kohli");
	when(session.getAttribute("userRole")).thenReturn("ADMIN");
	//mocking issueAdminService findAllApplications to return list of mocked issues
	when(issueAdminService.findAllApplications()).thenReturn(StringList);
	ModelAndView model = new ModelAndView();
	model = issueAdminController.findAllapplications(request);
	//Assert Statements
	assertEquals("admin/view_all_issues", model.getViewName());
}
	/**
	 * Description: This method is used to test if Admin can update status  to under review.
	 * @Input:mocked request and issue id
	 * @Output: This method verifies display_Issuestatus and  expected value
	 * @author: Kocherla Shaline
	 * Written on:November 5,2018
	 * Modified on: November 6, 2018
	 */
@Test
public void testUpdateStatusForUnderReview()
{
	//mocking request and session attributes
	when(request.getSession()).thenReturn(session);
	when(session.getAttribute("userName")).thenReturn("Virat Kohli");
	when(session.getAttribute("userRole")).thenReturn("ADMIN");
	//mocking issueAdminService findById to return mocked issue
	when(issueAdminService.findById(1)).thenReturn(issues);
	//mocking userRepository findById to return mocked user
	when(userRepository.findById(10)).thenReturn(mockUser);
	ModelAndView model = new ModelAndView();
	model = issueAdminController.issuestatusupdate(1,request);
	//Assert Statements
	assertTrue(model.getModel().containsKey("display_Issuestatus"));
	assertTrue(model.getModel().containsValue("Under Review"));
	
}
/**
 * Description: This method is used to test if Admin can update status to Inprogress .
 * @Input:mocked request and issue id
 * @Output: This method verifies display_Issuestatus and  expected value
 * @author: Kocherla Shaline
 * Written on:November 5,2018
 * Modified on: November 6, 2018
 */
@Test
public void testUpdateStatusForInprogress()
{
	//Mocking request and session attributes
	when(request.getSession()).thenReturn(session);
	when(session.getAttribute("userName")).thenReturn("Virat Kohli");
	when(session.getAttribute("userRole")).thenReturn("ADMIN");
	//Mocking issueAdminService findById to return mocked issue
	when(issueAdminService.findById(1)).thenReturn(newissues);
	//Mocking userRepository findById to return mocked issue
	when(userRepository.findById(10)).thenReturn(mockUser);
	ModelAndView model = new ModelAndView();
	model = issueAdminController.issuestatusupdate(1,request);
	//Assert Statements
	assertTrue(model.getModel().containsKey("display_Issuestatus"));
	assertTrue(model.getModel().containsValue("InProgress"));
	
}
/**
 * Description: This method is used to test if Admin can update status to Solved .
 * @Input:mocked request and issue id
 * @Output: This method verifies display_Issuestatus and  expected value
 * @author: Kocherla Shaline
 * Written on:November 5,2018
 * Modified on: November 6, 2018
 */
@Test
public void testUpdateStatusForSolved()
{
	//Mocked request and session attributes
	when(request.getSession()).thenReturn(session);
	when(session.getAttribute("userName")).thenReturn("Virat Kohli");
	when(session.getAttribute("userRole")).thenReturn("ADMIN");
	//mocking issueAdminService findById to return mocked issue
	when(issueAdminService.findById(1)).thenReturn(newissues1);
	//Mocking userRepository findById to return mocked user
	when(userRepository.findById(10)).thenReturn(mockUser);
	ModelAndView model = new ModelAndView();
	model = issueAdminController.issuestatusupdate(1,request);
	//Assert Statements
	assertTrue(model.getModel().containsKey("display_Issuestatus"));
	assertTrue(model.getModel().containsValue("Solved"));
	
}


}

