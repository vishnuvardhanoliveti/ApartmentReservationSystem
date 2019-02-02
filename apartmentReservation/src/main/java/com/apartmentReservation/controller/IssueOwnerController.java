/*This controller handles all Service Issues functionality of the owner. It is used for checking the submitted issues and change their status by owner*/
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
import com.apartmentReservation.service.IssueAdminService;
import com.apartmentReservation.service.UserService;


@RestController
public class IssueOwnerController {
	@Autowired
	private IssueAdminService issueAdminService;
	@Autowired
	private UserService userService;
	
	/**
	 * Description: This method is used to display all Service Issues Portal to owner
	 * Input: request
	 * Output: This method returns model as output which contains the ViewName and other objects like userName,userRole etc.
	
	 */
	@RequestMapping(value= {"/viewAllMyPropertyIssues"}, method=RequestMethod.GET)
	public ModelAndView findAllapplications(HttpServletRequest request) {
		//Getting the userName,userRole of the logged in user.
		HttpSession session = request.getSession();
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		Object userEmail = session.getAttribute("userEmail");
		
		//Getting all issues applications
		User user = userService.findUserByEmail((String) userEmail);
		List<Issue> applications = issueAdminService.findByPropertyOwnerId(user.getId());
		
		//Setting objects for user's details.
		ModelAndView model = new ModelAndView();
		model.addObject("issueapplications", applications);
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		model.setViewName("owner/view_all_issues");
		return model;
	}

	/**
	 * Description: This method is used to display all Service Issues Portal to owner
	 * Input: id(issue id), request
	 * Output: This method returns model as output which contains the ViewName and other objects like userName,userRole etc.
	
	 */
	@RequestMapping(value= {"/issueInDetail/{id}"}, method=RequestMethod.GET)
	public ModelAndView issuestatusupdate(@PathVariable int id, HttpServletRequest request) {
		
		//Getting the userName,userRole of the logged in user.
		HttpSession session = request.getSession();
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		ModelAndView model = new ModelAndView();
		
		//Getting issue details by ID.
		Issue application = issueAdminService.findById(id);
		String statusapp=application.getIssueStatus();
		
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
		model.addObject("issuedetails", application);
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		model.setViewName("owner/issue_more_details");
		return model;
	}
	
	/**
	 * Description: This method is used for updating the status of the service issue by owner.
	 * Input: id(issue id), status, httpResponse, IOException, request
	 * Output: This method redirects to viewAllMyPropertyIssues or findAllapplications method
	
	 */
	@RequestMapping(value= {"/OwnerIssueStatusUpdate/{id}/{status}"}, method=RequestMethod.GET)
	public void issueStatusUpdate(@PathVariable int id, @PathVariable String status, HttpServletResponse httpResponse, HttpServletRequest request) throws IOException {
		//updating the status
		String date="-------";
		if(status.equals("Solved"))
		{
		String pattern = "MM-dd-yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		date = simpleDateFormat.format(new Date());
		}
		//saving the status into database.
		issueAdminService.saveDetails(id,status,date);
		httpResponse.sendRedirect("/viewAllMyPropertyIssues/");
		
	}
}