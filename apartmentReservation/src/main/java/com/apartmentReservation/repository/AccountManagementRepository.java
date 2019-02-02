//This class handles all the database operations related to updating the user details*/

package com.apartmentReservation.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.apartmentReservation.model.User;
@Repository
public interface AccountManagementRepository extends JpaRepository<User, Long> {
	
	/**
	 * Description:This method is used to update the user details in  database .			   
	 * @Input:The input expected is the password, phoneNumber, address,city,state,zip,email . 
	 * @return : not applicable.
	 * @author : Vishnu Vardhan Oliveti
	 * Written on:October 31, 2018
	 * Modified on: Novemebr 6, 2018
	 */
	@Transactional
	 @Modifying
	 @Query("update User u set  u.phoneNumber = ?1 ,u.addressLine1 = ?2 ,u.addressLine2 = ?3,u.city = ?4, u.state= ?5,u.zip = ?6 where u.email = ?7"   )
	void updateUser( String phoneNumber, String addressLine1,String addressLine2,String city, String state, String zip,String email );

	@Transactional
	 @Modifying
	 @Query("update User u set u.password = ?1 where u.email = ?2"   )
	void updatePassword(String password,String email);
}
