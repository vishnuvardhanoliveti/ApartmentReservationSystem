//This class handles all the database operations related to table backgroundverification during users operations.*/
package com.apartmentReservation.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.apartmentReservation.model.Backgroundverification;
import com.apartmentReservation.model.User;

@Repository("backRepository")
public interface BackgroundVerificationAndLeaseUserRepository extends JpaRepository<Backgroundverification, Long> {
	
	
	//System.out.println(backgv.getFirstname());
	 Backgroundverification findByEmail(String email);

	 @Transactional
	 @Modifying
	 @Query("update Backgroundverification bv set bv.initials = ?1, bv.leaseSignedDate = ?3, bv.leaseStartDate =?4, bv.leaseEndDate =?5 where bv.email = ?2"   )
	 void saveLeaseDetails(String sign, String email, String leaseSignedDate, String leaseStartDate,
			String leaseEndDate);
	
	 @Transactional
	 @Modifying
	 @Query("update Backgroundverification bv set bv.app_lease_status = ?1, bv.leasedocname = ?2 where bv.email = ?3"   )
	void updateLeaseStatus(String status, String docname, String email);
	 

	 @Transactional
	 @Modifying
	 @Query("update Backgroundverification bv set bv.emailid_rm1 = ?1, bv.firstname_rm1 = ?2, bv.lastname_rm1 = ?3, bv.otherroommates = ?4, bv.emailid_rm2 = ?5, bv.firstname_rm2 = ?6, bv.lastname_rm2 = ?7 where bv.email = ?8"   )
	void updateUser(String emailrm1, String firstnamerm1, String lastnamerm1, String otherroomates, String emailrm2, String firstnamerm2, String lastnamerm2 ,String useremail);
	 
	}
