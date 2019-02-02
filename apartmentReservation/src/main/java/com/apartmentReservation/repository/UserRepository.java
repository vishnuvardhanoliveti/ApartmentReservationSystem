//This class handles all the database operations related to table user*/
package com.apartmentReservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.apartmentReservation.model.Issue;
import com.apartmentReservation.model.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
 
 User findByEmail(String email);
 
 User findById(int id);
}