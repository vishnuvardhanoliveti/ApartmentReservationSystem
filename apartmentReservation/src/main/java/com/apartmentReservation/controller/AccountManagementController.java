/*This controller is used to handle the requests of displaying the account management form and submitting the posted values
 * 
 * of the form*/
 
package com.apartmentReservation.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.apartmentReservation.configuration.WebMvcConfig;
import com.apartmentReservation.model.Properties;
import com.apartmentReservation.model.User;
import com.apartmentReservation.service.AccountManagementService;
import com.apartmentReservation.service.UserService;

@Controller
public class AccountManagementController {
	@Autowired
	private UserService userService;
	@Autowired
	private AccountManagementService accountManagementService;
	
	/**
	 * Description:This method is used to display the accountManagement form for the users in which they can change their details such as phoneNumber,address etc.
	 * 			   If the user details already exist in the database then they are pre-populated. 
	 * @Input:The input expected is the HttpServletRequest
	 * @return model: This method returns model as output which contains the ViewName and other objects like username,userrole of the logged in user
	 */
	@RequestMapping(value = { "/myAccount" }, method = RequestMethod.GET)
	public ModelAndView accountManagementDisplayForm(HttpServletRequest request) {
		HttpSession session = request.getSession();
		
		//Getting the userName,userRole,userEmail of the logged in user.
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		Object userEmail =session.getAttribute("userEmail");
		
		ModelAndView model = new ModelAndView();
		
		//Getting the details of the logged in user by using the email from session.
		User user=userService.findUserByEmail(userEmail.toString());
		
		//Adding objects to the model for session management purpose
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		model.addObject("user", user);
		
		//Setting the viewName of the model to which the request is redirected.
		model.setViewName("myAccount");
		
		
		return model;
	}
	
	
	/**
	 * Description:This method is used to post the accountManagement form values  the users enter  in order to  change their details such as phoneNumber,address etc.Then it updates those details in the database.			   
	 * @Input:The input expected is the  the user object posted from the form and HttpServletRequest. 
	 * @return model: This method returns model as output which contains the ViewName and other objects like userName,userRole of the logged in user
	
	 */
	@RequestMapping(value = { "/myAccount" }, method = RequestMethod.POST)
	public ModelAndView accountManagementSubmitForm(@ModelAttribute(value = "user") User user,HttpServletRequest request) {
		HttpSession session = request.getSession();
		
		//Getting the userName,userRole,userEmail of the logged in user.
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		Object userEmail =session.getAttribute("userEmail");
		
		user.setEmail(userEmail.toString());
		
		//updating the user details by passing the user object posted from form.
		accountManagementService.updateUser(user);
		ModelAndView model = new ModelAndView();
		
		//Adding objects to the model for session management purpose
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		model.addObject("user", user);
		model.addObject("msg", "Details updated successfully!");
		
		//Setting the viewName of the model to which the request is redirected.
		model.setViewName("myAccount");
		
		
		return model;
	}
	
	
	/**
	 * Description:This method is used to display the password update form.			   
	 *  @Input:The input expected is the HttpServletRequest
	 * @return model: This method returns model as output which contains the ViewName and other objects like username,userrole of the logged in user
	
	 */
	@RequestMapping(value = { "/changePassword" }, method = RequestMethod.GET)
	public ModelAndView changePasswordDisplayForm(HttpServletRequest request) {
		HttpSession session = request.getSession();
		
		//Getting the userName,userRole,userEmail of the logged in user.
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		Object userEmail =session.getAttribute("userEmail");
		
		ModelAndView model = new ModelAndView();
		
		//Getting the details of the logged in user by using the email from session.
		User user=userService.findUserByEmail(userEmail.toString());
		
		//Adding objects to the model for session management purpose
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		model.addObject("user", user);
		
		//Setting the viewName of the model to which the request is redirected.
		model.setViewName("changePassword");
		
		
		return model;
	}
	/**
	 * Description:This method is used to post the update password form values  the users enters 			   
	 * @Input:The input expected is the  the user object posted from the form and HttpServletRequest. 
	 * @return model: This method returns model as output which contains the ViewName and other objects like userName,userRole of the logged in user
	
	 
	 */
	
	@RequestMapping(value = { "/changePassword" }, method = RequestMethod.POST)
	public ModelAndView changePasswordSubmitForm(@ModelAttribute(value = "user") User user,HttpServletRequest request) {
		HttpSession session = request.getSession();
		
		//Getting the userName,userRole,userEmail of the logged in user.
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		Object userEmail =session.getAttribute("userEmail");
		
		user.setEmail(userEmail.toString());
		
		//updating the user details by passing the user object posted from form.
		accountManagementService.updatePassword(user);
		ModelAndView model = new ModelAndView();
		
		//Adding objects to the model for session management purpose
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		model.addObject("user", user);
		model.addObject("msg", "Details updated successfully!");
		
		//Setting the viewName of the model to which the request is redirected.
		model.setViewName("changePassword");
		
		
		return model;
	}
	
}
