//Description: test various methods in issue Service Administrator test
package com.apartmentReservation.service;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.apartmentReservation.model.Backgroundverification;
import com.apartmentReservation.model.Issue;
import com.apartmentReservation.repository.IssueRepository;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class IssueAdminServiceImplTest {
	@Mock
	TypedQuery<Backgroundverification> typedQuery;
	@Mock
	private IssueRepository issueRepository;
	@InjectMocks IssueAdminServiceImpl issueAdminServiceImpl;
	List<Issue> StringList = new ArrayList<>();
	//creating mocked instances of issue
	Issue issue = Issue.builder()
			.issueId(10)
			.issueSubmittedDate("12-12-18")
			.issueStatus(null)
			.build();

	public void compute(String e, String status, int c, int p) {
		Issue issue1 = Issue.builder()
				.issueId(c)
				.issueSubmittedDate(e)
				.issueStatus("Solved")
				.propertyOwner(p)
				.build();

		StringList.add(issue1);
	}
//this method runs before evry test
@Before
public void setup() {
	MockitoAnnotations.initMocks(this);
	compute("12-10-19", null, 1,20);
	compute("10-09-20", null, 22,20);
	compute("11-08-20", null, 22,20);
	compute("03-05-17", null, 22,20);
	compute("09-03-19", null, 22,20);
	compute("01-02-17", null, 22,20);
}
/**
 * Description: Test if admin can see all issues of customers 

 * @Input:-
 * @Output: checking if correct number of issues are present
 * @author: Kocherla Shaline 
 * Written on:November 5,2018
 * Modified on: November 6, 2018
 */
@Test
public void testFindAllApplication() {
	//mocking the issueRepositoryAdmin findAll to return list of mocked issues
	when(issueRepository.findAll()).thenReturn(StringList);
	
	List<Issue> StringList1 = issueAdminServiceImpl.findAllApplications();
	//Assert statements
	assertEquals(6, StringList1.size());
}
/**
 * Description: Test if Admin can update status 
 * @Input:status and issue  ID 
 * @Output: checking if the status is updated
 * @author: Kocherla Shaline 
 * Written on:November 5,2018
 * Modified on: November 6, 2018
 */

@Test
public void testUpdateStatus() {
	String status = "Solved";
	int id=10;
	//mocking the issueRepositoryAdmin findByIssueId to return mocked issue
	when(issueRepository.findByIssueId(10))
			.thenReturn(issue);
	//mocking the issueRepositoryAdmin save to return mocked issue
	when(issueRepository.save(issue))
			.thenReturn(issue);
	
	Issue Issue2 = issueAdminServiceImpl.updateStatus(id, status);
	//Assert statements
	assertEquals("Solved", Issue2.getIssueStatus());

}
/**
 * Description: Test if Admin can find issue by its Id

 * @Input: issue  ID 
 * @Output: checking if correct issue id is present
 * @author: Kocherla Shaline 
 * Written on:November 5,2018
 * Modified on: November 6, 2018
 */
@Test
public void testFindByIssueId() {
	int id=10;
	//mocking issueRepositoryAdmin findByIssueId to return mocked issue
	when(issueRepository.findByIssueId(10))
	.thenReturn(issue);
	Issue Issue = issueAdminServiceImpl.findById(id);
	//Assert statements
	assertEquals(10, Issue.getIssueId());
}
/**
 * Description: Test if Admin can save issue details

 * @Input:status and issue  ID and date
 * @Output: checking the id 
 * @author: Kocherla Shaline 
 * Written on:November 5,2018
 * Modified on: November 6, 2018
 */
@Test
public void testSaveIssueDetails()
{  
	int id = 11;
	//mocked issueRepositoryAdmin saveIssueDetails to return mocked issue id 
	when(issueRepository.saveIssueDetails("Solved",10,"12-12-18"))
	.thenReturn(id);
	int id2 = issueAdminServiceImpl.saveDetails(10,"Solved","12-12-18");
	//Assert statements
	assertEquals(11, id2);
}
/**
 * Description: Test if Admin can find by property owner id

 * @Input: owner id
 * @Output: checking if correct number of issues related to property owner are present
 * @author: Kocherla Shaline 
 * Written on:November 5,2018
 * Modified on: November 6, 2018
 */
@Test
public void testfindByPropertyOwnerId() {
	//mocked issueRepositoryAdmin findByPropertyOwner to return list of mocked issues
	when(issueRepository.findByPropertyOwner(20)).thenReturn(StringList);
	List<Issue> StringList1 = issueAdminServiceImpl.findByPropertyOwnerId(20);
	//Assert statements
	assertEquals(6, StringList1.size());
}


}
