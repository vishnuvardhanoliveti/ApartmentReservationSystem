/*This class contains all the methods of the Requests Service which we have to implement. It is an abstract class

 * @author  Devan Tarun Kumar
 * Written on:November 10,2018
 */

package com.apartmentReservation.service;

import java.util.List;

import com.apartmentReservation.model.Requests;
import com.apartmentReservation.model.User;

public interface SendRequestsService {

	public List<User> findAll();

	public Requests saveRequest(Requests request);
	public List<Requests> getReceivedRequests(String email);
	public List<Requests> getSentRequests(String email);
	public void updateRequestStatus(String useremail, String requesteremail, String Status);
	public Requests checkIfAlreadyRequestSent(String useremail, String requestedEmail);
}
