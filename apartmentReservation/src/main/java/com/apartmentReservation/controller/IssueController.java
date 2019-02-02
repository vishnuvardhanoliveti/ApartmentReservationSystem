/*This controller handles all Service Issues functionality of the user. It is used for submitting and checking the progress of the service issues*/
package com.apartmentReservation.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.apartmentReservation.model.Backgroundverification;
import com.apartmentReservation.model.Issue;
import com.apartmentReservation.model.Properties;
import com.apartmentReservation.model.User;
import com.apartmentReservation.service.BackgroundVerificationAndLeaseAdminService;
import com.apartmentReservation.service.BackgroundVerificationAndLeaseUserService;
import com.apartmentReservation.service.IssueUserService;
import com.apartmentReservation.service.PropertyService;
import com.apartmentReservation.service.UserService;


@Controller
public class IssueController {

	@Autowired
	private BackgroundVerificationAndLeaseUserService backgroundVerificationAndLeaseUserService;
	 
	@Autowired
	private IssueUserService issueUserService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PropertyService propertyService;
	
	@Autowired
	private BackgroundVerificationAndLeaseAdminService backgroundVerificationAndLeaseAdminService;
	
	/**
	 * Description: This method is used to display home page for Service Issue Portal
	 * Input: request
	 * Output: This method returns model as output which contains the ViewName and other objects like userName,userRole etc.
	
	 */
	@RequestMapping(value = { "/serviceIssuePortalHome" }, method = RequestMethod.GET)
	public ModelAndView issuehome(HttpServletRequest request) {
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
		Backgroundverification applications = backgroundVerificationAndLeaseAdminService.findByEmail((String) userEmail);
		ModelAndView model = new ModelAndView();
		if(applications!=null) {
			if(applications.getApp_lease_status()!=null){
			model.addObject("display_serviceProtal", "yes");
		} else{
			model.addObject("display_serviceProtal", "no");}
		}
		else{
			model.addObject("display_serviceProtal", "no");
		}
		
		//Setting objects for user's details.
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		
		//Setting objects for display options. 
		model.addObject("display_options", displayOptions);
		model.addObject("display_status", displayStatus);
		model.addObject("display_signthelease", displaySignTheLease);
		model.addObject("downloadpdf", downloadPDF);
		model.setViewName("user/service_issue_portal_home");
		return model;
	}
	
	/**
	 * Description: This method is used for viewing service issue requests and their status by user.
	 * Input: request
	 * Output: This method returns model as output which contains the ViewName and other objects like userName,userRole etc.
	 
	 */
	@RequestMapping(value = { "/serviceIssuePortalOpenRequests" }, method = RequestMethod.GET)
	public ModelAndView openissue(HttpServletRequest request) {
		
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
		
		//Getting issues submitted by user based on user email id
		List<Issue> myissues= issueUserService.findByIssueUserEmail((String) userEmail);
		
		//Setting objects for display options. 
		ModelAndView model = new ModelAndView();
		model.addObject("myissues", myissues);
		
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		
		//Setting objects for user's details.
		model.addObject("display_options", displayOptions);
		model.addObject("display_status", displayStatus);
		model.addObject("display_signthelease", displaySignTheLease);
		model.addObject("downloadpdf", downloadPDF);
		model.setViewName("user/service_issue_portal_open_requests");
		return model;
	}
	
	/**
	 * Description: This method is used for creating new service issue requests user.
	 * Input: request
	 * Output: This method returns model as output which contains the ViewName and other objects like userName,userRole etc.
	
	 */
	@RequestMapping(value = { "/serviceIssuePortalCreateRequest" }, method = RequestMethod.GET)
	public ModelAndView submitissue(HttpServletRequest request) {
		
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
		model.setViewName("user/service_issue_portal_create_request");
		return model;
	}
	
	/**
	 * Description: This method is used for saving the new service issue request details from user to database.
	 * Input issue, bindingResult, request
	 * Output: This method redirects to serviceIssuePortalHome or issuehome method
	
	 */
	
	@RequestMapping(value = { "/serviceIssuePortalCreateRequest" }, method = RequestMethod.POST)
	public String submitissued(@Valid Issue issue, BindingResult bindingResult, HttpServletRequest request) {
		
		//Getting the userEmail of the logged in user.
		HttpSession session = request.getSession();
		Object userEmail = session.getAttribute("userEmail");
		
		//Getting lease details of the user
		Backgroundverification backapp = backgroundVerificationAndLeaseUserService.findUserByEmail((String) userEmail);
		String pattern = "MM-dd-yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());
		User user = userService.findUserByEmail((String)userEmail);
		issue.setIssueUserFirstName(user.getFirstName());
		issue.setIssueUserLastName(user.getLastName());
		issue.setIssueUserEmail(user.getEmail());
		
		//Getting property details of the user
		Properties prop=propertyService.findProperty(backapp.getPropertyId());
		issue.setIssueAddress(prop.getPropertyName()+", Apt "+prop.getAptNumber()+", "+prop.getPropertyAddress());
		issue.setIssuePhoneNumber(user.getPhoneNumber());
		issue.setIssueSubmittedDate(date);
		issue.setIssueStatus("Under Review");
		issue.setPropertyOwner(backapp.getPropertyOwner());
		issue.setIssueSolvedDate("-----");
		
		//Saving the issue details submitted by the user
		issueUserService.saveIssue(issue);
		return "redirect:/serviceIssuePortalOpenRequests/";
	}
}
