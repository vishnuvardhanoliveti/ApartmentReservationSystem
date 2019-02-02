/*This class handles all the services related to  customer during  background verification and leasing */
package com.apartmentReservation.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apartmentReservation.model.Backgroundverification;
import com.apartmentReservation.repository.BackgroundVerificationAndLeaseUserRepository;

@Service("backgvservice")
public class BackgroundVerificationAndLeaseUserServiceImpl implements BackgroundVerificationAndLeaseUserService {

	@Autowired
	private BackgroundVerificationAndLeaseUserRepository backgroundVerificationAndLeaseUserRepository;
	
	/*This method is used for finding customer account based on email*/
	@Override
	public Backgroundverification findUserByEmail(String email) {
		// TODO Auto-generated method stub
		return backgroundVerificationAndLeaseUserRepository.findByEmail(email);
	}
	
	/*This method is used for saving customer details and creating an account*/
	@Override
	public Backgroundverification saveUser(Backgroundverification backgroundverification) {
		// TODO Auto-generated method stub
		
		return backgroundVerificationAndLeaseUserRepository.save(backgroundverification);
	}
	
	/*This method is used for saving customer's sign and dates of lease to database*/
	@Override
	public void saveLeaseDetails(String sign, Object email, String leaseSignedDate,String leaseStartDate,
			String leaseEndDate) {
		 String reqEmail = String.valueOf(email);
		  backgroundVerificationAndLeaseUserRepository.saveLeaseDetails(sign,reqEmail,leaseSignedDate,leaseStartDate,leaseEndDate);
		
	}

	/*This method is used for saving customer's lease status and lease document name to database*/
	@Override
	public void updateLeaseStatus(String status, String docname, String email) {
		// TODO Auto-generated method stub
		backgroundVerificationAndLeaseUserRepository.updateLeaseStatus(status,docname,email);
	}
	
	/*This method is used for updating customer's background verification details to database*/
	@Override
	public void updateUser(String emailrm1, String firstnamerm1, String lastnamerm1, String otherroomates, String emailrm2, String firstnamerm2, String lastnamerm2,String useremail) {
		// TODO Auto-generated method stub
		 backgroundVerificationAndLeaseUserRepository.updateUser(emailrm1, firstnamerm1, lastnamerm1, otherroomates, emailrm2, firstnamerm2, lastnamerm2, useremail);
		
	}
	
	/*This method is used for getting property id of customer*/
	@Override
	
	public int getPropertyId(String status, String email) 
	{
		// TODO Auto-generated method stub
		Backgroundverification b =backgroundVerificationAndLeaseUserRepository.findByEmail(email);
		int id=b.getPropertyId();
		//int id = backgroundVerificationAndLeaseUserRepository.getPropertyIdNumber(status);

		return id;
	}
	

}
