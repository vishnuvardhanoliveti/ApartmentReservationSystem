/*This class contains all the methods of the Service which we have to implement. It is an abstract class*/
package com.apartmentReservation.service;

import java.util.List;

import com.apartmentReservation.model.Backgroundverification;
import com.apartmentReservation.model.User;
public interface BackgroundVerificationAndLeaseAdminService {
	public List<Backgroundverification> findAllApplications();
	public List<Backgroundverification> findAllApplicationsofOwner(Integer id);
	public Backgroundverification updateStatus(String email, String status);
	public Backgroundverification updateOwnerSign(String email, String status);
	public Backgroundverification findByEmail(String email);
	
	public  void saveLeaseDetails(String status, Object email);
}
