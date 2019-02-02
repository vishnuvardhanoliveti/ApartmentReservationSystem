/*This class handles all the database operations related to table backgroundverification during administrators operations.*/
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

public interface BackgroundVerificationAndLeaseAdminRepository extends JpaRepository<Backgroundverification, Long> {
	
	public List<Backgroundverification> findAll();
	
	Backgroundverification findByEmail(String email);
	
	public List<Backgroundverification> findByPropertyOwner(Integer id);
	
	@Transactional
	 @Modifying
	 @Query("update Backgroundverification bv set bv.app_bgc_status = ?1 where bv.email = ?2"   )
	void saveLeaseDetails(String status, String email);
	
}