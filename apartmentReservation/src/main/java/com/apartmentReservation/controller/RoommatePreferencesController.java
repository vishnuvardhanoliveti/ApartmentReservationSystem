/*This controller is used to handle the requests of displaying the RoommatePreferences form and submitting the posted values
 * 
 * of the form*/

package com.apartmentReservation.controller;



import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.apartmentReservation.model.Properties;
import com.apartmentReservation.model.RoommatePreferences;
import com.apartmentReservation.model.User;
import com.apartmentReservation.service.PropertyService;
import com.apartmentReservation.service.RoommatePreferencesService;
import com.apartmentReservation.service.SharedApartmentService;
import com.apartmentReservation.service.UserService;
import com.google.common.collect.Multimap;

@Controller
public class RoommatePreferencesController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private PropertyService propertyService;
	@Autowired 
	private RoommatePreferencesService roommatePreferencesService;
	@Autowired
	private SharedApartmentService sharedApartmentService;
	
	List<Properties> allProperties;
	Set<String> propertyLocations = new TreeSet<String>();
	List<String> agePreferences = new ArrayList<String>();
	Map<String, String> result;
	Map<Integer, List<Map<String, String>>> matchedResults;
	
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
	
	/**
	 * Description:This method is used to display the roomamtePreferences form for the customers in which they can set their roommate preferences such as gender,ageGroup,location etc.
	 *  If the roommatePreference details for the logged in customer already exist in the database then they are pre-populated. 
	 * @Input:The input expected is the HttpServletRequest
	 * @return model: This method returns model as output which contains the ViewName and other objects like userName,userRole of the logged in user
	
	 */
	@RequestMapping(value = { "/roommatePreferences" }, method = RequestMethod.GET)
	public ModelAndView roommatePreferencesDisplayForm(HttpServletRequest request) {
		HttpSession session = request.getSession();
		
		//Getting the userName,userRole,userEmail of the logged in user.
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		Object userEmail =session.getAttribute("userEmail");
		Object displayOptions = session.getAttribute("display_options");
		Object displayStatus = session.getAttribute("display_status");
		Object displaySignTheLease = session.getAttribute("display_signthelease");
		Object downloadPDF = session.getAttribute("downloadpdf");
		
		String msg = null;
		getPropertyLocations();//getting the propertyLocations
		
		//Getting the details of the logged in user by using the email from session.
		User user=userService.findUserByEmail(userEmail.toString());
		getAgePreferences();
		ModelAndView model = new ModelAndView();
	
		//Getting the roommatePrefernces of the logged in user from database if they exist.
		RoommatePreferences roommatePreferences = roommatePreferencesService.findRoommatePreferencesById(user.getId()) ;
		if(roommatePreferences==null) {
		roommatePreferences = new RoommatePreferences() ;
			model.addObject("msg","Preferences are not set.");
		}else {
			model.addObject("msg","Preferences are already set.");
		}
		
		//Adding objects to the model for session management purpose
		
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		model.addObject("roommatePreferences", roommatePreferences);
		model.addObject("locations",propertyLocations);
		model.addObject("agePreferences", agePreferences);
		model.addObject("display_options", displayOptions);
		model.addObject("display_status", displayStatus);
		model.addObject("display_signthelease", displaySignTheLease);
		model.addObject("downloadpdf", downloadPDF);
		
		//Setting the viewName of the model to which the request is redirected.
		model.setViewName("user/roommatePreferences");
		
		
		return model;
	}
	
	
	/**
	 * Description:This method is used to post the roommatePrefernces form values  the customer enter  in order to  set his roommatePrefernce details such as gender,ageGroup,location etc..Then it updates those details in the database.	
	 * @Input:The input expected is the posted RoommatePreferences object, HttpServletRequest
	 * @return model: This method returns model as output which contains the ViewName and other objects like userName,userRole of the logged in user
	
	 */
	@RequestMapping(value = { "/roommatePreferences" }, method = RequestMethod.POST)
	public ModelAndView roommatePreferencesSubmitForm(@ModelAttribute(value = "roommatePreferences") RoommatePreferences roommatePreferences,HttpServletRequest request) {
		HttpSession session = request.getSession();
		//Getting the userName,userRole,userEmail of the logged in user.
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		Object userEmail =session.getAttribute("userEmail");
		Object displayOptions = session.getAttribute("display_options");
		Object displayStatus = session.getAttribute("display_status");
		Object displaySignTheLease = session.getAttribute("display_signthelease");
		Object downloadPDF = session.getAttribute("downloadpdf");
		//Getting the details of the logged in user by using the email from session.
		User person=userService.findUserByEmail(userEmail.toString());
		
		getPropertyLocations();
		getAgePreferences();
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
		ModelAndView model = new ModelAndView();
		
		//Adding objects to the model for session management purpose
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		model.addObject("userEmail",userEmail);
		model.addObject("display_options", displayOptions);
		model.addObject("display_status", displayStatus);
		model.addObject("display_signthelease", displaySignTheLease);
		model.addObject("downloadpdf", downloadPDF);
		model.addObject("roommatePreferences", roommatePreferences);
		model.addObject("msg", "Details updated successfully!");
		model.addObject("locations",propertyLocations);
		model.addObject("agePreferences", agePreferences);
		
		//Setting the viewName of the model to which the request is redirected.
		model.setViewName("user/roommatePreferences");
		
		return model;
	}
	
	
	/**
	 * Description:This method is used to display the roommate Search page with possible roommate matches if available..
	 * @Input:request
	 * @Output: the roommate search model.
	
	 */
	@RequestMapping(value = { "/roommatePreferencesSearch" }, method = RequestMethod.GET)
	public ModelAndView roommatePreferencesSearchDisplayForm(HttpServletRequest request) {
		HttpSession session = request.getSession();
		
		//Getting the userName,userRole,userEmail of the logged in user.
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		Object userEmail =session.getAttribute("userEmail");
		Object displayOptions = session.getAttribute("display_options");
		Object displayStatus = session.getAttribute("display_status");
		Object displaySignTheLease = session.getAttribute("display_signthelease");
		Object downloadPDF = session.getAttribute("downloadpdf");
	
		//Getting the details of the logged in user by using the email from session.
		User user=userService.findUserByEmail(userEmail.toString());

		//getting the propertyLocations from database and age-preferences which are a list set manually
		getPropertyLocations();	
		getAgePreferences();
		RoommatePreferences roommatePreferences = roommatePreferencesService.findRoommatePreferencesById(user.getId()) ;

		
		ModelAndView model = new ModelAndView();
		if(roommatePreferences != null){
			System.out.println("IN empty set");
			model.addObject("displayResults", true);
			
			//getting the matched results of roommates who are not in lease based on set preferences
			matchedResults= roommatePreferencesService.getMatchedPreferences(roommatePreferences,"newRoommateSearch");
		}else {
			model.addObject("displayResults", false);
		}
		
		
		if(roommatePreferences==null) {
		roommatePreferences = new RoommatePreferences() ;
			model.addObject("msg","Preferences are not set.");
		}else {
			model.addObject("msg","Preferences are set.");
		}
		
		
		

		//Adding objects to the model for session management purpose
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		model.addObject("userEmail", userEmail);
		model.addObject("display_options", displayOptions);
		model.addObject("display_status", displayStatus);
		model.addObject("display_signthelease", displaySignTheLease);
		model.addObject("downloadpdf", downloadPDF);
		model.addObject("searchtype","new");
		model.addObject("roommatePreferences", roommatePreferences);
		//model.addObject("resultsSize",matchedResults.size());
		model.addObject("matchedResults",matchedResults);
		model.addObject("locations",propertyLocations);
		model.addObject("agePreferences", agePreferences);
		
		//Setting the viewName of the model to which the request is redirected.
		model.setViewName("user/roommatePreferencesSearch");
		
		
		return model;
	}
	
	/**
	 * Description:This method is used to update roommate preferences and display the roommate Search page with possible roommate matches if available.
	 * @Input:roommatePreferences object,request
	 * @Output: redirect to the "GET" method of the roommate search page displaying the matches
	
	 */
	@RequestMapping(value = { "/roommatePreferencesSearch" }, method = RequestMethod.POST)
	public String roommatePreferencesSearchSubmitForm(@ModelAttribute(value = "roommatePreferences") RoommatePreferences roommatePreferences,HttpServletRequest request) {
		
		
	
		HttpSession session = request.getSession();
		//Getting the userName,userRole,userEmail of the logged in user.
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		Object userEmail =session.getAttribute("userEmail");
		Object displayOptions = session.getAttribute("display_options");
		Object displayStatus = session.getAttribute("display_status");
		Object displaySignTheLease = session.getAttribute("display_signthelease");
		Object downloadPDF = session.getAttribute("downloadpdf");
		
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
		ModelAndView model = new ModelAndView();
		
		//Adding objects to the model for session management purpose
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		model.addObject("userEmail", userEmail);
		model.addObject("display_options", displayOptions);
		model.addObject("display_status", displayStatus);
		model.addObject("display_signthelease", displaySignTheLease);
		model.addObject("downloadpdf", downloadPDF);
		model.addObject("roommatePreferences", roommatePreferences);
		model.addObject("msg", "Details updated successfully!");
		model.addObject("locations",propertyLocations);
		model.addObject("agePreferences", agePreferences);
		
		//Setting the viewName of the model to which the request is redirected.
		model.setViewName("user/roommatePreferencesSearch");
		
		return "redirect:/roommatePreferencesSearch";
	}
	
	/**
	 * Description:This method is used to display the roommate Search page with for a visiting customer.
	 * @Input:request
	 * @Output: the roommate search model.
	 
	 */
	@RequestMapping(value = { "/visitingCustomerRoommatePreferencesSearch" }, method = RequestMethod.GET)
	public ModelAndView visitngCustomerRoommateSearchDisplayForm(HttpServletRequest request) {
		HttpSession session = request.getSession();
		//Getting the userName,userRole,userEmail of the logged in user.
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		Object userEmail =session.getAttribute("userEmail");
		Object displayOptions = session.getAttribute("display_options");
		Object displayStatus = session.getAttribute("display_status");
		Object displaySignTheLease = session.getAttribute("display_signthelease");
		Object downloadPDF = session.getAttribute("downloadpdf");
		
		//getting the propertyLocations from database and age-preferences which are a list set manually	
		getPropertyLocations();
		getAgePreferences();
		
		ModelAndView model = new ModelAndView();
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		model.addObject("userEmail", userEmail);
		model.addObject("display_options", displayOptions);
		model.addObject("display_status", displayStatus);
		model.addObject("display_signthelease", displaySignTheLease);
		model.addObject("downloadpdf", downloadPDF);
		
		//Adding objects to the model for session management purpose
		RoommatePreferences roommatePreferences = new RoommatePreferences() ;

		model.addObject("roommatePreferences", roommatePreferences);
		model.addObject("locations",propertyLocations);
		model.addObject("agePreferences", agePreferences);
		
		//Setting the viewName of the model to which the request is redirected.
		model.setViewName("user/roommatePreferencesSearchVisitingCustomer");
		return model;
		
	}
	
	/**
	 * Description:This method is used to take the set roommate preferences and display the roommate Search page with possible roommate matches if available.
	 * @Input:oommate preferences from form,request
	 * @Output: sets the same model as "GET" method but with updated roommate search results.
	
	 */
	@RequestMapping(value = { "/visitingCustomerRoommatePreferencesSearch" }, method = RequestMethod.POST)
	public ModelAndView visitingCustomerRoommateSearchSubmitForm(@ModelAttribute(value = "roommatePreferences") RoommatePreferences roommatePreferences,HttpServletRequest request) {
		HttpSession session = request.getSession();
		//Getting the userName,userRole,userEmail of the logged in user.
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		Object userEmail =session.getAttribute("userEmail");
		Object displayOptions = session.getAttribute("display_options");
		Object displayStatus = session.getAttribute("display_status");
		Object displaySignTheLease = session.getAttribute("display_signthelease");
		Object downloadPDF = session.getAttribute("downloadpdf");
		
		//getting the propertyLocations from database and age-preferences which are a list set manually		
		getPropertyLocations();
		getAgePreferences();
		
		ModelAndView model = new ModelAndView();
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		model.addObject("userEmail", userEmail);
		model.addObject("display_options", displayOptions);
		model.addObject("display_status", displayStatus);
		model.addObject("display_signthelease", displaySignTheLease);
		model.addObject("downloadpdf", downloadPDF);
				
		//Getting the matched results from service
		Map<Integer, List<Map<String, String>>> matchedResults = roommatePreferencesService.getMatchedPreferences(roommatePreferences,"newRoommateSearch");
		System.out.println(matchedResults);
		
		//Adding objects to the model for session management purpose
		
		model.addObject("roommatePreferences", roommatePreferences);
		model.addObject("locations",propertyLocations);
		model.addObject("agePreferences", agePreferences);
		model.addObject("matchedResults",matchedResults);
		
		//Setting the viewName of the model to which the request is redirected.
		model.setViewName("user/roommatePreferencesSearchVisitingCustomer");
		
		return model;
	}
	
	/**
	 * Description: This method is used to build the roommate matching modal.
	 * @param id: id of the requester.
	 * @param searchType : search type of the query.
	 * @param request
	 * @param model
	 * @return displays the roommateSearchMatching modal.
	 
	 */
	@RequestMapping(value = { "/roommatePreferencesSearch/{id}/{searchType}" }, method = RequestMethod.GET)
	public String setDataInModal(@PathVariable("id") Integer id,@PathVariable("searchType") String searchType, HttpServletRequest request, ModelMap model) {
		HttpSession session = request.getSession();
		Object userEmail =session.getAttribute("userEmail");
		//Getting the details of the logged in user by using the email from session.

		if(userEmail!=null) {
			User user=userService.findUserByEmail(userEmail.toString());
			RoommatePreferences roommatePreferences = roommatePreferencesService.findRoommatePreferencesById(user.getId());
			Map<Integer, List<Map<String, String>>> matchedResults = roommatePreferencesService.getMatchedPreferences(roommatePreferences,searchType);
			//matchedResults = sharedApartmentService.addPropertyDetailsToResults(matchedResults);
			model.addAttribute("showProperty", false);
			if(searchType.equals("sharedPropertiesSearch")) {
				matchedResults = sharedApartmentService.addPropertyDetailsToResults(matchedResults);
				model.addAttribute("showProperty", true);
			}
			for(List<Map<String,String>> matchedResultsList : matchedResults.values()) {
				matchedResultsList.forEach(matchedResult->{
					if(matchedResult.get("userId").equals(id.toString())) {
	 					result = matchedResult;
					}
				});
			}
			
			model.addAttribute("roommatePreferences", roommatePreferences);
			model.addAttribute("result", result);
		}
		
		return "template :: roommatePreferenceModal";
	}
	
}
