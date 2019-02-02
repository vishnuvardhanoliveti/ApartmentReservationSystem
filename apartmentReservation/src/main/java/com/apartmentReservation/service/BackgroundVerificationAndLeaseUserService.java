/*This class contains all the methods of the Service which we have to implement. It is an abstract class*/
package com.apartmentReservation.service;

import com.apartmentReservation.model.Backgroundverification;


public interface BackgroundVerificationAndLeaseUserService {

	public Backgroundverification findUserByEmail(String email);
	 
	 public Backgroundverification saveUser(Backgroundverification backgroundverification);
	 public void updateUser(String emailrm1, String firstnamerm1, String lastnamerm1, String otherroomates, String emailrm2, String firstnamerm2, String lastnamerm2,String useremail);

	public  void saveLeaseDetails(String sign, Object email, String leaseSignedDate,String leaseStartDate,	String leaseEndDate);
		
	public void updateLeaseStatus(String status, String docname, String email);
	
	public int getPropertyId(String status,String email);
}
