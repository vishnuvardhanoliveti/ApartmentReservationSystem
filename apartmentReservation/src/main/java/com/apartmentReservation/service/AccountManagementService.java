/*This class contains all the methods of the AccountManagement Service which we have to implement. It is an abstract class
 * @author  Vishnu Vardhan Oliveti
* Written on:October 31,2018
 */
package com.apartmentReservation.service;

import com.apartmentReservation.model.User;

public interface AccountManagementService {
	public String updateUser(User user);
	
	public String updatePassword(User user);
	

}
