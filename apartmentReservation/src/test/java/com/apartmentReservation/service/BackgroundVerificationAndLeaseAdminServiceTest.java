//Description: Test for verifying admin,owner prospective of background  and lease application
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
import com.apartmentReservation.repository.BackgroundVerificationAndLeaseAdminRepository;
import com.apartmentReservation.repository.BackgroundVerificationAndLeaseUserRepository;
import com.apartmentReservation.repository.CustomRepository;
import com.apartmentReservation.repository.RoleRepository;
import com.apartmentReservation.repository.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.TypedQuery;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class BackgroundVerificationAndLeaseAdminServiceTest {
	@Mock
	private BackgroundVerificationAndLeaseAdminRepository backgroundVerificationAndLeaseAdminRepository;
	@Mock
	TypedQuery<Backgroundverification> typedQuery;
	List<Backgroundverification> StringList = new ArrayList<>();
	@InjectMocks
	private BackgroundVerificationAndLeaseAdmnServiceImpl backgroundVerificationAndLeaseAdmnServiceImpl;

	Backgroundverification backgroundverification1 = Backgroundverification.builder().id(1).address("westhickory")
			.email("shaline@gmail.com").firstname("shaline").lastname("Kocherla").app_bgc_status(null)
			.app_lease_status(null).ownerSign(null).propertyId(22).dob("12-10-1997").phonenumber("67999").ssn("immhhm")
			.initials("kocherla").build();

	public void compute(String e, String f, String l, String ph, String ad1, String status, int c) {
		Backgroundverification backgroundverification = Backgroundverification.builder().email(e).firstname(f)
				.lastname(l).app_bgc_status("accept").ownerSign("accept").propertyId(c).phonenumber(ph).address(ad1)
				.build();
		StringList.add(backgroundverification);
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		compute("abc@gmail.com", "firstname1", "lastname1", "phon1", "add1", null, 22);
		compute("bcd@gmail.com", "firstname2", "lastname2", "phon2", "add2", null, 22);
		compute("cef@gmail.com", "firstname3", "lastname3", "phon3", "add3", null, 22);
		compute("def@gmail.com", "firstname4", "lastname4", "phon4", "add4", null, 22);
		compute("efg@gmail.com", "firstname5", "lastname5", "phon5", "add5", null, 22);
		compute("fgh@gmail.com", "firstname6", "lastname6", "phon6", "add6", null, 22);
	}

	/*
	 * Description: testing if all applications are listed 
	 * input: -when clicks on view application button
	 *  output:checking number of applications displayed
	 */
	@Test
	public void testFindAllApplication() {
		when(backgroundVerificationAndLeaseAdminRepository.findAll()).thenReturn(StringList);
		List<Backgroundverification> StringList1 = backgroundVerificationAndLeaseAdmnServiceImpl.findAllApplications();

		assertEquals(6, StringList1.size());
	}

	/*
	 * Description: testing if background verification status is updated 
	 * input: user email, status that is to be updated
	 *  output: checking if background verification status is updated
	 */
	@Test

	public void testUpdateStatus() {
		String status = "accept";
		when(backgroundVerificationAndLeaseAdminRepository.findByEmail("shaline@gmail.com"))
				.thenReturn(backgroundverification1);
		when(backgroundVerificationAndLeaseAdminRepository.save(backgroundverification1))
				.thenReturn(backgroundverification1);
		Backgroundverification backgroundverification = backgroundVerificationAndLeaseAdmnServiceImpl
				.updateStatus("shaline@gmail.com", status);
		assertEquals("accept", backgroundverification.getApp_bgc_status());

	}

	/*
	 * Description: testing finding user by email
	 *  input: email id of user 
	 *  output:checking email id of customer who filled background verification form
	 */
	@Test
	public void testFindUserByEmail() {
		when(backgroundVerificationAndLeaseAdminRepository.findByEmail("abc@gmail.com")).thenReturn(StringList.get(0));
		Backgroundverification backgroundverification = backgroundVerificationAndLeaseAdmnServiceImpl
				.findByEmail("abc@gmail.com");
		assertEquals("abc@gmail.com", backgroundverification.getEmail());
	}

	/*
	 * Description: testing if all applications are seen by owner
	 *  input: property id
	 * output: number of applications with that property id
	 */
	@Test
	public void testFindAllApplicationbyownerTest() {
		int id = 22;
		when(backgroundVerificationAndLeaseAdminRepository.findByPropertyOwner(id)).thenReturn(StringList);
		List<Backgroundverification> StringList1 = backgroundVerificationAndLeaseAdmnServiceImpl
				.findAllApplicationsofOwner(22);
		assertEquals(6, StringList1.size());
	}

	/*
	 * Description: testing if owner sign is updated 
	 * input: user email and background verification status 
	 * output: checking the sign of owner
	 */
	@Test
	public void testUpdateOwnerTest() {
		String status = "accept";
		when(backgroundVerificationAndLeaseAdminRepository.findByEmail("shaline@gmail.com"))
				.thenReturn(backgroundverification1);
		when(backgroundVerificationAndLeaseAdminRepository.save(backgroundverification1))
				.thenReturn(backgroundverification1);
		Backgroundverification backgroundverification = backgroundVerificationAndLeaseAdmnServiceImpl
				.updateOwnerSign("shaline@gmail.com", status);
		assertEquals("accept", backgroundverification.getOwnerSign());
	}

}
