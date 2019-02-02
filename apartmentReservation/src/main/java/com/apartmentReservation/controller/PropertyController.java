//This class handles all the requests and responses related to the Properties link on the USer Interface
package com.apartmentReservation.controller;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.apartmentReservation.model.Backgroundverification;
import com.apartmentReservation.model.Properties;
import com.apartmentReservation.model.RatingsAndReviews;
import com.apartmentReservation.model.RoommatePreferences;
import com.apartmentReservation.model.SharedApartment;
import com.apartmentReservation.model.User;
import com.apartmentReservation.service.BackgroundVerificationAndLeaseAdminService;
import com.apartmentReservation.service.BackgroundVerificationAndLeaseUserService;
import com.apartmentReservation.service.PropertyService;
import com.apartmentReservation.service.RatingsAndReviewsService;
import com.apartmentReservation.service.RoommatePreferencesService;
import com.apartmentReservation.service.SharedApartmentService;
import com.apartmentReservation.service.UserService;

@Controller

public class PropertyController {
	@Autowired
	private PropertyService propertyService;
	@Autowired
	private Properties property;
	@Autowired 
	private RoommatePreferencesService roommatePreferencesService;
	
	
	Set<String> propertyTypes = new TreeSet<String>();
	Set<String> propertyLocations = new TreeSet<String>();
	Set<Integer> propertyBedrooms = new TreeSet<Integer>();
	Set<Integer> propertyBaths = new TreeSet<Integer>();
	Set<Integer> propertyUtilities = new TreeSet<Integer>();
	Set<Integer> propertyRents = new TreeSet<Integer>();
	Set<Integer> propertyPets = new TreeSet<Integer>();
	Integer propertyListSize = 0;
	List<Properties> allProperties;
	Map<String,Double> propertyToRatingMap = new TreeMap<String,Double>();
	Map<String,List<RatingsAndReviews>> propertyToRatingAndReviewsMap = new TreeMap<String,List<RatingsAndReviews>>();
	List<String> agePreferences = new ArrayList<String>();
	Map<Integer, List<Map<String, String>>> matchedResults;
	@Autowired
	private UserService userService;
	@Autowired
	private BackgroundVerificationAndLeaseUserService backgroundVerificationAndLeaseUserService;

	@Autowired
	private BackgroundVerificationAndLeaseAdminService backgroundVerificationAndLeaseAdminService;
	
	@Autowired
	private RatingsAndReviewsService ratingsAndReviewsService;
	
	@Autowired
	private SharedApartmentService sharedApartmentService;
	
	public int calculateAge(String dob) {
		
		String[] result = dob.split("-");
		int year =Integer.parseInt(result[0]);
		int month =Integer.parseInt(result[1]);
		int day = Integer.parseInt(result[2]);
		LocalDate birthdate = LocalDate.of (year, month, day);      //Birth date
		LocalDate now = LocalDate.now();                        //Today's date
		 
		int age=Period.between(birthdate, now).getYears();
		return age;
	}
	
	/**
	 * Description:This method is used to get unique property locations from database.
	
	 * @Input:not applicable
	 * @Output: This method returns unique property locations from database as output
	
	 */
	public void getPropertyLocations() {

		allProperties = propertyService.findAllProperties();
		
		Iterator<Properties> iterator = allProperties.iterator();
		while (iterator.hasNext()) {
			Properties property = iterator.next();
			
			propertyLocations.add(property.getPropertyLocation());
	
		}

	}
	
	/**
	 * Description:This method is used to get different ageGroups by setting them with some static values.
	
	 * @Input:not applicable
	 * @Output: This method returns different ageGroups as output
	 
	 */
	public void getAgePreferences() {
		agePreferences.clear();
		agePreferences.addAll(Arrays.asList("20-30","30-40","40-50","50-70","70-100"));
	}

