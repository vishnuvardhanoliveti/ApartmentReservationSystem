/*This class handles services related to the Issues like getting and storing  the customer's issues details based on the values provided by the customer in the Service Issue Portal*/

package com.apartmentReservation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apartmentReservation.model.Issue;
import com.apartmentReservation.repository.BackgroundVerificationAndLeaseAdminRepository;
import com.apartmentReservation.repository.IssueRepository;

@Service("issueUserService")
public class IssueUserServiceImpl implements IssueUserService {

	@Autowired
	private IssueRepository issueRepository;
	@Override
	
	/**
	 * Description:This method is used to save Issues to database.			   
	 * @Input: The input expected is the  Issue. 
	 * @return : This method returns a Issue object from the database.
	 * @author : Devan Tarun Kumar.
	 * Written on:Novemebr 1, 2018
	 * Modified on: November 6, 2018
	 */
	public Issue saveIssue(Issue issue) {
		return issueRepository.save(issue);
	}
	
	

	/**
	 * Description:This method is used to get the Issue object from database by  passing userEmail as input.			   
	 * @Input: The input expected is the userEmail. 
	 * @return : This method returns a Issue list object from the database.
	 * @author : Devan Tarun Kumar.
	 * Written on: Novemebr 1, 2018
	 * Modified on: November 6, 2018
	 */
	@Override
	public List<Issue> findByIssueUserEmail(String email) {
		return issueRepository.findByIssueUserEmail(email);
	}

}
