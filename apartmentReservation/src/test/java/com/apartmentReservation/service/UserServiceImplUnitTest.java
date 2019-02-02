//Description: testing the user service methods 
package com.apartmentReservation.service;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.apartmentReservation.model.User;
import com.apartmentReservation.model.UserRole;
import com.apartmentReservation.repository.CustomRepository;
import com.apartmentReservation.repository.RoleRepository;
import com.apartmentReservation.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class UserServiceImplUnitTest {
	@Mock
	private UserRepository userRepository;
	@Mock
	private RoleRepository roleRepository;
	@Mock
	private CustomRepository customRepository;
	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@InjectMocks
	private UserServiceImpl userServiceImpl;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	User mockUser = User.builder().email("virat@gmail.com").firstName("Virat").lastName("Kohli").password("sample")
			.phoneNumber("123-456-7678").addressLine1("University Manor").addressLine2("Stella Street").city("Denton")
			.state("TX").zip("76201").build();

	/*
	 * Description: test if user is saved
	 *  input: user 
	 *  output:checking if user name is as expected
	 */
	@Test
	public void testSaveUser() {
		User testUser = new User();
		testUser.setPassword("sample");
		when(userRepository.save(any(User.class))).thenReturn(mockUser);
		User createdUser = userServiceImpl.saveUser(testUser);
		assertEquals("Virat", createdUser.getFirstName());
	}

	/*
	 * Description: test if user is found with his email 
	 * input: email 
	 * output:checking user name and user email
	 */
	@Test
	public void testFindUserByEmail() {
		when(userRepository.findByEmail(any())).thenReturn(mockUser);
		User user = userServiceImpl.findUserByEmail("virat@gmail.com");
		assertEquals("Virat", user.getFirstName());
		assertEquals("virat@gmail.com", user.getEmail());
	}

	/*
	 * Description: test for finding user role 
	 * input: user id 
	 * output: checking userrole name
	 */
	@Test
	public void testFindUserRole() {
		UserRole role1 = UserRole.builder().id(1).userId(1).roleId(1).build();
		UserRole role2 = UserRole.builder().id(2).userId(2).roleId(2).build();
		UserRole role3 = UserRole.builder().id(3).userId(3).roleId(3).build();

		when(customRepository.findByUserId(1)).thenReturn(role1);
		String roleName1 = userServiceImpl.userRole(1);
		assertEquals("ADMIN", roleName1);

		when(customRepository.findByUserId(2)).thenReturn(role2);
		String roleName2 = userServiceImpl.userRole(2);
		assertEquals("CUSTOMER", roleName2);

		when(customRepository.findByUserId(3)).thenReturn(role3);
		String roleName3 = userServiceImpl.userRole(3);
		assertEquals("OWNER", roleName3);
	}
}
