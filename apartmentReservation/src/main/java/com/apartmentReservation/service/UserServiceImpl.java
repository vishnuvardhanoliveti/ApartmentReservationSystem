/*This class handles all the services related to  user such as find user by email,save user*/
package com.apartmentReservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.apartmentReservation.model.Role;
import com.apartmentReservation.model.User;
import com.apartmentReservation.model.UserRole;
import com.apartmentReservation.repository.CustomRepository;
import com.apartmentReservation.repository.RoleRepository;
import com.apartmentReservation.repository.UserRepository;

import java.util.Arrays;
import java.util.HashSet;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CustomRepository customRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
//This method is used to retrieve user record when an email is given
	@Override
	public User findUserByEmail(String email) {
		User user = new User();
		user = userRepository.findByEmail(email);
		return user;
	}
//This method is used to save a user during registration.
	@Override
	public User saveUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setActive(1);
		Role userRole = roleRepository.findByRole("CUSTOMER");
		user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		User createdUser=userRepository.save(user);
		return createdUser;
	}

	//This method is used to fetch user role based on his id
	@Override
	public String userRole(Integer userid) {
		UserRole role = customRepository.findByUserId(userid);
		String roleName;
		if (role.getRoleId() == 1) {
			roleName = "ADMIN";
		} else if (role.getRoleId() == 2) {
			roleName = "CUSTOMER";
		} else {
			roleName = "OWNER";
		}
		return roleName;
	}
}