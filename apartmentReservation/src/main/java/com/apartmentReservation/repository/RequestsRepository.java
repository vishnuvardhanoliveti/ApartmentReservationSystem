package com.apartmentReservation.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.apartmentReservation.model.Properties;
import com.apartmentReservation.model.Requests;

@Repository("requestsRepository")
public interface RequestsRepository extends JpaRepository<Requests, Integer>{
	
public List<Requests> findByUserEmail(String email);
public List<Requests> findByRequestedUserEmail(String email);



@Transactional
 @Modifying
@Query("update Requests r set r.userDecision = ?1 where r.userEmail = ?3 and r.requestedUserEmail = ?2 "   )
Integer updateRequestStatus(String status, String useremail, String requestedemail);


@Query("SELECT r from Requests r  where r.userEmail = ?1 and r.requestedUserEmail = ?2" )
public Requests find(@Param("useremail") String useremail,
		@Param("requestedemail") String requestedemail);
}