	// This method is used to the get the properties select preference form
	// populated such as rent options, location options etc.
	public void getPropertiesMenuPopulated() {

		allProperties = propertyService.findAllProperties();
		getRatingsForProperties(allProperties);
		propertyListSize = allProperties.size();
		Iterator<Properties> iterator = allProperties.iterator();
		while (iterator.hasNext()) {
			Properties property = iterator.next();
			propertyBedrooms.add(property.getPropertyBedrooms());
			propertyBaths.add(property.getPropertyBath());
			propertyLocations.add(property.getPropertyLocation());
			propertyTypes.add(property.getPropertyType());
			propertyRents.add(property.getPropertyRent());
			propertyUtilities.add(property.getPropertyUtility());
			propertyPets.add(property.getPropertyPets());
		}

	}
	/**
	 * This method fetches average rating of each property in the list of all properties based on the property name
	 * and stores them in a map for the view to render it.
	 * @param allPropertiesList
	 */
	public void getRatingsForProperties(List<Properties> allPropertiesList) {
		List<RatingsAndReviews> ratingsAndReviewsForEachProperty = new ArrayList<RatingsAndReviews>();
		for(Properties property : allPropertiesList) {
			ratingsAndReviewsForEachProperty = ratingsAndReviewsService.findRatingsAndReviewsByPropertyName(property.getPropertyName());
			
			if(ratingsAndReviewsForEachProperty.size()!=0) {
				propertyToRatingAndReviewsMap.put(property.getPropertyName(), ratingsAndReviewsForEachProperty);
				Double ratingsSum = 0.0;
				Double avgRating =0.0;
				for(RatingsAndReviews ratingsAndReviews : ratingsAndReviewsForEachProperty) {
					ratingsSum += ratingsAndReviews.getReviewRating();
				}
				avgRating = (double) (ratingsSum/ratingsAndReviewsForEachProperty.size());
				propertyToRatingMap.put(property.getPropertyName(), avgRating);
			}
		}
	}
	/**
	 * This method fetches an average rating of all units under a single propertyName.
	 * @param propertyName
	 * @return Double : The average rating for propertyName. 
	 */
	public Double getRatingsForSingleProperty(String propertyName) {
		List<RatingsAndReviews> ratingsAndReviewsForEachProperty = new ArrayList<RatingsAndReviews>();
		ratingsAndReviewsForEachProperty = ratingsAndReviewsService.findRatingsAndReviewsByPropertyName(propertyName);
		Double avgRating=0.0;
		if(ratingsAndReviewsForEachProperty.size()!=0) {
			Double ratingsSum = 0.0;
			for(RatingsAndReviews ratingsAndReviews : ratingsAndReviewsForEachProperty) {
				ratingsSum += ratingsAndReviews.getReviewRating();
			}
			avgRating = (double) (ratingsSum/ratingsAndReviewsForEachProperty.size());
			
		}
		return avgRating;
	}
	
	
	// This controller is called for the first time when user enters the properties
	// page to view properties
	@RequestMapping(value = { "/properties" }, method = RequestMethod.GET)
	public ModelAndView displayProperties(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		Object displayOptions = session.getAttribute("display_options");
		Object displayStatus = session.getAttribute("display_status");
		Object displaySignTheLease = session.getAttribute("display_signthelease");
		Object downloadPDF = session.getAttribute("downloadpdf");

		getPropertiesMenuPopulated();
		ModelAndView model = new ModelAndView();
		model.addObject("bedrooms", propertyBedrooms);
		model.addObject("baths", propertyBaths);
		model.addObject("locations", propertyLocations);
		model.addObject("types", propertyTypes);
		model.addObject("rents", propertyRents);
		model.addObject("utilities", propertyUtilities);
		model.addObject("propertiesSize", propertyListSize);
		model.addObject("pets", propertyPets);
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		model.addObject("display_options", displayOptions);
		model.addObject("display_status", displayStatus);
		model.addObject("display_signthelease", displaySignTheLease);
		model.addObject("downloadpdf", downloadPDF);
		model.addObject("ratingByPropertyName", propertyToRatingMap);
		model.setViewName("user/properties");
		model.addObject("prop", allProperties);
		 model.addObject("property", new Properties());
		return model;
	}

