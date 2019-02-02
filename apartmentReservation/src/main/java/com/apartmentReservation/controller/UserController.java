/*This class handles  requests and responses for customer for login, registration, unauthorized access*/
package com.apartmentReservation.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.apartmentReservation.model.Backgroundverification;
import com.apartmentReservation.model.Properties;
import com.apartmentReservation.model.RatingsAndReviews;
import com.apartmentReservation.model.User;
import com.apartmentReservation.service.BackgroundVerificationAndLeaseAdminService;
import com.apartmentReservation.service.BackgroundVerificationAndLeaseUserService;
import com.apartmentReservation.service.PropertyService;
import com.apartmentReservation.service.RatingsAndReviewsService;
import com.apartmentReservation.service.UserService;

@Controller
@SessionAttributes(value = { "userName", "userRole", "userEmail", "display_options","display_status","display_signthelease","downloadpdf" })
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private BackgroundVerificationAndLeaseUserService backgroundVerificationAndLeaseUserService;
	@Autowired
	private PropertyService propertyService;
	@Autowired
	private BackgroundVerificationAndLeaseAdminService backgroundVerificationAndLeaseAdminService;
	@Autowired
	private RatingsAndReviewsService ratingsAndReviewsService;
	
	Map<String,Double> propertyToRatingMap = new TreeMap<String,Double>();
	Map<String,List<RatingsAndReviews>> propertyToRatingAndReviewsMap = new TreeMap<String,List<RatingsAndReviews>>();
	
	/**
	 * This method fetches average rating of each property in the list of all properties based on the property name
	 * and stores them in a map for the view to render it.
	 * @param allPropertiesList
	 * @return 
	 */
	public void getRatingsForProperties(List<Properties> allPropertiesList) {
		List<RatingsAndReviews> ratingsAndReviewsForEachProperty = new ArrayList<RatingsAndReviews>();
		for(Properties property : allPropertiesList) {
			ratingsAndReviewsForEachProperty = ratingsAndReviewsService.findRatingsAndReviewsByPropertyName(property.getPropertyName());
			
			if(ratingsAndReviewsForEachProperty.size()!=0) {
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
	
	/* This method is used for redirecting to home page */
	@RequestMapping(value = {"/"}, method = RequestMethod.GET)
	public String login(HttpServletRequest request) {
		return "redirect:/home";
	}
	
	/* This method is used for Login to display the login page */
	@RequestMapping(value= {"/login"}, method = RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView model = new ModelAndView();
		model.setViewName("user/login");
		return model;
	}

	/* This method is used for Registration to display the signup form */
	@RequestMapping(value = { "/signup" }, method = RequestMethod.GET)
	public ModelAndView signup() {
		ModelAndView model = new ModelAndView();
		User user = new User();
		model.addObject("user", user);
		model.setViewName("user/signup");
		
		return model;
	}

	/* This method is used for posting the values of the signup form */
	@RequestMapping(value = { "/signup" }, method = RequestMethod.POST)
	public ModelAndView createUser(@Valid User user, BindingResult bindingResult) {

		ModelAndView model = new ModelAndView();
		User userExists = userService.findUserByEmail(user.getEmail());
		if (userExists != null) {
			bindingResult.rejectValue("email", "error.user", "This email already exists!");
			model.addObject("error","This email already exists!");
		}
		if (bindingResult.hasErrors()) {
			model.setViewName("user/signup");
			model.addObject("error2","Binding errors");
		} else {
			User createdUser = userService.saveUser(user);
			model.addObject("msg", "User has been registered successfully!");
			model.addObject("user", new User());
			model.setViewName("user/signup");
		}
		return model;
	}
	
	/* This method is for handling errors */
	@RequestMapping(value = { "/access_denied" }, method = RequestMethod.GET)
	public ModelAndView accessDenied() {
		ModelAndView model = new ModelAndView();
		model.setViewName("errors/access_denied");
		return model;
	}

	/* This method is used for displaying contact page*/
	@RequestMapping(value = { "/contact" }, method = RequestMethod.GET)
	public ModelAndView contact() {
		ModelAndView model = new ModelAndView();
		model.setViewName("user/contact_us");

		return model;
	}

	/* This method is used for posting the values of the signup form */
	@RequestMapping(value = { "/contact" }, method = RequestMethod.POST)
	public ModelAndView fillcontact( BindingResult bindingResult) {

		ModelAndView model = new ModelAndView();

		 

		return model;
	}
	/*
	 * When /home is passed in URL depending on the user role he is redirected to
	 * the appropriate page
	 */
	@RequestMapping("/home")
	public ModelAndView defaultAfterLogin(HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		User user = userService.findUserByEmail(auth.getName());
		String role ="nouser";
		if(user!=null) {
			System.out.println(user.getEmail());
			Integer userid = user.getId();
			role = userService.userRole(userid);
			model.addObject("userEmail", user.getEmail());
			
			Backgroundverification applications = backgroundVerificationAndLeaseAdminService.findByEmail(user.getEmail());
			
			
			if (applications != null) {
				model.addObject("display_status", applications.getApp_bgc_status());
				model.addObject("display_options", "yes");
				model.addObject("downloadpdf", "no");
				model.addObject("signapplicationemail", user.getEmail());
				String emailid_rm1= applications.getEmailid_rm1();
				String emailid_rm2= applications.getEmailid_rm2();
				Backgroundverification application_rm1 = backgroundVerificationAndLeaseAdminService.findByEmail(emailid_rm1);
				Backgroundverification application_rm2 = backgroundVerificationAndLeaseAdminService.findByEmail(emailid_rm2);
				String sde = applications.getOwnerSign();
				String signinit=applications.getInitials();
				String pdfdownload=applications.getApp_lease_status();
				if(pdfdownload!=null)
				{
					model.addObject("downloadpdf", "yes");
					model.addObject("display_signthelease", "no");	
					model.addObject("docname",applications.getLeasedocname());
				}
				if (sde!=null && signinit==null) {
					
					if(application_rm1!=null)
					{
						if(application_rm1.getOwnerSign()!=null )
						{
							model.addObject("display_signthelease", "yes");
						}
						else
						{
							model.addObject("display_signthelease", "no");	
						}
					}
					if(application_rm2!=null)
					{
						if(application_rm2.getOwnerSign()!=null )
						{
							model.addObject("display_signthelease", "yes");
						}
						else
						{
							model.addObject("display_signthelease", "no");	
						}
					}
					else {
						model.addObject("display_signthelease", "yes");
					}
					
					
				}
			} else {
				model.addObject("display_options", "no");
				model.addObject("display_signthelease", "no");	
			}
			model.addObject("userName", user.getFirstName() + " " + user.getLastName());
		}
		
		if(role=="nouser" || role.toLowerCase().equals("customer")) {
			List<Properties> featuredProperties = propertyService.displayFeaturedProperties();
			getRatingsForProperties(featuredProperties);
			model.setViewName("user/home");
			model.addObject("prop", featuredProperties);
			model.addObject("ratingByPropertyName", propertyToRatingMap);//adding rating to render on the home page.
			model.addObject("userRole", role);
		}
		else if (role.toLowerCase().equals("admin")) {
			model.setViewName("admin/home");
			model.addObject("userRole", role);
			return model;
		} else if (role.toLowerCase().equals("owner")) {
			model.setViewName("owner/home");
			model.addObject("userRole", role);
			return model;
		}
		return model;
	}
	


	  
	
}