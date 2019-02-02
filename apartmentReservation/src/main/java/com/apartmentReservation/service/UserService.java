/*This class contains all the methods of the Service which we have to implement. It is an abstract class*/
package com.apartmentReservation.service;

import com.apartmentReservation.model.User;

public interface UserService {
  
 public User findUserByEmail(String email);
 
 public User saveUser(User user);
 
 public String userRole(Integer userid);
}