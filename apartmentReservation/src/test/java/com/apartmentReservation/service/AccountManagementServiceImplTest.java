//Testing several methods in account management service 
package com.apartmentReservation.service;
import static org.junit.Assert.*;
import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.apartmentReservation.model.User;
import com.apartmentReservation.repository.AccountManagementRepository;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)

public class AccountManagementServiceImplTest {
	@Mock
	private AccountManagementRepository accountManagementRepository;
	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@InjectMocks
	private AccountManagementServiceImpl accountManagementServiceImpl;
	//This method is run before every test
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	//Creating a mocked instance of user object.
	User mockUser = User.builder()
			.email("viratnew@gmail.com")
			.firstName("Viratnew")
			.lastName("Kohlinew")
			.password("sample")
			.phoneNumber("123-456-7678")
			.addressLine1("University Manor")
			.addressLine2("Stella Street")
			.city("Denton")
			.state("TX")
			.zip("76201")
			.build();
	
	
	/**
	 * Description:Testing if customer can update his details 
	 * @Input:mocked User
	 * @Output: String stating success
	 * @author: Kocherla Shaline
	 * Written on:November 5,2018
	 * Modified on: November 6, 2018
	 */
	@Test
	public void testUpdateUser()
	{
		String output = accountManagementServiceImpl.updateUser(mockUser);
		//Assert statements
		assertEquals("successfully updated",output);
		
	}
	/**
	 * Description:Testing if customer can update his Password 
	 * @Input:mocked User
	 * @Output: String stating successfully updated password
	 * @author: Kocherla Shaline
	 * Written on:November 9,2018
	 * Modified on: November 10, 2018
	 */
	@Test
	public void testUpdatePassword()
	{
		String output = accountManagementServiceImpl.updatePassword(mockUser);
		//Assert statements
		assertEquals("successfully updated password",output);
		
	}
	
}