	// This controller is called when customer sets his apartment search preferences and
	// submits the page
	@RequestMapping(value = { "/properties" }, method = RequestMethod.POST)
	public ModelAndView findApts(@ModelAttribute(value = "property") Properties property, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		Object displayOptions = session.getAttribute("display_options");
		Object displayStatus = session.getAttribute("display_status");
		Object displaySignTheLease = session.getAttribute("display_signthelease");
		Object downloadPDF = session.getAttribute("downloadpdf");
		List<Properties> filteredProperties = propertyService.findPropertyByAptSearchPreferencesCriteria(
				property.getPropertyType(), property.getPropertyBedrooms(), property.getPropertyBath(),
				property.getPropertyLocation(), property.getPropertyRent(), property.getPropertyPets(),
				property.getPropertyUtility());

		ModelAndView model = new ModelAndView();

		model.addObject("bedrooms", propertyBedrooms);
		model.addObject("baths", propertyBaths);
		model.addObject("locations", propertyLocations);
		model.addObject("types", propertyTypes);
		model.addObject("rents", propertyRents);
		model.addObject("utilities", propertyUtilities);
		model.addObject("pets", propertyPets);
		model.setViewName("user/properties");
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		model.addObject("display_options", displayOptions);
		model.addObject("display_status", displayStatus);
		model.addObject("display_signthelease", displaySignTheLease);
		model.addObject("downloadpdf", downloadPDF);
		model.addObject("ratingByPropertyName", propertyToRatingMap);
		if (filteredProperties.size() > 0) {
			model.addObject("prop", filteredProperties);
			System.out.println("11058492 FILTERED: " + filteredProperties.size());
			model.addObject("filteredPropertiesSize", filteredProperties.size());
		} else
			model.addObject("msg", "No Apartments found with entered search preferences");
		return model;
	}
	
