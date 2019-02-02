//Description: unit test for user contoller
package com.apartmentReservation.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.apartmentReservation.model.Properties;
import com.apartmentReservation.model.RatingsAndReviews;
import com.apartmentReservation.model.User;
import com.apartmentReservation.repository.UserRepository;
import com.apartmentReservation.service.BackgroundVerificationAndLeaseAdminService;
import com.apartmentReservation.service.BackgroundVerificationAndLeaseUserService;
import com.apartmentReservation.service.PropertyService;
import com.apartmentReservation.service.RatingsAndReviewsService;
import com.apartmentReservation.service.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerUnitTest {
	private MockMvc mockMvc;
	@MockBean
	private UserService userService;
	@MockBean
	private BackgroundVerificationAndLeaseUserService backgroundVerificationAndLeaseUserService;
	@MockBean
	private PropertyService propertyService;
	@MockBean
	private BackgroundVerificationAndLeaseAdminService backgroundVerificationAndLeaseAdminService;
	@MockBean
	private RatingsAndReviewsService ratingsAndReviewService;
	@Mock
	private UserRepository userRepository;
	@InjectMocks
	private UserController userController;
	@Mock
	View mockView;
	@Mock
	private MockHttpServletRequest request;
	@Mock
	private HttpSession session;
	Authentication auth = Mockito.mock(Authentication.class);
	SecurityContext securityContext = Mockito.mock(SecurityContext.class);
	List<Properties> allProperties = new ArrayList<Properties>();
	Map<String, Double> propertyToRatingMap = new TreeMap<String, Double>();
	List<RatingsAndReviews> ratingsAndReviewsForEachProperty = new ArrayList<RatingsAndReviews>();
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(userController).setSingleView(mockView).build();
		computeProperties("Oak Glen", false,1);
		computeProperties("Oak Glen", false,2);
		computeProperties("Oak Glen", true,3);
		computeProperties("Oak Glen", true,4);
		computeRatings("Oak Glen", "Dhoni MS", 3.5, "Hello World.");
		computeRatings("Oak Glen", "Anoop V", 4.0, "Test Comment.");
		propertyToRatingMap.put("Oak Glen", 3.75); //expected
	}
	public void computeProperties(String a, Boolean b, Integer c) {
		Properties properties = Properties.builder().propertyName(a).isFeatured(b).aptNumber(c).build();
		allProperties.add(properties);
	}
	public void computeRatings(String a, String b, Double c, String d) {
		RatingsAndReviews ratingsAndReviews = RatingsAndReviews.builder()
												.reviewPropertyName(a)
												.reviewUserName(b)
												.reviewRating(c)
												.reviewComment(d)
												.build();
		ratingsAndReviewsForEachProperty.add(ratingsAndReviews);
	}

	/*
	 * Description: testing if the registration of user is successful 
	 * input:-user
	 * output: checking model attribute msg
	 */
	@Test
	public void testRegisterSuccessfull() throws Exception {
		User mockUser = new User();
		mockUser = User.builder().email("virat@gmail.com").firstName("Virat").lastName("Kohli").password("sample")
				.phoneNumber("123-456-7678").addressLine1("University Manor").addressLine2("Stella Street")
				.city("Denton").state("TX").zip("76201").build();
		when(userService.saveUser(any(User.class))).thenReturn(mockUser);
		ModelAndView model = new ModelAndView();
		mockMvc.perform(post("/signup", mockUser)).andExpect(status().isOk())
				.andExpect(model().attribute("msg", equalTo("User has been registered successfully!"))).andReturn();
	}
	
	@Test
	public void testPropertiesRatings() {
		when(securityContext.getAuthentication()).thenReturn(auth);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userName")).thenReturn("Vishnu Vardhan Oliveti");
		when(session.getAttribute("userRole")).thenReturn("CUSTOMER");
		SecurityContextHolder.setContext(securityContext);
		when(auth.getName()).thenReturn("vishnu@gmail.com");
		when(propertyService.displayFeaturedProperties()).thenReturn(allProperties);
		for(Properties prop : allProperties) {
			when(ratingsAndReviewService.findRatingsAndReviewsByPropertyName(prop.getPropertyName())).thenReturn(ratingsAndReviewsForEachProperty);
		}
		
		ModelAndView model = userController.defaultAfterLogin(request);
		assertEquals(propertyToRatingMap, model.getModel().get("ratingByPropertyName"));
	}
}
