/*This class handles services related to the Issues like getting and updating  the customer's issues details by the Admin in the Service Issue Portal*/

package com.apartmentReservation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.apartmentReservation.model.Issue;
import com.apartmentReservation.repository.IssueRepository;

@Service("issueAdminService")
public  class IssueAdminServiceImpl implements IssueAdminService {
	@Autowired
	private IssueRepository issueRepository;

	
	/**
	 * Description:This method is used to get all the  Issues from database.			   
	 * @Input: The input expected is the Id. 
	 * @return : This method returns a Issue list object from the database.
	 * @author : Kocherla Shaline Sreemati.
	 * Written on: Novemebr 2, 2018
	 * Modified on: November 6, 2018
	 */
	@Override
	public List<Issue> findAllApplications() {
		// TODO Auto-generated method stub
		return issueRepository.findAll();
	}
	
	/**
	 * Description:This method is used to update status of the Issue in database by  passing ID and Status as input.			   
	 * @Input: The input expected is the id and status. 
	 * @return : This method returns a Issue object from the database.
	 * @author : Kocherla Shaline Sreemati.
	 * Written on: Novemebr 2, 2018
	 * Modified on: November 6, 2018
	 */
	@Override
	public Issue updateStatus(int id, String status) {
		Issue updatestatus = issueRepository.findByIssueId(id);
		updatestatus.setIssueStatus(status);
		Issue application=issueRepository.save(updatestatus);
		return application;
	}
	
	/**
	 * Description:This method is used to get the Issue object from database by  passing ID as input.			   
	 * @Input: The input expected is the id. 
	 * @return : This method returns a Issue object from the database.
	 * @author : Kocherla Shaline Sreemati.
	 * Written on: Novemebr 2, 2018
	 * Modified on: November 6, 2018
	 */
	@Override
	public Issue findById(int id) {
		// TODO Auto-generated method stub
		Issue issue = issueRepository.findByIssueId(id);
		return issue;
	}
	
	/**
	 * Description:This method is used to save the status of the Issue in database by  passing id, status and date as input.			   
	 * @Input: The input expected is the id, status and date. 
	 * @return : This method returns a Issue object from the database.
	 * @author : Kocherla Shaline Sreemati.
	 * Written on: Novemebr 2, 2018
	 * Modified on: November 6, 2018
	 */
	@Override
	public Integer saveDetails(int id, String status,String date) {
		// TODO Auto-generated method stub
		 return issueRepository.saveIssueDetails(status,id,date);
		 
	}
	
	/**
	 * Description:This method is used to get the Issues details based on the Owner's property from database by  passing id, status and date as input.			   
	 * @Input: The input expected is the id. 
	 * @return : This method returns a Issue list object from the database.
	 * @author : Devan Tarun Kumar.
	 * Written on: Novemebr 2, 2018
	 * Modified on: November 6, 2018
	 */
	@Override
	public List<Issue> findByPropertyOwnerId(Integer id) {
		// TODO Auto-generated method stub
		List<Issue> issue = issueRepository.findByPropertyOwner(id);
		return issue;
	}

}
