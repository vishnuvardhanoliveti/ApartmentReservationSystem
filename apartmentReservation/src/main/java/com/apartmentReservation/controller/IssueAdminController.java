/*This controller handles all Service Issues functionality of the admin. It is used for checking the submitted issues and change their status by admin*/
package com.apartmentReservation.controller;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.apartmentReservation.model.Issue;
import com.apartmentReservation.model.User;
import com.apartmentReservation.repository.UserRepository;
import com.apartmentReservation.service.IssueAdminService;
import com.apartmentReservation.service.UserService;

@RestController
public class IssueAdminController {
	@Autowired
	private IssueAdminService issueAdminService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * Description: This method is used to display all Service Issues Portal to admin
	 * Input: request
	 * Output: This method returns model as output which contains the ViewName and other objects like userName,userRole etc.
	
	 */
	@RequestMapping(value= {"/viewAllIssues"}, method=RequestMethod.GET)
	public ModelAndView findAllapplications(HttpServletRequest request) {
		//Getting the userName,userRole of the logged in user.
		HttpSession session = request.getSession();
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		
		//Getting all issues applications 
		List<Issue> applications = issueAdminService.findAllApplications();
		
		//Setting objects for user's details.
		ModelAndView model = new ModelAndView();
		model.addObject("issueapplications", applications);
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		model.setViewName("admin/view_all_issues");
		return model;
	}
	
	/**
	 * Description: This method is used to display all Service Issues Portal to admin
	 * Input: id(issue id), request
	 * Output: This method returns model as output which contains the ViewName and other objects like userName,userRole etc.
	
	 */
	@RequestMapping(value= {"/issueMoreDetails/{id}"}, method=RequestMethod.GET)
	public ModelAndView issuestatusupdate(@PathVariable int id, HttpServletRequest request) {
		
		//Getting the userName,userRole of the logged in user.
		HttpSession session = request.getSession();
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		ModelAndView model = new ModelAndView();
		
		//Getting issue details by ID.
		Issue application = issueAdminService.findById(id);
		String statusapp=application.getIssueStatus();
		User user=userRepository.findById(application.getPropertyOwner());
		
		//Setting Status buttons
		if(statusapp.equals("Under Review"))
		 {
			System.out.println("yes");
			model.addObject("display_Issuestatus","Under Review");
		 }
		 else if(statusapp.equals("InProgress"))
		 {
			 System.out.println("InProgress");
			 model.addObject("display_Issuestatus","InProgress"); 
		 }
		 else 
		 {
			 model.addObject("display_Issuestatus","Solved"); 
		 }
		
		//Setting objects for user's details, owner's details.
		model.addObject("OwnerDetails", user);
		model.addObject("issuedetails", application);
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		model.setViewName("admin/issue_more_details");
		return model;
	}
	
	/**
	 * Description: This method is used for updating the status of the service issue by admin
	 * Input: id(issue id), status, httpResponse, IOException, request
	 * Output: This method redirects to viewAllIssues or findAllapplications method
	 
	 */
	@RequestMapping(value= {"/IssueStatusUpdate/{id}/{status}"}, method=RequestMethod.GET)
	public void issueStatusUpdate(@PathVariable int id, @PathVariable String status, HttpServletResponse httpResponse) throws IOException {
		String date="-------";
		//updating the status
		if(status.equals("Solved"))
		{
		String pattern = "MM-dd-yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		date = simpleDateFormat.format(new Date());
		}
		//saving the status into database.
		issueAdminService.saveDetails(id,status,date);
		httpResponse.sendRedirect("/viewAllIssues/");
		
	}
}

