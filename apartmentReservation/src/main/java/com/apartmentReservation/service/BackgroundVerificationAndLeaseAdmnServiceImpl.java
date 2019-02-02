/*This class handles all the services related to  administrator during  background verification and leasing */
package com.apartmentReservation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apartmentReservation.model.Backgroundverification;
import com.apartmentReservation.repository.BackgroundVerificationAndLeaseAdminRepository;

@Service("bvapplicationService")
public class BackgroundVerificationAndLeaseAdmnServiceImpl implements BackgroundVerificationAndLeaseAdminService {
	@Autowired
	private BackgroundVerificationAndLeaseAdminRepository backgroundVerificationAndLeaseAdminRepository;

	/*This method is used for fetching and returning all background verification applications to administrator*/
	@Override
	public List<Backgroundverification> findAllApplications() {
		// TODO Auto-generated method stub
		return backgroundVerificationAndLeaseAdminRepository.findAll();
	}

	/*This method is used for updating the status of a background verification application based on email by administrator*/
	@Override
	public Backgroundverification updateStatus(String email, String status) {
		// TODO Auto-generated method stub
		Backgroundverification updatestatus = backgroundVerificationAndLeaseAdminRepository.findByEmail(email);
		updatestatus.setApp_bgc_status(status);
		Backgroundverification application=backgroundVerificationAndLeaseAdminRepository.save(updatestatus);
		return application;
	}
	
	/*This method is used for fetching the background verification application based on email by administrator*/
	@Override
	public Backgroundverification findByEmail(String email) {
		// TODO Auto-generated method stub
		Backgroundverification backgroundverification = backgroundVerificationAndLeaseAdminRepository.findByEmail(email);
		return backgroundverification;
	}

	/*This method is used for fetching and returning all the properties based on ownerid to owner*/
	@Override
	public List<Backgroundverification> findAllApplicationsofOwner(Integer id) {
		// TODO Auto-generated method stub
		return backgroundVerificationAndLeaseAdminRepository.findByPropertyOwner(id);	
	}

	/*This method is used for saving the sign for a customer's background verification application in database by owner*/
	@Override
	public Backgroundverification updateOwnerSign(String email, String status) {
		// TODO Auto-generated method stub
		Backgroundverification updatestatus = backgroundVerificationAndLeaseAdminRepository.findByEmail(email);
		updatestatus.setOwnerSign(status);
		return backgroundVerificationAndLeaseAdminRepository.save(updatestatus);
	}

	/*This method is used for saving the accept/reject response for a customer's background verification application in database by admin*/
	@Override
	public void saveLeaseDetails(String status, Object email) {
		// TODO Auto-generated method stub
		 String reqEmail = String.valueOf(email);
			 backgroundVerificationAndLeaseAdminRepository.saveLeaseDetails(status,reqEmail);
	}
}