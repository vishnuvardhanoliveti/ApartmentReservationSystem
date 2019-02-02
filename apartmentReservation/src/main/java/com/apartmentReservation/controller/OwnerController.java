/*This class handles all the requests and responses for all his operations*/
package com.apartmentReservation.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.apartmentReservation.model.Backgroundverification;
import com.apartmentReservation.model.Properties;
import com.apartmentReservation.model.RatingsAndReviews;
import com.apartmentReservation.model.User;
import com.apartmentReservation.service.BackgroundVerificationAndLeaseAdminService;
import com.apartmentReservation.service.PropertyService;
import com.apartmentReservation.service.RatingsAndReviewsService;
import com.apartmentReservation.service.UserService;
import com.itextpdf.text.DocumentException;

@Controller
public class OwnerController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private PropertyService propertyService;
	@Autowired
	private BackgroundVerificationAndLeaseAdminService backgroundVerificationAndLeaseAdminService;
	@Autowired
	private RatingsAndReviewsService ratingsAndReviewsService;
	
	Map<String,Double> propertyToRatingMap = new TreeMap<String,Double>();
	
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
	
	/* This method is used for displaying all the owner's properties to himself */
	@RequestMapping(value = { "/viewProperties" }, method = RequestMethod.GET)
	public ModelAndView viewProperties(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		ModelAndView model = new ModelAndView();
		List<Properties> ownerProperties = propertyService.findPropertybyOwner(user.getId());
		getRatingsForProperties(ownerProperties);
		int numberOfProperties = ownerProperties.size();
		model.addObject("numberOfProperties",numberOfProperties);
		model.addObject("prop", ownerProperties);
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		model.addObject("ratingByPropertyName", propertyToRatingMap);//adding rating to render on the home page.
		model.setViewName("owner/view_my_properties");
		
		return model;
	}

	/*This method is used for fetching and display to owner, all the background verification applications submitted to his properties*/
	@RequestMapping(value= {"/viewAllApplications"}, method=RequestMethod.GET)
	public ModelAndView findAllapplications(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		List<Backgroundverification> applications = backgroundVerificationAndLeaseAdminService.findAllApplicationsofOwner(user.getId());
		int numberOfApplications=applications.size();
		ModelAndView model = new ModelAndView();
		model.addObject("vapplications", applications);
		model.addObject("numberOfApplications",numberOfApplications);
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		model.setViewName("owner/view_all_applications");
		
		
		
		return model;
	}
	
	/*This method is used for fetching and display all the details of particular customer's background verification application to owner*/
	@RequestMapping(value= {"/applicationsDetails/{email}"}, method=RequestMethod.GET)
	public ModelAndView updateBackground(@PathVariable String email, HttpServletRequest request) throws DocumentException, URISyntaxException, IOException{
		
		HttpSession session = request.getSession();
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		Backgroundverification application = backgroundVerificationAndLeaseAdminService.findByEmail(email);
		ModelAndView model = new ModelAndView();
		model.addObject("backgroundUpdate", application);
		model.setViewName("owner/applications_details");
		
		String statusapp=application.getApp_bgc_status();
		String ownersign=application.getOwnerSign();
		Properties property = propertyService.findProperty(application.getPropertyId());
		String propertyname=property.getPropertyName()+", Apt no "+property.getAptNumber()+", at "+property.getPropertyLocation();
		model.addObject("propertyname",propertyname);
		 if(statusapp.equals("Under Review"))
		 {
			
			model.addObject("display_appstatus","yes");
		 }
		 else
		 {
			 if(ownersign==null)
			 {
			 
			 model.addObject("display_appstatus","no");
			 }
			 else
			 {
				 model.addObject("display_appstatus","yes");
			 }
		 }
		 String pdfdownload=application.getApp_lease_status();
			if(pdfdownload!=null)
			{
				model.addObject("downloadpdf", "yes");
				model.addObject("docname",application.getLeasedocname());
			}
		 
		 String otherroommate=application.getOtherroommates();
			String emailid_rm1= application.getEmailid_rm1();
			String emailid_rm2= application.getEmailid_rm2();
			Backgroundverification application_rm1 = backgroundVerificationAndLeaseAdminService.findByEmail(emailid_rm1);
			Backgroundverification application_rm2 = backgroundVerificationAndLeaseAdminService.findByEmail(emailid_rm2);
			
		 if(!otherroommate.equals("No"))
		 {
			
			 if(otherroommate.equals("1"))
			 {
				 model.addObject("otherroomate","1");
			 }
			 else
			 {
				 model.addObject("otherroomate","2");
			 }
			if(application_rm1==null)
			{
				model.addObject("appnotavail","yes");
			}
			else
			{
				model.addObject("appnotavail","no");
			}
			if(application_rm2==null)
			{
				model.addObject("appnotavail1","yes");
			}
			else
			{
				model.addObject("appnotavail1","no");
			}
			
		 }
		 else
		 {
			 
			 model.addObject("otherroomate","no");
		 }
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		
		return model;
	}
	
	/*This method is used for accepting/rejecting/siging the background verification application by owner*/
	@RequestMapping(value= {"/ownersign/{email}/{status}"}, method=RequestMethod.GET)
	public String ownerSign(@PathVariable String email, @PathVariable String status, HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		if(status.equals("accepted")) {
			String ownername=user.getFirstName()+" "+user.getLastName();
			Backgroundverification userApplication = backgroundVerificationAndLeaseAdminService.updateOwnerSign(email, ownername);
		}
		else
		{
			Backgroundverification userApplication=backgroundVerificationAndLeaseAdminService.updateStatus(email, status);	
		}
		
		return "redirect:/applicationsDetails/"+email;
	}
}
