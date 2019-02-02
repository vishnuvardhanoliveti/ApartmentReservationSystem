//This class handles all the database operations related to table user_role*/
package com.apartmentReservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apartmentReservation.model.User;
import com.apartmentReservation.model.UserRole;

@Repository("customRepository")
public interface CustomRepository extends JpaRepository<UserRole, Long> {
	UserRole findByUserId(int userId);
}
