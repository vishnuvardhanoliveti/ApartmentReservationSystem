//Description: Test various methods in SharedApartmentServiceImpl service
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
import com.apartmentReservation.model.SharedApartment;
import com.apartmentReservation.model.User;
import com.apartmentReservation.model.UserRole;
import com.apartmentReservation.repository.BackgroundVerificationAndLeaseUserRepository;
import com.apartmentReservation.repository.CustomRepository;
import com.apartmentReservation.repository.PropertyRepository;
import com.apartmentReservation.repository.RoleRepository;
import com.apartmentReservation.repository.SharedApartmentsRepository;
import com.apartmentReservation.repository.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)

public class SharedApartmentServiceTest {
	@Mock
	private SharedApartmentsRepository sharedApartmentsRepository;

	@Mock
	private PropertyRepository propertyRepository;
	@Mock
	private Map<Integer, List<Map<String, String>>> matchedResults;
	@InjectMocks
	private SharedApartmentServiceImpl sharedApartmentServiceImpl;
	//Creating a mocked instance of SharedApartment object
	SharedApartment newsharedApartment = SharedApartment.builder().userEmail("virat@gmail.com")
			.propertyId(1).id(1).build();
	//Creating a mocked instance of Properties object
	Properties property = Properties.builder().propertyId(1).aptNumber(1)
			.propertyBath(1).propertyBedrooms(1).propertyRent(2000)
			.propertyDeposit(1000).propertyPets(1).propertyOwner(1).propertyImagePath("src")
			.propertyName("park place").propertyLocation("west hivckory").propertyAddress("2001 west hickory")
			.build();
	//Runs before every test 
	@Before
	public void setup() {

		MockitoAnnotations.initMocks(this);
		

	}
	 /**
	 * Description:This method is used to test AddToSharedApartment method.		
	 * @Input:mocked newsharedApartment
	 * @Output: This method verifies expected output
	 * @author: Kocherla Shaline
	 * Written on:November 23,2018
	 */
@Test
public void testAddToSharedApartment()
{
when(sharedApartmentsRepository.save(newsharedApartment)).thenReturn(newsharedApartment);
SharedApartment output= sharedApartmentServiceImpl.addToSharedApartment(newsharedApartment);
//assert statements
assertEquals(newsharedApartment,output);
assertEquals(1,output.getId());
}
/**
	 * Description:This method is used to test AddPropertyDetailsToResults method.		
	 * @Input:mocked matchedResults
	 * @Output: This method verifies expected output
	 * @author: Kocherla Shaline
	 * Written on:November 23,2018
	 */

}


