//Testing various methods in IssueServiceUserService Controller 
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
public class IssueUserServiceImplTest {
	@Mock
	TypedQuery<Backgroundverification> typedQuery;
	@Mock
	private IssueRepository issueRepository;
	@InjectMocks IssueUserServiceImpl issueUserServiceImpl;
	List<Issue> StringList = new ArrayList<>();
	//Creating a mocked instance of issue object.
	Issue issue = Issue.builder()
			.issueId(10)
			.issueSubmittedDate("12-12-18")
			.issueStatus(null)
			.build();

	public void compute(String e,String email, String status, int c, int p) {
		Issue issue1 = Issue.builder()
				.issueId(c)
				.issueUserEmail(email)
				.issueSubmittedDate(e)
				.issueStatus("solved")
				.propertyOwner(p)
				.build();

		StringList.add(issue1);
	}
//THis method before every test
@Before
public void setup() {
	MockitoAnnotations.initMocks(this);
	compute("12-10-19","a@gmail.com", null, 1,20);
	compute("10-09-20","a@gmail.com", null, 22,20);
	compute("11-08-20","a@gmail.com", null, 22,20);
	compute("03-05-17","a@gmail.com", null, 22,20);
	compute("09-03-19","a@gmail.com", null, 22,20);
	compute("01-02-17","a@gmail.com", null, 22,20);
}
/**
 * Description:This method will test if we can find issue by user email.
 * @Input:mocked issue email
 * @Output: list of mocked issues
 * @author: kocherla shaline
 * Written on:November 5,2018
 * Modified on: November 6, 2018
 */
@Test
public void testFindByIssueUserEmail()
{
	//mocking issueRepository findByIssueUserEmail to return list of mocked issues
	when(issueRepository.findByIssueUserEmail("a@gmail.com")).thenReturn(StringList);
	List<Issue> issue=issueUserServiceImpl.findByIssueUserEmail("a@gmail.com");
	//Assert statements
	assertEquals(6, issue.size());

}


}



