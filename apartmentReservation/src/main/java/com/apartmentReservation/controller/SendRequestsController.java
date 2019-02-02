/*This controller is used to handle the requests to send,receive and view Roommate requests */

package com.apartmentReservation.controller;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.apartmentReservation.model.Backgroundverification;
import com.apartmentReservation.model.Properties;
import com.apartmentReservation.model.Requests;
import com.apartmentReservation.model.Role;
import com.apartmentReservation.model.RoommatePreferences;
import com.apartmentReservation.model.User;
import com.apartmentReservation.repository.UserRepository;
import com.apartmentReservation.service.BackgroundVerificationAndLeaseAdminService;
import com.apartmentReservation.service.BackgroundVerificationAndLeaseUserService;
import com.apartmentReservation.service.PropertyService;
import com.apartmentReservation.service.RoommatePreferencesService;
import com.apartmentReservation.service.SendRequestsService;
import com.apartmentReservation.service.UserService;

@Controller
public class SendRequestsController {
 @Autowired
 SendRequestsService sendRequestsService;
 @Autowired
 private UserService userService;
 
 @Autowired
 private  PropertyService propertyService;
 @Autowired
 private BackgroundVerificationAndLeaseUserService backgroundVerificationAndLeaseUserService;
 
 @Autowired
	private UserRepository userRepository;
 @Autowired 
	private RoommatePreferencesService roommatePreferencesService;
 
 @Autowired
	private BackgroundVerificationAndLeaseAdminService backgroundVerificationAndLeaseAdminService;
	
 Map<String, String> result;
 
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
	 * Description: This method is used to display home page for Roommate request
	 * Input: request
	 * Output: This method returns model as output which contains the ViewName and other objects like userName,userRole etc.
	 
	 */
 @RequestMapping(value = { "/roommateRequest" }, method = RequestMethod.GET)
	public ModelAndView roommateRequestHome(HttpServletRequest request) {
		//Getting the userName,userRole,userEmail of the logged in user.
		HttpSession session = request.getSession();
		Object userEmail = session.getAttribute("userEmail");
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		
		//Getting objects for displaying options. 
		Object displayOptions = session.getAttribute("display_options");
		Object displayStatus = session.getAttribute("display_status");
		Object displaySignTheLease = session.getAttribute("display_signthelease");
		Object downloadPDF = session.getAttribute("downloadpdf");
		ModelAndView model = new ModelAndView();
		
		//Setting objects for user's details.
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		
		//Setting objects for display options. 
		model.addObject("display_options", displayOptions);
		model.addObject("display_status", displayStatus);
		model.addObject("display_signthelease", displaySignTheLease);
		model.addObject("downloadpdf", downloadPDF);
		model.setViewName("user/roommate_requests");
		return model;
	}
 
 /**
	 * Description: This method is used to send request
	 * Input: request, id, age, matchscore, searchtype
	 * Output: This method returns model as output which contains the ViewName and other objects like userName,userRole etc.
	 
	 */
	@RequestMapping(value= {"/requestForMoreDetails/{id}/{age}/{matchscore}/{searchtype}"}, method=RequestMethod.GET)
	public String sendRequest(@PathVariable String id,@PathVariable String age,@PathVariable String matchscore,@PathVariable String searchtype, HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		Object userName = session.getAttribute("userName");
		Object userEmail = session.getAttribute("userEmail");
		Object userRole = session.getAttribute("userRole");
		User userd=userRepository.findById(Integer.parseInt(id));
		User user=userRepository.findByEmail((String)userEmail);
		System.out.println(userName);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Requests a=new Requests();
		a.setUserEmail(auth.getName());
		a.setRequestedUserEmail(userd.getEmail());
		a.setRequestedUserGender(userd.getGender());
		a.setRequestedUserAge(age);
		a.setUserDecision("sent");
		a.setUserGender(user.getGender());
		a.setUserAge(Integer.toString(calculateAge(user.getDob())));
		float x=(Integer.parseInt(matchscore));
		x=(x/7)*100;
		a.setMatchedPercent(String.valueOf(x));
		sendRequestsService.saveRequest(a);
		
		if(searchtype.equalsIgnoreCase("new"))
		{
		return "redirect:/roommatePreferencesSearch";
	}
		else
		{
			return "redirect:/sharedProperties/search";
		}
	}
	
