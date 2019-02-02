//Description: This tests  several methods in the owner controller 
package com.apartmentReservation.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.any;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import com.apartmentReservation.model.Backgroundverification;
import com.apartmentReservation.model.Properties;
import com.apartmentReservation.model.RatingsAndReviews;
import com.apartmentReservation.model.User;
import com.apartmentReservation.repository.BackgroundVerificationAndLeaseAdminRepository;
import com.apartmentReservation.service.BackgroundVerificationAndLeaseAdminService;
import com.apartmentReservation.service.PropertyService;
import com.apartmentReservation.service.RatingsAndReviewsService;
import com.apartmentReservation.service.UserService;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class OwnerControllerTest {
	Authentication auth = Mockito.mock(Authentication.class);
	SecurityContext securityContext = Mockito.mock(SecurityContext.class);
	@Mock
	private BackgroundVerificationAndLeaseAdminService backgroundVerificationAndLeaseAdminService;
	@Mock
	private BackgroundVerificationAndLeaseAdminRepository backgroundVerificationAndLeaseAdminRepository;
	@Mock
	private HttpServletRequest request;
	@Mock
	private PropertyService propertyService;
	@Mock
	private User user;
	@Mock
	private UserService userService;
	@Mock
	private HttpSession session;
	@Mock
	private RatingsAndReviewsService ratingsAndReviewService;
	@InjectMocks
	private OwnerController ownerController;
	List<Properties> properties = new ArrayList<>();

	public void propertiesBuilder(int propertyId, int apt_number, int property_bath, int propery_bedrooms,
			int property_rent, int property_deposit, int property_utility, int property_pets, int property_owner,
			Boolean property_isfeatured, String property_type, String property_name, String property_location,
			String property_address) {
		Properties property = Properties.builder().propertyId(propertyId).aptNumber(apt_number)
				.propertyBath(property_bath).propertyBedrooms(propery_bedrooms).propertyRent(property_rent)
				.propertyDeposit(property_deposit).propertyUtility(property_utility).propertyPets(property_pets)
				.propertyOwner(property_owner).isFeatured(property_isfeatured).propertyType(property_type)
				.propertyName(property_name).propertyLocation(property_location).propertyAddress(property_address)
				.build();
		properties.add(property);
	}

	User owner = User.builder().email("kumar@gmail.com").id(3).firstName("Kumar").lastName("Reddy").build();

	public void backgroundverificationBuilder(String address, String app_bgc_status, String otherroommates) {
		Backgroundverification application = Backgroundverification.builder().address(address)
				.app_bgc_status(app_bgc_status).otherroommates(otherroommates)

				.build();
		applications.add(application);
	}

	List<Backgroundverification> applications = new ArrayList<>();
	List<RatingsAndReviews> ratingsAndReviewsForEachProperty = new ArrayList<RatingsAndReviews>();
	Map<String, Double> propertyToRatingMap =  new TreeMap<String, Double>();;
	

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		backgroundverificationBuilder("100 Avenue", "Under Review", "No");
		backgroundverificationBuilder("Ave G", "Under Review", "No");
		computeRatings("Oak Glen", "Dhoni MS", 3.5, "Hello World.");
		computeRatings("Oak Glen", "Anoop V", 4.0, "Test Comment.");
		propertyToRatingMap .put("Hope Apartments", 3.75);
		propertyToRatingMap.put("Oak Glen", 3.75);//expected
		propertyToRatingMap.put("Rayzor Creek", 3.75);//expected
		propertyToRatingMap.put("University Manor", 3.75);//expected
		
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
	 * Description: Test if the owners properties are listed after he is logged in
	 * input: HttpSession 
	 * output: checking the viewname of model, and the count of displayed properties
	 */
	@Test
	public void testForOwnerProperties() {
		when(securityContext.getAuthentication()).thenReturn(auth);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userName")).thenReturn("kumar reddy");

		when(session.getAttribute("userRole")).thenReturn("OWNER");
		SecurityContextHolder.setContext(securityContext);
		when(auth.getName()).thenReturn("kumar@gmail.com");
		when(userService.findUserByEmail("kumar@gmail.com")).thenReturn(owner);
		propertiesBuilder(6, 6, 2, 2, 1200, 100, 1, 1, 3, true, "Apartment", "Hope Apartments", "Trust Drive, Denton",
				"Trust Drive");
		propertiesBuilder(7, 7, 2, 2, 1200, 100, 1, 1, 3, true, "Apartment", "University Manor", "Ave G, Denton",
				"Ave G");
		propertiesBuilder(8, 8, 2, 3, 1500, 100, 1, 1, 3, true, "Apartment", "Oak Glen", "214 North Texas Blvd, Denton",
				" North Texas Blvd");
		propertiesBuilder(9, 9, 2, 3, 1500, 100, 1, 1, 3, true, "Duplex", "Rayzor Creek", "Bonnie Brie, Denton",
				"Bonnie Brie");
		when(propertyService.findPropertybyOwner(3)).thenReturn(properties);
		for(Properties prop : properties) {
			when(ratingsAndReviewService.findRatingsAndReviewsByPropertyName(prop.getPropertyName())).thenReturn(ratingsAndReviewsForEachProperty);
		}
		ModelAndView model = new ModelAndView();
		model = ownerController.viewProperties(request);
		assertEquals("owner/view_my_properties", model.getViewName());
		assertEquals(4, model.getModel().get("numberOfProperties"));
	}

	/*
	 * Description: Test to find all applications of users who applied for lease for
	 * properties of a particular owner input: HttpSession Output: checking number
	 * of applications displayed , viewname of the model
	 */
	@Test
	public void testForFindAllApplications() {
		when(securityContext.getAuthentication()).thenReturn(auth);
		SecurityContextHolder.setContext(securityContext);
		when(auth.getName()).thenReturn("kumar@gmail.com");
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userName")).thenReturn("kumar reddy");
		when(session.getAttribute("userRole")).thenReturn("OWNER");
		when(userService.findUserByEmail("kumar@gmail.com")).thenReturn(owner);
		when(backgroundVerificationAndLeaseAdminService.findAllApplicationsofOwner(3)).thenReturn(applications);
		ModelAndView model = new ModelAndView();
		model = ownerController.findAllapplications(request);
		assertEquals(2, model.getModel().get("numberOfApplications"));
		assertEquals("owner/view_all_applications", model.getViewName());
	}

	/*
	 * Description: Test to find the leasing application exists for a customer by
	 * his email input: email id of customer and HttpSession request output:
	 * checking model attribute display_appstatus and otherroomate
	 */
	@Test

	public void testApplicationForGivenEmail() throws Exception {
		Properties property=Properties.builder()
							.propertyId(1)
							.propertyName("University Manor")
							.propertyLocation("Stella Strret")
							.aptNumber(1)
				            .build();
		Backgroundverification application = Backgroundverification.builder().address("100 Avenue")
				.app_bgc_status("Under Review").otherroommates("No").propertyId(1)

				.build();

		Backgroundverification applicationAccepted = Backgroundverification.builder().address("100 Avenue")
				.app_bgc_status("accept").otherroommates("No").propertyId(1)

				.build();
		Backgroundverification applicationWithRoomMates = Backgroundverification.builder().address("100 Avenue")
				.app_bgc_status("accept").otherroommates("2").propertyId(1)

				.build();

		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userName")).thenReturn("kumar reddy");
		when(session.getAttribute("userRole")).thenReturn("OWNER");
		
		when(propertyService.findProperty(1)).thenReturn(property);
		when(backgroundVerificationAndLeaseAdminService.findByEmail("tarundevan@gmail.com")).thenReturn(application);
		when(backgroundVerificationAndLeaseAdminService.findByEmail("vishnu@gmail.com"))
				.thenReturn(applicationAccepted);
		when(backgroundVerificationAndLeaseAdminService.findByEmail("anoop@gmail.com"))
				.thenReturn(applicationWithRoomMates);
		ModelAndView model = new ModelAndView();
		model = ownerController.updateBackground("tarundevan@gmail.com", request);

		assertEquals("yes", model.getModel().get("display_appstatus"));
		assertEquals("no", model.getModel().get("otherroomate"));

		ModelAndView model2 = new ModelAndView();
		model2 = ownerController.updateBackground("vishnu@gmail.com", request);
		assertEquals("no", model2.getModel().get("display_appstatus"));
		assertEquals("no", model2.getModel().get("otherroomate"));

		ModelAndView model3 = new ModelAndView();
		model3 = ownerController.updateBackground("anoop@gmail.com", request);
		assertEquals("no", model3.getModel().get("display_appstatus"));
		assertEquals("2", model3.getModel().get("otherroomate"));

	}

	/*
	 * Description: Test if owner can sign by accepting or rejecting the application
	 * Input: email id, sign and Httpsession output: intended page to be viewed by
	 * owner
	 */
	@Test
	public void testOwnerSign() {
		when(securityContext.getAuthentication()).thenReturn(auth);
		SecurityContextHolder.setContext(securityContext);
		when(auth.getName()).thenReturn("kumar@gmail.com");
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userName")).thenReturn("kumar reddy");
		when(session.getAttribute("userRole")).thenReturn("OWNER");
		when(auth.getName()).thenReturn("kumar@gmail.com");
		when(userService.findUserByEmail("kumar@gmail.com")).thenReturn(owner);
		String path = ownerController.ownerSign("vishnu@gmail.com", "reject", request);
		assertEquals("redirect:/applicationsDetails/vishnu@gmail.com", path);

	}
	
	@Test
	public void testPropertiesRatings() {
		when(securityContext.getAuthentication()).thenReturn(auth);
		SecurityContextHolder.setContext(securityContext);
		when(auth.getName()).thenReturn("kumar@gmail.com");
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userName")).thenReturn("kumar reddy");
		when(session.getAttribute("userRole")).thenReturn("OWNER");
		when(auth.getName()).thenReturn("kumar@gmail.com");
		when(userService.findUserByEmail("kumar@gmail.com")).thenReturn(owner);
		propertiesBuilder(6, 6, 2, 2, 1200, 100, 1, 1, 3, true, "Apartment", "Hope Apartments", "Trust Drive, Denton",
				"Trust Drive");
		propertiesBuilder(7, 7, 2, 2, 1200, 100, 1, 1, 3, true, "Apartment", "University Manor", "Ave G, Denton",
				"Ave G");
		propertiesBuilder(8, 8, 2, 3, 1500, 100, 1, 1, 3, true, "Apartment", "Oak Glen", "214 North Texas Blvd, Denton",
				" North Texas Blvd");
		propertiesBuilder(9, 9, 2, 3, 1500, 100, 1, 1, 3, true, "Duplex", "Rayzor Creek", "Bonnie Brie, Denton",
				"Bonnie Brie");
		when(propertyService.findPropertybyOwner(3)).thenReturn(properties);
		for(Properties prop : properties) {
			when(ratingsAndReviewService.findRatingsAndReviewsByPropertyName(prop.getPropertyName())).thenReturn(ratingsAndReviewsForEachProperty);
		}
		ModelAndView model = new ModelAndView();
		model = ownerController.viewProperties(request);
		assertEquals("owner/view_my_properties", model.getViewName());
		assertEquals(propertyToRatingMap, model.getModel().get("ratingByPropertyName"));
	}
}
