//Description: testing user service implementation (Integration test)
package com.apartmentReservation.service;

import static org.junit.Assert.*;
import javax.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import com.apartmentReservation.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Transactional
public class UserServiceImplTest {
	@Autowired
	private UserService userService;

	/*
	 * Description: testing if user is saved 
	 * input: user 
	 * output: checking if user name is as expected
	 */
	@Test
	public void testSaveUser() {
		User newUser = new User();
		newUser.setEmail("virat@gmail.com");
		newUser.setFirstName("Virat");
		newUser.setLastName("Kohli");
		newUser.setPassword("sample");
		newUser.setPhoneNumber("123-456-7891");
		newUser.setAddressLine1("dfhjdf");
		newUser.setAddressLine2("fvdjhfb");
		newUser.setCity("fkjf");
		newUser.setState("TX");
		newUser.setZip("43734");
		User createdUser = userService.saveUser(newUser);
		assertNotNull(createdUser);
		assertNotNull(createdUser.getId());
		assertEquals("Virat", createdUser.getFirstName());
	}
}