	/**
	 * Description: This method is used to display received requests
	 * Input: request
	 * Output: This method returns model as output which contains the ViewName and other objects like userName,userRole etc.
	
	 */
	
	@RequestMapping(value= {"/viewRequests"}, method=RequestMethod.GET)
	public ModelAndView viewRequests( HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		//Getting objects for displaying options. 
		Object displayOptions = session.getAttribute("display_options");
		Object displayStatus = session.getAttribute("display_status");
		Object displaySignTheLease = session.getAttribute("display_signthelease");
		Object downloadPDF = session.getAttribute("downloadpdf");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<Requests> viewrequest=sendRequestsService.getReceivedRequests(auth.getName());
		ModelAndView model = new ModelAndView();
		if(viewrequest.isEmpty())
		{
			model.addObject("requestsDisplay", "no");	
		}
		else
		{
			model.addObject("requestsDisplay", "yes");	
		}
		model.addObject("viewrequest", viewrequest);
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		
		//Setting objects for display options. 
		model.addObject("display_options", displayOptions);
		model.addObject("display_status", displayStatus);
		model.addObject("display_signthelease", displaySignTheLease);
		model.addObject("downloadpdf", downloadPDF);
		model.setViewName("user/view_requests");

		return model;
		
	}
	

	/**
	 * Description: This method is used to display more details of received requests
	 * Input: request
	 * Output: This method returns model as output which contains the ViewName and other objects like userName,userRole etc.
	
	 */
	@RequestMapping(value= {"/viewMoreDetails/{email}"}, method=RequestMethod.GET)
	public ModelAndView viewMoreDetals(@PathVariable String email,HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		//Getting objects for displaying options. 
		Object displayOptions = session.getAttribute("display_options");
		Object displayStatus = session.getAttribute("display_status");
		Object displaySignTheLease = session.getAttribute("display_signthelease");
		Object downloadPDF = session.getAttribute("downloadpdf");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User userd=userService.findUserByEmail(auth.getName());
		User userd1=userService.findUserByEmail(email);
		RoommatePreferences roommatePreferences = roommatePreferencesService.findRoommatePreferencesById(userd.getId());
		Map<Integer, List<Map<String, String>>> matchedResults = roommatePreferencesService.getMatchedPreferences(roommatePreferences,"newRoommateSearch");
		
		List<Requests> viewrequest=sendRequestsService.getReceivedRequests( auth.getName());
		Requests reqUnique=sendRequestsService.checkIfAlreadyRequestSent(email, auth.getName());
for(List<Map<String,String>> matchedResultsList : matchedResults.values()) {
			matchedResultsList.forEach(matchedResult->{
				if(matchedResult.get("userId").equals(userd1.getId())) {
 					result = matchedResult;
 					
				}
			});
		}
		ModelAndView model = new ModelAndView();
		model.addObject("requestedUserEmail", email);
		model.addObject("acceptoption", "yes");
		if(!reqUnique.getUserDecision().equalsIgnoreCase("sent"))
		{
			model.addObject("acceptoption", "no");
			model.addObject("acceptstatus", reqUnique.getUserDecision());
		}
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		model.addObject("userdetails", userd1);
		model.addObject("result", result);
		
		//Setting objects for display options. 
			model.addObject("display_options", displayOptions);
			model.addObject("display_status", displayStatus);
			model.addObject("display_signthelease", displaySignTheLease);
			model.addObject("downloadpdf", downloadPDF);
		model.setViewName("user/view_requests_more_details");

		return model;
		
	}
	

	/**
	 * Description: This method is used to display sent requests
	 * Input: request
	 * Output: This method returns model as output which contains the ViewName and other objects like userName,userRole etc.
	
	 */
	
