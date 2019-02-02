/*This class handles services related to the Requests like sending and receiving  the customer's requests details */

package com.apartmentReservation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apartmentReservation.model.Requests;
import com.apartmentReservation.model.User;
import com.apartmentReservation.repository.RequestsRepository;
import com.apartmentReservation.repository.UserRepository;

@Service
public class SendRequestsServiceImpl implements SendRequestsService{


	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RequestsRepository requestsRepository;
	
	/**
	 * Description:This method is used to get user.			   
	 * @Input: The input expected is the  user. 
	 * @return : This method returns a user object from the database.
	 * @author : Devan Tarun Kumar.
	 * Written on:Novemebr 10, 2018
	 * Modified on: November 16, 2018
	 */
	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return userRepository.findAll();

	}
	
	/**
	 * Description:This method is used to save requests.			   
	 * @Input: The input expected is the  request. 
	 * @return : This method returns a request object from the database.
	 * @author : Devan Tarun Kumar.
	 * Written on:Novemebr 10, 2018
	 * Modified on: November 16, 2018
	 */
	@Override
	public Requests saveRequest(Requests request) {
		requestsRepository.save(request);
		return null;
	}
	
	/**
	 * Description:This method is used to get received requests.			   
	 * @Input: The input expected is the  request. 
	 * @return : This method returns a request object from the database.
	 * @author : Devan Tarun Kumar.
	 * Written on:Novemebr 10, 2018
	 * Modified on: November 16, 2018
	 */
	@Override
	public List<Requests> getReceivedRequests(String email) {
		// TODO Auto-generated method stub
		return requestsRepository.findByRequestedUserEmail(email);
		 
	}
	
	/**
	 * Description:This method is used to get sent requests.			   
	 * @Input: The input expected is the  request. 
	 * @return : This method returns a request object from the database.
	 * @author : Devan Tarun Kumar.
	 * Written on:Novemebr 10, 2018
	 * Modified on: November 16, 2018
	 */
	@Override
	public List<Requests> getSentRequests(String email) {
		// TODO Auto-generated method stub
		return requestsRepository.findByUserEmail(email);
		 
	}
	/**
	 * Description:This method is used to get update request status.			   
	 * @Input: The input expected is the  request. 
	 * @return : This method returns a request object from the database.
	 * @author : Devan Tarun Kumar.
	 * Written on:Novemebr 10, 2018
	 * Modified on: November 16, 2018
	 */
	@Override
	public void updateRequestStatus(String useremail, String requesteremail, String Status) {
		// TODO Auto-generated method stub
		
		 requestsRepository.updateRequestStatus(Status,useremail,requesteremail);
	}

	/**
	 * Description:This method is used to  check if Request already sent			   
	 * @Input: The input expected is the  request. 
	 * @return : This method returns a request object from the database.
	 * @author : Devan Tarun Kumar.
	 * Written on:Novemebr 12, 2018
	 * Modified on: November 16, 2018
	 */
	@Override
	public Requests checkIfAlreadyRequestSent(String useremail, String requestedEmail) {
		// TODO Auto-generated method stub
		return requestsRepository.find(useremail, requestedEmail);
	}

}
