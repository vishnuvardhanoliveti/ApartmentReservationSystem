/*This class handles all the requests and responses for administrator for background verification and leasing process*/
package com.apartmentReservation.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.apartmentReservation.model.Backgroundverification;
import com.apartmentReservation.model.Properties;
import com.apartmentReservation.service.BackgroundVerificationAndLeaseAdminService;
import com.apartmentReservation.service.PropertyService;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfWriter;

@RestController
public class BackgroundVerificationAndLeaseAdmin {
	@Autowired
	private BackgroundVerificationAndLeaseAdminService backgroundVerificationAndLeaseAdminService;
	@Autowired
	private PropertyService propertyService;
	/*This method is used for fetching and display all the background verification applications to administrator*/
	@RequestMapping(value= {"/process_background_applications"}, method=RequestMethod.GET)
	public ModelAndView findAllapplications(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		List<Backgroundverification> applications = backgroundVerificationAndLeaseAdminService.findAllApplications();
		ModelAndView model = new ModelAndView();
		model.addObject("bvapplications", applications);
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		model.setViewName("admin/process_background_applications");
		return model;
	}
	
	/*This method is used for updating the status of a background verification applications by administrator*/
	@RequestMapping(value= {"/statusupdate/{email}/{status}"}, method=RequestMethod.GET)
	public ModelAndView statusupdate(@PathVariable String email, @PathVariable String status, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		ModelAndView model = new ModelAndView();
		Backgroundverification application = backgroundVerificationAndLeaseAdminService.findByEmail(email);
		if(status.equals("accepted"))
		{
			backgroundVerificationAndLeaseAdminService.saveLeaseDetails(status, application.getEmail());
			model.addObject("updatestatus", "yes");
		}
		else
		{
			 backgroundVerificationAndLeaseAdminService.saveLeaseDetails(status, application.getEmail());
			String emailid_rm1= application.getEmailid_rm1();
			String emailid_rm2= application.getEmailid_rm2();
			Backgroundverification application_rm1 = backgroundVerificationAndLeaseAdminService.findByEmail(emailid_rm1);
			Backgroundverification application_rm2 = backgroundVerificationAndLeaseAdminService.findByEmail(emailid_rm2);
			 if(!application.getOtherroommates().equals("No"))
			 {
				 model.addObject("otherroommates", "yes");
				if(application_rm1!=null)
				{
					model.addObject("numberroommates", "1");
					backgroundVerificationAndLeaseAdminService.saveLeaseDetails(status, application_rm1.getEmail());
				}
				if(application_rm2!=null)
				{
					model.addObject("numberroommates", "2");
					backgroundVerificationAndLeaseAdminService.saveLeaseDetails(status, application_rm2.getEmail());
				}
				
			 }
		}
		
		List<Backgroundverification> applications = backgroundVerificationAndLeaseAdminService.findAllApplications();
		
		model.addObject("bvapplications", applications);
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		model.setViewName("admin/process_background_applications");
		return model;
	}
	
	/*This method is used for fetching and display all the details of a customer's application for background verification to administrator*/
	@RequestMapping(value= {"/background_verification_updatepage/{email}"}, method=RequestMethod.GET)
	public ModelAndView updateBackground(@PathVariable String email, HttpServletRequest request) throws DocumentException, URISyntaxException, IOException{
		HttpSession session = request.getSession();
		Object userName = session.getAttribute("userName");
		Object userRole = session.getAttribute("userRole");
		
		Backgroundverification applications = backgroundVerificationAndLeaseAdminService.findByEmail(email);
		ModelAndView model = new ModelAndView();
		model.addObject("backgroundUpdate", applications);
		model.addObject("userName", userName);
		model.addObject("userRole", userRole);
		model.setViewName("admin/background_verification_updatepage");
		System.out.println(applications.getApp_bgc_status());
		String statusapp=applications.getApp_bgc_status();
		String ownersign=applications.getOwnerSign();
		Properties property = propertyService.findProperty(applications.getPropertyId());
		String propertyname=property.getPropertyName()+", Apt no "+property.getAptNumber()+", at "+property.getPropertyLocation();
		model.addObject("propertyname",propertyname);
		 if(!statusapp.equals("Under Review"))
		 {
			model.addObject("display_appstatus","yes");
		 }
		 else
		 {
			 model.addObject("display_appstatus","no"); 
		 }
		 String pdfdownload=applications.getApp_lease_status();
			if(pdfdownload!=null)
			{
				model.addObject("downloadpdf", "yes");
				model.addObject("docname",applications.getLeasedocname());
			}
		 String otherroommate=applications.getOtherroommates();
			String emailid_rm1= applications.getEmailid_rm1();
			String emailid_rm2= applications.getEmailid_rm2();
			Backgroundverification application_rm1 = backgroundVerificationAndLeaseAdminService.findByEmail(emailid_rm1);
			Backgroundverification application_rm2 = backgroundVerificationAndLeaseAdminService.findByEmail(emailid_rm2);
			
		 if(!otherroommate.equals("No"))
		 {
			 System.out.println("yes");
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
			 System.out.println("no");
			 model.addObject("otherroomate","no");
		 }
		return model;
	}
}
