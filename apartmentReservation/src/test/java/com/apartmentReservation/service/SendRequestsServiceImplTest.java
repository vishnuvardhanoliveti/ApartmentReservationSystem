//Description: test various methods in SendRequestsServiceImpl method
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
import com.apartmentReservation.model.Requests;
import com.apartmentReservation.model.User;
import com.apartmentReservation.model.UserRole;
import com.apartmentReservation.repository.BackgroundVerificationAndLeaseUserRepository;
import com.apartmentReservation.repository.CustomRepository;
import com.apartmentReservation.repository.RequestsRepository;
import com.apartmentReservation.repository.RoleRepository;
import com.apartmentReservation.repository.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class SendRequestsServiceImplTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private RequestsRepository requestsRepository;
	@Mock
	private Requests  request1;
	
	List<User> Stringlist=new ArrayList<>();
	List<Requests> Stringlist2=new ArrayList<>();
	@InjectMocks
	private SendRequestsServiceImpl sendRequestsServiceImpl;
	//Creating a mocked instance of user object.
	public void compute(String email,String firstname,String password, String phonenumber) {
		User mockUser = User.builder().email(email).firstName(firstname).password(password)
				.phoneNumber(phonenumber).build();
		Stringlist.add(mockUser);
	}
	//Creating a mocked instance of requests object.
	public void compute2(String email,String percent) {
	Requests request=Requests.builder().userEmail(email).matchedPercent(percent).build();
	Stringlist2.add(request);
	}
	//runs before every test
	@Before
	public void setup() {

		MockitoAnnotations.initMocks(this);
		compute("alex@gmail.com","alex","password","098765");
		compute("new@gmail.com","new","password1","12098765");
		compute("tarun@gmail.com","tarun","password2","234098765");
		compute2("tarunya@gmail.com","69");
		compute2("shikhara@gmail.com","90");
		
	}
	 /**
	 * Description:This method is used to test findAll method.		
	 * @Output: This method verifies expected size of output
	 * @author: Kocherla Shaline
	 * Written on:November 23,2018
	 */
	@Test
	public void testfindAll()
	{
		when(userRepository.findAll()).thenReturn(Stringlist);
		List<User> output =sendRequestsServiceImpl.findAll();
		//assert statments
		assertEquals(Stringlist,output);
		assertEquals(3,output.size());
		}
	 /**
		 * Description:This method is used to test GetReceivedRequests method.		
		 * @Output: This method verifies expected size of output
		 * @author: Kocherla Shaline
		 * Written on:November 23,2018
		 */
	@Test
	public void testGetReceivedRequests()
	{
		String email="tarun@gmail.com";
		when(requestsRepository.findByRequestedUserEmail(email)).thenReturn(Stringlist2);
		List<Requests> output =sendRequestsServiceImpl.getReceivedRequests(email);
		//assert statements
		assertEquals(Stringlist2,output);
		assertEquals(2,output.size());
		}
	 /**
	 * Description:This method is used to test GetSentRequests method.		
	 * @Output: This method verifies expected size of output
	 * @author: Kocherla Shaline
	 * Written on:November 23,2018
	 */
	@Test
	public void testGetSentRequests()
	{
		String email="tarun@gmail.com";
		when(requestsRepository.findByUserEmail(email)).thenReturn(Stringlist2);
		List<Requests> output =sendRequestsServiceImpl.getSentRequests(email);
		//assert statements
		assertEquals(Stringlist2,output);
		assertEquals(2,output.size());
		}
	/**
	 * Description:This method is used to test CheckIfAlreadyRequestSent method.		
	 * @Output: This method verifies expected output
	 * @author: Kocherla Shaline
	 * Written on:November 23,2018
	 */
	@Test
	public void testCheckIfAlreadyRequestSent()
	{
		String userEmail="virat@gmail.com";
		String requestedEmail="tarun@gmail.com";
		when(requestsRepository.find(userEmail, requestedEmail)).thenReturn(request1);
		Requests output =sendRequestsServiceImpl.checkIfAlreadyRequestSent(userEmail, requestedEmail);
		//assert statements
		assertEquals(request1,output);
	}
}