	@RequestMapping(value= {"/viewSentRequests"}, method=RequestMethod.GET)
	public ModelAndView sentRequests( HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		//Getting objects for displaying options. 
		Object displayOptions = session.getAttribute("display_options");
		Object displayStatus = session.getAttribute("display_status");
		Object displaySignTheLease = session.getAttribute("display_signthelease");
		Object downloadPDF = session.getAttribute("downloadpdf");
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<Requests> viewsentrequest=sendRequestsService.getSentRequests( auth.getName());
		List<String> a=new ArrayList<String>();
		a.add("Yes");
		a.add("No");
		ModelAndView model = new ModelAndView();
		model.addObject("viewsentrequest", viewsentrequest);
		model.addObject("isleased", a);
		if(viewsentrequest.isEmpty())
		{
			model.addObject("requestsDisplay", "no");	
		}
		else
		{
			model.addObject("requestsDisplay", "yes");	
		}
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		//Setting objects for display options. 
		model.addObject("display_options", displayOptions);
		model.addObject("display_status", displayStatus);
		model.addObject("display_signthelease", displaySignTheLease);
		model.addObject("downloadpdf", downloadPDF);
		model.setViewName("user/sent_requests");

		return model;
		
	}
	

	/**
	 * Description: This method is used to display more details of sent requests
	 * Input: request, email
	 * Output: This method returns model as output which contains the ViewName and other objects like userName,userRole etc.
	
	 */
	
	@RequestMapping(value= {"/viewSentRequestMoreDetails/{email}"}, method=RequestMethod.GET)
	public ModelAndView viewSentRequestMoreDetails( HttpServletRequest request, @PathVariable String email) {
		
		HttpSession session = request.getSession();
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		
		//Getting objects for displaying options. 
		Object displayOptions = session.getAttribute("display_options");
		Object displayStatus = session.getAttribute("display_status");
		Object displaySignTheLease = session.getAttribute("display_signthelease");
		Object downloadPDF = session.getAttribute("downloadpdf");
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User userd=userService.findUserByEmail(auth.getName());
		User userd1=userService.findUserByEmail(email);
		
		RoommatePreferences roommatePreferences = roommatePreferencesService.findRoommatePreferencesById(userd.getId());
		Map<Integer, List<Map<String, String>>> matchedResults = roommatePreferencesService.getMatchedPreferences(roommatePreferences,"newRoommateSearch");
		
		List<Requests> viewrequest=sendRequestsService.getReceivedRequests( auth.getName());
for(List<Map<String,String>> matchedResultsList : matchedResults.values()) {
			matchedResultsList.forEach(matchedResult->{
				if(matchedResult.get("userId").equals(userd1.getId())) {
 					result = matchedResult;
 					
				}
			});
		}
		Backgroundverification backgroundappl =backgroundVerificationAndLeaseAdminService.findByEmail(email);
		Backgroundverification backgroundappl1 =backgroundVerificationAndLeaseAdminService.findByEmail(auth.getName());
		ModelAndView model = new ModelAndView();
		if(backgroundappl!=null && backgroundappl1==null)
		{
			
			model.addObject("isleaseduser","yes");
		}
		else
		{
			model.addObject("isleaseduser","no");
	}
		
		model.addObject("userName", userName);
		model.addObject("leasedUserEmail", email);
		model.addObject("userRole", userRole);
		model.addObject("userdetails", userd1);
		model.addObject("result", result);
		//Setting objects for display options. 
				model.addObject("display_options", displayOptions);
				model.addObject("display_status", displayStatus);
				model.addObject("display_signthelease", displaySignTheLease);
				model.addObject("downloadpdf", downloadPDF);
		model.setViewName("user/view_sent_request_more_details");

		return model;
		
	}
	

	/**
	 * Description: This method is used to update received requests status
	 * Input: request, email, status
	 * Output: This method returns model as output which contains the ViewName and other objects like userName,userRole etc.
	
	 */
	
	@RequestMapping(value= {"/updateRequestStatus/{email}/{status}"}, method=RequestMethod.GET)
	public String updateRequestStatus(@PathVariable String email, @PathVariable String status, HttpServletRequest request) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		sendRequestsService.updateRequestStatus(auth.getName(), email, status);
		