	/**
	 * This method is used to render the propertyDescription.html page. It populates all required objects
	 * required for page render.
	 * @param id
	 * @param request
	 * @return ModelAndView : renders a UI template setting all required objects for rendering.
	 */
	@RequestMapping(value = { "/propertyDescription/{id}" }, method = RequestMethod.GET)
	public ModelAndView propertyDescription(@PathVariable Integer id,HttpServletRequest request) {
		HttpSession session = request.getSession();
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		Object userEmail = session.getAttribute("userEmail");
		String allowRatings = "notAllowed"; //default action to allow ratings functionality.
		String hasAlreadyReviewed = "no";	//default action to check if a user has already reviewed.
		ModelAndView model = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		model.addObject("showRequestForRoommate", false);
		User user = userService.findUserByEmail(auth.getName());
		if (user != null) {
			Integer userid = user.getId();
			String role = userService.userRole(userid);
			Backgroundverification applications = backgroundVerificationAndLeaseAdminService
					.findByEmail(user.getEmail());
			model.addObject("userEmail", user.getEmail());
			
			if (role.equalsIgnoreCase("CUSTOMER")) {
				if (applications != null) {
					String pdfdownload=applications.getApp_lease_status();
					if(pdfdownload!=null)
					{
						String bgcUserEmail = applications.getEmail();
						Integer bgcPropertyId = applications.getPropertyId();
						//check if the logged in user has leased the respective property to allow rating.
						if(bgcUserEmail.equals(userEmail) && (bgcPropertyId==id) ) {
							allowRatings = "allowed";
						}
						model.addObject("downloadpdf", "yes");
						model.addObject("docname",applications.getLeasedocname());
						
						//check if user has already requested for a roommate.
						model.addObject("showRequestForRoommate", true);
						List<SharedApartment> sharedApartments = sharedApartmentService.findAllSharedProperties();
						for(SharedApartment sharedApartment : sharedApartments) {
							if((sharedApartment.getPropertyId()==id) && sharedApartment.getUserEmail().equals(userEmail)) {
								model.addObject("showRequestForRoommate", false);
							}
						}
						
					}
					model.addObject("display_status", applications.getApp_bgc_status());
					model.addObject("display_options", "yes");
					model.addObject("signapplicationemail", user.getEmail());
					String signinit=applications.getInitials();
					String sde = applications.getOwnerSign();
					System.out.println(sde);
					if (sde!=null && signinit==null) {
						model.addObject("display_signthelease", "yes");
					}
				} else {
					
					model.addObject("display_options", "no");
				}
			}
		}
		Properties property = propertyService.findProperty(id);
		String appdisplay=property.getLeased();
		if(appdisplay==null)
		{
			model.addObject("availability", "yes");
			//model.addObject("display_options", "no");
		}
		else
		{
			model.addObject("availability", "no");
			model.addObject("display_options", "yes");
		}
		model.setViewName("user/propertyDescription");
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		model.addObject("userEmail", userEmail);
		model.addObject("propertyDetails", property);
		
		//adding average rating of a single property
		model.addObject("ratingByPropertyName", getRatingsForSingleProperty(property.getPropertyName()));
		model.addObject("allowRatings", allowRatings);
		List<RatingsAndReviews> ratingsAndReviewsList = ratingsAndReviewsService.findRatingsAndReviewsByPropertyName(property.getPropertyName());
		if(allowRatings=="allowed") {
			//check if user has already rated an apartment they are leasing.
			for(RatingsAndReviews rating : ratingsAndReviewsList) {
				if(rating.getReviewUserName().equals(user.getFirstName() + " " + user.getLastName())) {
					hasAlreadyReviewed ="yes";
				}
			}
		}
		model.addObject("hasAlreadyReviewed", hasAlreadyReviewed);
		//adding ratings and reviews of a property.
		propertyToRatingAndReviewsMap.clear();
		propertyToRatingAndReviewsMap.put(property.getPropertyName(),ratingsAndReviewsList);
		model.addObject("ratingAndReviewsByPropertyName", propertyToRatingAndReviewsMap);
		model.addObject("newRatingAndReview", new RatingsAndReviews());
		return model;
	}
	/**
	 * This method retrieves user reviews on a property and stores them in the database.
	 * @param id
	 * @param ratingsAndReviews
	 * @param bindingResult
	 * @param request
	 * @return String: URL of the parent page.
	 */
	@RequestMapping(value = { "/propertyDescription/{id}" }, method = RequestMethod.POST)
	public String saveRatingsAndReview(@PathVariable Integer id, @Valid RatingsAndReviews ratingsAndReviews, BindingResult bindingResult , HttpServletRequest request) {
		User user = userService.findUserByEmail(ratingsAndReviews.getReviewUserName());
		ratingsAndReviews.setReviewUserName(user.getFirstName() + " " + user.getLastName());
		ratingsAndReviewsService.addNewReview(ratingsAndReviews);
		return "redirect:/propertyDescription/{id}";
	}
	
	/**
	 * Description:This method is used to display all shared properties.
	 * @Input:request
	 * @Output: Displays the html page with all shared properties
	 
	 */
	@RequestMapping(value = { "/sharedProperties" }, method = RequestMethod.GET)
	public ModelAndView sharedPropertiesDisplay(HttpServletRequest request){
		getPropertiesMenuPopulated();
		List<SharedApartment> allSharedProperties = sharedApartmentService.findAllSharedProperties();
		List<Properties> filteredProperties = new ArrayList<Properties>();
		for(Properties property : allProperties) {
			for(SharedApartment sharedApartment : allSharedProperties) {
				if(property.getPropertyId() == sharedApartment.getPropertyId()) {
					filteredProperties.add(property);
				}
			}
		}
		HttpSession session = request.getSession();
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		Object userEmail = session.getAttribute("userEmail");
		Object displayOptions = session.getAttribute("display_options");
		Object displayStatus = session.getAttribute("display_status");
		Object displaySignTheLease = session.getAttribute("display_signthelease");
		Object downloadPDF = session.getAttribute("downloadpdf");
		ModelAndView model = new ModelAndView();
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		model.addObject("userEmail", userEmail);
		model.addObject("display_options", displayOptions);
		model.addObject("display_status", displayStatus);
		model.addObject("display_signthelease", displaySignTheLease);
		model.addObject("downloadpdf", downloadPDF);
		model.addObject("ratingByPropertyName", propertyToRatingMap);
		model.setViewName("user/sharedProperties");
		model.addObject("prop", filteredProperties);
		return model;
	}

