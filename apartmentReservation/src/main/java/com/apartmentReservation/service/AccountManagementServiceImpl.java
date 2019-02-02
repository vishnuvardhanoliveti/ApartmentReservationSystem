/*This class handles services related to the AccountManagement like updating the user details based on the values provided by the user in the AccountManagement form*/
package com.apartmentReservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.apartmentReservation.model.User;
import com.apartmentReservation.repository.AccountManagementRepository;
import com.apartmentReservation.repository.CustomRepository;
import com.apartmentReservation.repository.RoleRepository;
import com.apartmentReservation.repository.UserRepository;


@Service("accountManagementService")
public class AccountManagementServiceImpl implements AccountManagementService {
	@Autowired
	AccountManagementRepository accountManagementRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	/**
	 * Description:This method is used to update the user details  such as phoneNumber,address etc in the database.			   
	 * @Input:The input expected is the  the user object posted from the form . 
	 * @return: This method returns a successfully update message.
	 * @author: Vishnu Vardhan Oliveti
	 * Written on:October 31,2018
	 * Modified on: Novemebr 6, 2018
	 */
		@Override
		public String updateUser(User user) {
	
			accountManagementRepository.updateUser(user.getPhoneNumber(),user.getAddressLine1(),user.getAddressLine2(),user.getCity(),user.getState(),user.getZip(),user.getEmail());
			
			return "successfully updated";
		}
		
		
		/**
		 * Description:This method is used to update the user password in the database.			   
		 * @Input:The input expected is the  the user object posted from the form . 
		 * @return: This method returns a successfully updated message.
		 * @author: Vishnu Vardhan Oliveti
		 * Written on:November 8,2018
		 * 
		 */
	@Override
	public String updatePassword(User user) {
		
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		accountManagementRepository.updatePassword(user.getPassword(), user.getEmail());
		return "successfully updated password";
	}
}