		return "redirect:/viewMoreDetails/"+email;
	}
	
	@RequestMapping(value= {"/cheack_if_leased/{email}"}, method=RequestMethod.GET)
	public String checkforlease(@PathVariable String email) {
		
		Backgroundverification backgroundappl =backgroundVerificationAndLeaseAdminService.findByEmail(email);
		if(backgroundappl!=null)
		{
			return "redirect:/submit_background_verification_application_leased/"+email;
		}
		else
		{
		return "redirect:/viewMoreDetails/"+email;
	}
	}
	 

	/**
	 * Description:This method is used for displaying background verification application form to customer to start leasing process
	 * Input: request
	 * Output: This method returns model as output which contains the ViewName and other objects like userName,userRole etc.
	
	 */
	 @RequestMapping(value= {"/submit_background_verification_application_leased/{email}"}, method=RequestMethod.GET)
	 public ModelAndView backgv(@PathVariable String email, HttpServletRequest request) {
	  ModelAndView model = new ModelAndView();
	  model.addObject("otherEmail",email);
	  HttpSession session = request.getSession();
		 Object userdetails= session.getAttribute("userEmail");
		 Object userName = session.getAttribute("userName");
		 Object userRole = session.getAttribute("userRole");
		 Object displayOptions = session.getAttribute("display_options");
		 Object displayStatus = session.getAttribute("display_status");
			Object displaySignTheLease = session.getAttribute("display_signthelease");
			Object downloadPDF = session.getAttribute("downloadpdf");
		 User user = userService.findUserByEmail((String) userdetails);
		 System.out.println(user.getFirstName());
		 Backgroundverification backgroundverification = new Backgroundverification();
	  model.addObject("userforDetails",user);
	  model.addObject("userName", userName);
	  model.addObject("userRole", userRole);
	  model.addObject("display_options", displayOptions);
	  model.addObject("display_status", displayStatus);
		model.addObject("display_signthelease", displaySignTheLease);
		model.addObject("downloadpdf", downloadPDF);
	  model.addObject("backgv", backgroundverification);

	  model.setViewName("user/submit_background_verification_application_leased");
	  
	  return model;
	 }
	 
	 
	 /**
		 * Description:This method is used for saving the customer responses to background verification application to database
		 * Input: request, backgroundverification, email
		 * Output: This method returns model as output which contains the ViewName and other objects like userName,userRole etc.
		 
		 */
	 /*This method is used for saving the customer responses to background verification application to database*/
	 @RequestMapping(value= {"/submit_background_verification_application_leased/{email}"}, method=RequestMethod.POST)
	 public String backgroundverification(@Valid Backgroundverification backgroundverification, BindingResult bindingResult,@PathVariable String email, HttpServletRequest request) {
	  ModelAndView model = new ModelAndView();
	  HttpSession session = request.getSession();
		 Object userdetails= session.getAttribute("userEmail");

		 User user = userService.findUserByEmail((String) userdetails);
		 
		 Backgroundverification backgroundappl =backgroundVerificationAndLeaseAdminService.findByEmail(email);
		 
	  
	  backgroundverification.setFirstname_rm1(backgroundappl.getFirstname_rm1());
	  backgroundverification.setLastname_rm1(backgroundappl.getLastname_rm1());
	  backgroundverification.setEmailid_rm1(backgroundappl.getEmailid_rm1());
	  backgroundverification.setFirstname_rm2(backgroundappl.getFirstname());
	  backgroundverification.setLastname_rm2(backgroundappl.getLastname());
	  backgroundverification.setEmailid_rm2(backgroundappl.getEmail());
	  backgroundverification.setPropertyId(backgroundappl.getPropertyId());
	  backgroundverification.setApp_bgc_status("Under Review");
	  backgroundverification.setDob(user.getDob());
	  String otherroommates=backgroundappl.getOtherroommates();
	  if(otherroommates.equals("No")){
		  backgroundverification.setOtherroommates("1");
	  }
	  else if(otherroommates.equals("1")){
		  backgroundverification.setOtherroommates("2");
	  }
	  backgroundverification.setEmail(user.getEmail());
	  backgroundverification.setFirstname(user.getFirstName());
	  backgroundverification.setLastname(user.getLastName());
	  backgroundverification.setAddress(user.getAddressLine1());
	  backgroundverification.setPhonenumber(user.getPhoneNumber());
	  Properties property = propertyService.findProperty(backgroundappl.getPropertyId());
	  backgroundverification.setPropertyOwner(property.getPropertyOwner());
	  Backgroundverification backExists = backgroundVerificationAndLeaseUserService.findUserByEmail(backgroundverification.getEmail());
	  
	  if(backExists != null) {
	   bindingResult.rejectValue("email", "error.user", "This email already exists!");
	  }
	  if(bindingResult.hasErrors()) {
	   model.setViewName("user/submit_background_verification_application");
	   
	  } else {
	   Backgroundverification userApplication=backgroundVerificationAndLeaseUserService.saveUser(backgroundverification);
	  }
	  return "redirect:/home";
	 }
	 
	 @RequestMapping(value= {"/backgroundverification_form_update"}, method=RequestMethod.GET)
	 public String backgroundverificationformupdate(HttpServletRequest request) {
	
	  HttpSession session = request.getSession();
		 Object userdetails= session.getAttribute("userEmail");

		 String emailrm1=null;
		 String firstnamerm1=null; 
		 String lastnamerm1=null;
		 String otherroomates=null;
		 String emailrm2=null;
		 String firstnamerm2=null;
		 String lastnamerm2 =null;
		 
		 User user = userService.findUserByEmail((String) userdetails);
		 
		 Backgroundverification backgroundappl =backgroundVerificationAndLeaseAdminService.findByEmail((String) userdetails);
		
		String otherroommates=backgroundappl.getOtherroommates();
		 Backgroundverification backgroundapplone =null;
		 Backgroundverification backgroundappltwo =null;
		  if(otherroommates.equals("1")){
			  backgroundapplone =backgroundVerificationAndLeaseAdminService.findByEmail(backgroundappl.getEmailid_rm1());
			  if(backgroundapplone.getEmailid_rm1()=="null")
			  {
				  emailrm1=user.getEmail();
				  firstnamerm1=user.getFirstName();
				  lastnamerm1=user.getLastName();
				  otherroomates="1";
			  backgroundVerificationAndLeaseUserService.updateUser(emailrm1,  firstnamerm1,  lastnamerm1,  otherroomates,  emailrm2,  firstnamerm2,  lastnamerm2 , backgroundapplone.getEmail());
				 
			  }
		  }
		  else if(otherroommates.equals("2")){
			  
			  backgroundapplone =backgroundVerificationAndLeaseAdminService.findByEmail(backgroundappl.getEmailid_rm1());
			  if(backgroundapplone.getEmailid_rm1()!="null")
			  {
				  emailrm2=user.getEmail();
				  firstnamerm2=user.getFirstName();
				  lastnamerm2=user.getLastName();
				emailrm1=backgroundapplone.getEmailid_rm1();
				firstnamerm1=backgroundapplone.getFirstname_rm1();
				lastnamerm1=backgroundapplone.getLastname_rm1();
				 otherroomates="2";
			  backgroundVerificationAndLeaseUserService.updateUser(emailrm1,  firstnamerm1,  lastnamerm1,  otherroomates,  emailrm2,  firstnamerm2,  lastnamerm2 , backgroundapplone.getEmail());
			backgroundappltwo =backgroundVerificationAndLeaseAdminService.findByEmail(backgroundappl.getEmailid_rm2());
			emailrm1=backgroundappltwo.getEmailid_rm1();
			firstnamerm1=backgroundappltwo.getFirstname_rm1();
			lastnamerm1=backgroundappltwo.getLastname_rm1();
			
			  backgroundVerificationAndLeaseUserService.updateUser(emailrm1,  firstnamerm1,  lastnamerm1,  otherroomates,  emailrm2,  firstnamerm2,  lastnamerm2 , backgroundappltwo.getEmail());
			 
			  backgroundVerificationAndLeaseUserService.saveUser(backgroundappltwo);
			  }
		  }
	
	  return "redirect:/home";
	 }
}