	/**
	 * Description:This method is used to display the search results for shared properties with matching roommate preferences.
	 * @Input:request
	 * @Output: sets the shared property search html page
	
	 */
	@RequestMapping(value = { "/sharedProperties/search" }, method = RequestMethod.GET)
	public ModelAndView sharedPropertiesSearch( HttpServletRequest request){
		HttpSession session = request.getSession();
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		Object userEmail = session.getAttribute("userEmail");
		Object displayOptions = session.getAttribute("display_options");
		Object displayStatus = session.getAttribute("display_status");
		Object displaySignTheLease = session.getAttribute("display_signthelease");
		Object downloadPDF = session.getAttribute("downloadpdf");
		ModelAndView model = new ModelAndView();
		if(userEmail == null) {
			model.addObject("userLoggedIn", false);
			
			//getting the propertyLocations from database and age-preferences which are a list set manually		
			getPropertyLocations();
			getAgePreferences();
			RoommatePreferences roommatePreferences = new RoommatePreferences() ;
			model.addObject("roommatePreferences", roommatePreferences);
			model.addObject("locations",propertyLocations);
			model.addObject("agePreferences", agePreferences);
			//Setting the viewName of the model to which the request is redirected.
			model.setViewName("user/sharedPropertiesSearchVisitingCustomer");
			return model;
		}else {
			model.addObject("userLoggedIn", true);
			User user=userService.findUserByEmail(userEmail.toString());
			getPropertyLocations();//getting the propertyLocations	
			getAgePreferences();
			RoommatePreferences roommatePreferences = roommatePreferencesService.findRoommatePreferencesById(user.getId()) ;
			
			if(roommatePreferences==null) {
				roommatePreferences = new RoommatePreferences() ;
					model.addObject("msg","Preferences are not set.");
				}else {
					model.addObject("msg","Preferences are set.");
					matchedResults = roommatePreferencesService.getMatchedPreferences(roommatePreferences,"sharedPropertiesSearch");
					matchedResults = sharedApartmentService.addPropertyDetailsToResults(matchedResults);
					model.addObject("matchedResults",matchedResults);
					System.out.println(matchedResults);
					System.out.println(matchedResults.size());	
				}	
			model.setViewName("user/sharedPropertiesSearch");
			model.addObject("roommatePreferences", roommatePreferences);		
			model.addObject("locations",propertyLocations);
			model.addObject("agePreferences", agePreferences);
		}
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		model.addObject("userEmail", userEmail);
		model.addObject("display_options", displayOptions);
		model.addObject("display_status", displayStatus);
		model.addObject("display_signthelease", displaySignTheLease);
		model.addObject("downloadpdf", downloadPDF);
		return model;
	}
	
	
	/**
	 * Description:This method is used to updated the database with new roommate preferences and display the search results for shared properties with matching roommate preferences.
	 * @Input:roommatePreferences object,request
	 * @Output: sets the shared property search html page
	 
	 */
	@RequestMapping(value = { "/sharedProperties/search" }, method = RequestMethod.POST)
	public ModelAndView sharedPropertiesSearchResults(@ModelAttribute(value = "roommatePreferences") RoommatePreferences roommatePreferences,HttpServletRequest request) {
		HttpSession session = request.getSession();
		//Getting the userName,userRole,userEmail of the logged in user.
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		Object userEmail =session.getAttribute("userEmail");
		Object displayOptions = session.getAttribute("display_options");
		Object displayStatus = session.getAttribute("display_status");
		Object displaySignTheLease = session.getAttribute("display_signthelease");
		Object downloadPDF = session.getAttribute("downloadpdf");
		ModelAndView model = new ModelAndView();
		if(userEmail == null) {
			
			//getting the propertyLocations from database and age-preferences which are a list set manually		
			getPropertyLocations();
			getAgePreferences();
			
			//getting the matched results from service
			Map<Integer, List<Map<String, String>>> matchedResults = roommatePreferencesService.getMatchedPreferences(roommatePreferences,"sharedPropertiesSearch");
			System.out.println(matchedResults);
			System.out.println(matchedResults.size());
			
			//Adding objects to the model for session management purpose
			model.setViewName("user/sharedPropertiesSearchVisitingCustomer");
			
			model.addObject("roommatePreferences", roommatePreferences);
			matchedResults = sharedApartmentService.addPropertyDetailsToResults(matchedResults);
			model.addObject("matchedResults",matchedResults);
			model.addObject("locations",propertyLocations);
			model.addObject("agePreferences", agePreferences);
		}else {
			
			//Getting the details of the logged in user by using the email from session.
			User person=userService.findUserByEmail(userEmail.toString());
			
			
			roommatePreferences.setUserId(person.getId());
			
			int age = calculateAge(person.getDob());
			
			roommatePreferences.setAge(age);
			roommatePreferences.setActualGender(person.getGender());
			roommatePreferences.setUserEmail(userEmail.toString());
			//Getting the roommatePrefernces of the logged in user from database if they exist.
			RoommatePreferences setroommatePreferences=roommatePreferencesService.findRoommatePreferencesById(person.getId()) ;
			if(setroommatePreferences!=null) {
				roommatePreferences.setId(setroommatePreferences.getId());
				
			}
			//updating the roommatePrefernces of the customer in the database.
			roommatePreferences= roommatePreferencesService.saveUserRoommatePreferences(roommatePreferences);
			if(roommatePreferences==null) {
				roommatePreferences = new RoommatePreferences() ;
					model.addObject("msg","Preferences are not set.");
				}else {
					model.addObject("msg","Preferences are set.");
					matchedResults = roommatePreferencesService.getMatchedPreferences(roommatePreferences,"sharedPropertiesSearch");
					matchedResults = sharedApartmentService.addPropertyDetailsToResults(matchedResults);
					model.addObject("matchedResults",matchedResults);
					System.out.println(matchedResults);
					System.out.println(matchedResults.size());
					
				}
			model.addObject("roommatePreferences", roommatePreferences);
			model.addObject("msg", "Details updated successfully!");
			model.addObject("locations",propertyLocations);
			model.addObject("agePreferences", agePreferences);
			
			//Setting the viewName of the model to which the request is redirected.
			model.setViewName("user/sharedPropertiesSearch");
		}
		model.addObject("userName", userName);
		model.addObject("searchtype","shared");
		model.addObject("userRole", userRole);
		model.addObject("userEmail", userEmail);
		model.addObject("display_options", displayOptions);
		model.addObject("display_status", displayStatus);
		model.addObject("display_signthelease", displaySignTheLease);
		model.addObject("downloadpdf", downloadPDF);
		return model;
		
	}
	
	/**
	 * Description:This method adds a property to shared apartments table when a customer requests it.
	 * @Input:property id,request
	 * @Output: add the id as a shared property and redirect to property description page
	 
	 */
	@RequestMapping(value = { "/addToSharedApartments/{id}" }, method = RequestMethod.GET)
	public String addToSharedProperties(@PathVariable Integer id,  HttpServletRequest request) {
		HttpSession session = request.getSession();
		Object userEmail = session.getAttribute("userEmail");
		SharedApartment sharedApartment= SharedApartment.builder().propertyId(id).userEmail(userEmail.toString()).build();
		sharedApartmentService.addToSharedApartment(sharedApartment);
		return "redirect:/propertyDescription/{id}";
	}
	

}
