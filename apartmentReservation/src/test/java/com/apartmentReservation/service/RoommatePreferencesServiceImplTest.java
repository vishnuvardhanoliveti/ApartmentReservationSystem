//Description: testing the RoommatePreferences service methods 
package com.apartmentReservation.service;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.apartmentReservation.model.Backgroundverification;
import com.apartmentReservation.model.RoommatePreferences;
import com.apartmentReservation.repository.BackgroundVerificationAndLeaseAdminRepository;
import com.apartmentReservation.repository.RoommatePreferencesRepository;
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RoommatePreferencesServiceImplTest {
	@Mock
	RoommatePreferencesRepository roommatePreferencesRepository;
	@Mock
	List<RoommatePreferences> matchedPreferences;
	@Mock
	BackgroundVerificationAndLeaseAdminRepository backgroundVerificationAndLeaseAdminRepository;
	
	@InjectMocks
	RoommatePreferencesServiceImpl roommatePreferencesServiceimpl;
	
	//Creating a mocked instance of roommatePreferences object
	RoommatePreferences mockedroommatePreferences = RoommatePreferences.builder()
			 .id(1)
			 .userId(1)
			 .ageGroup("15-30")
			 .age(23)
			 .actualGender("male")
			 .vegan(true)
			 .gender("Male")
			 .drinking(false)
			 .smoking(false)
			 .pets(true)
			 .location("Avenue D")
			 .build();
	

	
	List<Backgroundverification> stringlist = new ArrayList<>();
	RoommatePreferences roommatePreferences = RoommatePreferences.builder().id(109).userId(62).userEmail("tarun@gmail.com").vegan(true).ageGroup("15-30").actualGender("Male").age(20).gender("Male")
			.drinking(true).smoking(false).pets(true).location("Trust Drive").build();
	//This method runs before every test
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		compute("a@gmail.com", "a", "b", "p", "234", "west", "tex", "us", "3000");
		compute("b@gmail.com", "b", "c", "pa", "2345", "westh", "texe", "usq", "30001");
		compute("c@gmail.com", "c", "d", "pas", "2346", "westi", "texr", "usw", "30002");
		compute("d@gmail.com", "d", "e", "pass", "2347", "westc", "text", "use", "30003");
		compute("e@gmail.com", "e", "f", "passw", "2348", "westo", "texy", "usr", "30004");
		compute("f@gmail.com", "f", "g", "passwo", "2349", "westr", "texu", "ust", "30005");
	}
	public void compute(String e, String f, String l, String p, String ph, String ad1, String c, String s, String z) {
		Backgroundverification backgroundverification = Backgroundverification.builder().email(e).firstname(f)
				.lastname(l).app_bgc_status("accept").ownerSign(null).propertyId(22).phonenumber(ph).address(ad1)
				.build();

		stringlist.add(backgroundverification);
	}
	
	
	/**
	 * Description:This method is used to test roommatePreferencesById service which returns roommatePrefernces object when customer id is given
	
	 * @Input:customer id
	 * @Output: This method verifies returns the RoommatePrefernces object
	 * @author: Vishnu Vardhan Oliveti
	 * Written on:November 2,2018
	 * Modified on: November 6, 2018
	 */
	
	@Test
	public void testfindRoommatePreferencesById() {
		 //mocking the roommatePreferencesRepository
		 when(roommatePreferencesRepository.findByUserId(1)).thenReturn(mockedroommatePreferences);
		 RoommatePreferences roommatePreferences=roommatePreferencesServiceimpl.findRoommatePreferencesById(1);
		 //Assert statement
		 assertEquals("Male", roommatePreferences.getGender());
	}
	
	
	/**
	 * Description:This method is used to test saveUserRoommatePreferences service which returns the saved roommatePrefernces object 
	
	 * @Input:roommatePrefernces object with some attributes set.
	 * @Output: This method  returns the roommatePrefernces object
	 * @author: Vishnu Vardhan Oliveti
	 * Written on:November 2,2018
	 * Modified on: November 6, 2018
	 */
	@Test
	public void testsaveUserRoommatePreferences() {
		 //mocking the roommatePreferencesRepository
		 when(roommatePreferencesRepository.save(mockedroommatePreferences)).thenReturn(mockedroommatePreferences);
		 
		 RoommatePreferences roommatePreferences = roommatePreferencesServiceimpl.saveUserRoommatePreferences(mockedroommatePreferences) ;
		 
		 //Assert statements
		 assertEquals("Avenue D",roommatePreferences.getLocation());
		 assertEquals("Male",roommatePreferences.getGender());
	}



}
