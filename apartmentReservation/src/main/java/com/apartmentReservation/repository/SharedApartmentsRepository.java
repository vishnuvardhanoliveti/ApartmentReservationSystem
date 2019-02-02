//This class handles all the database operations related to the SHaredApartments*/
package com.apartmentReservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.apartmentReservation.model.SharedApartment;

@Repository("sharedApartmentsRepository")
public interface SharedApartmentsRepository extends JpaRepository<SharedApartment,Long> {
	/**
	 * Description:This method is used to find if a user email exists in SharedApartment table .			   
	 * @Input:The input expected is the user email . 
	 * @return : SharedApartemnt object.
	 * @author : Vishnu Vardhan Oliveti
	 * Written on:November 22, 2018
	
	 */
	SharedApartment findByUserEmail(String email);

}
