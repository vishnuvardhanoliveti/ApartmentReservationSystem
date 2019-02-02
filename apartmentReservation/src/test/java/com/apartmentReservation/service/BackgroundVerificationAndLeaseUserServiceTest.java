//Description: Test the customer prospective of background verification and lease application
package com.apartmentReservation.service;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
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
import com.apartmentReservation.model.Backgroundverification;
import com.apartmentReservation.model.Properties;
import com.apartmentReservation.model.User;
import com.apartmentReservation.model.UserRole;
import com.apartmentReservation.repository.BackgroundVerificationAndLeaseUserRepository;
import com.apartmentReservation.repository.CustomRepository;
import com.apartmentReservation.repository.RoleRepository;
import com.apartmentReservation.repository.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class BackgroundVerificationAndLeaseUserServiceTest {
	@Mock
	private BackgroundVerificationAndLeaseUserRepository backgroundVerificationAndLeaseUserRepository;
	@InjectMocks
	private BackgroundVerificationAndLeaseUserServiceImpl backgroundVerificationAndLeaseUserServiceImpl;
	List<Backgroundverification> StringList = new ArrayList<>();

	Backgroundverification backgroundverification = Backgroundverification.builder().email("shaline@gmail.com")
			.firstname("shaline").lastname("Kocherla").app_bgc_status("accept").ownerSign(null).propertyId(22).build();


	@Before
	public void setup() {

		MockitoAnnotations.initMocks(this);
	}

	/*
	 * Description: test if correct customer is returned with email 
	 * input: user email 
	 * output: checking if correct customer email is returned
	 */
	@Test
	public void testFindUserByEmail() {
		when(backgroundVerificationAndLeaseUserRepository.findByEmail("shaline@gmail.com"))
				.thenReturn(backgroundverification);
		Backgroundverification backgroundverification = backgroundVerificationAndLeaseUserServiceImpl
				.findUserByEmail("shaline@gmail.com");
		assertEquals("shaline@gmail.com", backgroundverification.getEmail());
	}

	/*
	 * Description: test if customer is saved 
	 * input: user who filled background verification 
	 * output: checking if correct customer email is returned
	 */
	@Test
	public void testSaveUser() {
		when(backgroundVerificationAndLeaseUserRepository.save(any(Backgroundverification.class)))
				.thenReturn(backgroundverification);
		Backgroundverification backgroundverification1 = backgroundVerificationAndLeaseUserServiceImpl
				.saveUser(backgroundverification);
		assertEquals("shaline@gmail.com", backgroundverification1.getEmail());
	}

}