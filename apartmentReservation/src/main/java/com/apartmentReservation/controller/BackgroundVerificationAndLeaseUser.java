/*This class handles all the requests and responses for customer for background verification and leasing process*/
package com.apartmentReservation.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.apartmentReservation.model.Backgroundverification;
import com.apartmentReservation.model.Properties;
import com.apartmentReservation.model.User;
import com.apartmentReservation.service.BackgroundVerificationAndLeaseUserService;
import com.apartmentReservation.service.BackgroundVerificationAndLeaseAdminService;
import com.apartmentReservation.service.PropertyService;
import com.apartmentReservation.service.UserService;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@Controller
public class BackgroundVerificationAndLeaseUser {
	
	@Autowired
	 private UserService userService;
	 @Autowired
	 private BackgroundVerificationAndLeaseUserService backgroundVerificationAndLeaseUserService;
	 
	 @Autowired
	 private  PropertyService propertyService;
	 @Autowired
	 private BackgroundVerificationAndLeaseAdminService backgroundVerificationAndLeaseAdminService;
	 
	 String sign_rm1;
	String name_rm1;
	String leaseSignedDate_rm1;
	String sign_rm2;
	String name_rm2;
	String leaseSignedDate_rm2;
	 private  String FILE_PATH = "src/main/resources/static/LeasingDocument/";
	    private static final String APPLICATION_PDF = "application/pdf";

	/*This method is used for displaying the lease document for customer to sign*/
	 @RequestMapping(value= {"/signthelease"}, method=RequestMethod.GET)
	 public ModelAndView signthelease(HttpServletRequest request) {
		 HttpSession session = request.getSession();
		 Object userName = session.getAttribute("userName");
		 Object userRole = session.getAttribute("userRole");
		 Object displayOptions = session.getAttribute("display_options");
		Object displayStatus = session.getAttribute("display_status");
		Object displaySignTheLease = session.getAttribute("display_signthelease");
		Object downloadPDF = session.getAttribute("downloadpdf");
		 ModelAndView model = new ModelAndView();
		 User user = new User();
		 model.addObject("user", user);
		 model.addObject("userName", userName);
		 model.addObject("userRole", userRole);
		 model.addObject("display_options", displayOptions);
		model.addObject("display_status", displayStatus);
		model.addObject("display_signthelease", displaySignTheLease);
		model.addObject("downloadpdf", downloadPDF);
		 model.setViewName("user/sign_the_lease");
		 return model;
	 }
	 
	 /*This method is used for generating and saving the lease document after customer sign the lease*/
	 @RequestMapping(value= {"/signthelease"}, method=RequestMethod.POST)
	 public String signthelease(@RequestParam("sign") String sign, HttpServletRequest request) throws IOException, DocumentException {
		 System.out.println(sign);
		 HttpSession session = request.getSession();
		 Object email= session.getAttribute("userEmail");
		 User user = userService.findUserByEmail((String) email);
		 Backgroundverification backExists = backgroundVerificationAndLeaseUserService.findUserByEmail((String) email);
		 Properties property = propertyService.findProperty(backExists.getPropertyId());
		 Calendar cal = Calendar.getInstance();
		 int month = cal.get(Calendar.MONTH)+2;
		 int endMonth ;
		 if(month ==1)
			 endMonth = 12;
		 else
			 endMonth =month -1;
		 int year = cal.get(Calendar.YEAR);
		 int nextYear = year+1;
		 String leaseYear =Integer.toString(year);
		 String leaseEndYear =Integer.toString(nextYear);
		 String leaseMonth =Integer.toString(month);
		 String leaseEndMonth =Integer.toString(endMonth);
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());
		String leaseSignedDate =date;
		String leaseStartDate =leaseYear+"-"+leaseMonth+"-"+"01";
		String leaseEndDate=leaseEndYear+"-"+leaseEndMonth+"-"+"30";
		backgroundVerificationAndLeaseUserService.saveLeaseDetails(sign,email,leaseSignedDate,leaseStartDate,leaseEndDate);
	  ModelAndView model = new ModelAndView();
	   model.addObject("msg", "Lease Document signed successfully!");
	   model.addObject("user", new User());
	   model.setViewName("user/home");
	   Backgroundverification applications = backgroundVerificationAndLeaseAdminService.findByEmail((String) email);
		model.addObject("backgroundUpdate", applications);
		model.setViewName("admin/background_verification_updatepage");	
		
		String emailid_rm1= applications.getEmailid_rm1();
		String emailid_rm2= applications.getEmailid_rm2();
		String rooomateno=applications.getOtherroommates();
		Backgroundverification application_rm1 = backgroundVerificationAndLeaseAdminService.findByEmail(emailid_rm1);
		Backgroundverification application_rm2 = backgroundVerificationAndLeaseAdminService.findByEmail(emailid_rm2);
		int generatepdf =1;
		
		sign_rm1="not applicable";
		name_rm1="not applicable";
		leaseSignedDate_rm1="not applicable";
		sign_rm2="not applicable";
		name_rm2="not applicable";
		leaseSignedDate_rm2="not applicable";
		
		 if(rooomateno.equals("1"))
		{
			if(application_rm1!=null)
			{
				if(application_rm1.getInitials()!=null )
				{
					generatepdf=1;
					sign_rm1=application_rm1.getInitials();
					name_rm1=application_rm1.getFirstname()+" "+application_rm1.getLastname();
					leaseSignedDate_rm1=application_rm1.getLeaseSignedDate();				}
				else
				{

					generatepdf=0;
				}
			}
		}
		
		 else if(rooomateno.equals("2")) {
			if(application_rm1!=null && application_rm2!=null)
			{
				if(application_rm1.getInitials()!=null && application_rm2.getInitials()!=null )
				{
					generatepdf=1;
					sign_rm1=application_rm1.getInitials();
					name_rm1=application_rm1.getFirstname()+" "+application_rm1.getLastname();
					leaseSignedDate_rm1=application_rm1.getLeaseSignedDate();
					sign_rm2=application_rm2.getInitials();
					name_rm2=application_rm2.getFirstname()+" "+application_rm2.getLastname();
					leaseSignedDate_rm2=application_rm2.getLeaseSignedDate();
				}
				else
				{

					generatepdf=0;
				}
			}
			else
			{
				generatepdf=0;
			}
			}
		
		
		if(generatepdf==1)
		{
		Path path = Paths.get("src/main/resources/templates/admin/fileTest.txt");
		Path path1 = Paths.get("src/main/resources/tempdoc/fileTest.txt");
		Files.copy( path, path1, StandardCopyOption.REPLACE_EXISTING );
		String docname=property.getPropertyName()+"_Aptno_"+property.getAptNumber()+"_leasing_document.pdf";
		propertyService.updateLeaseinfo(property.getPropertyId());
		String fullname=applications.getFirstname()+" "+applications.getLastname();
		try (Stream<String> lines = Files.lines(path1)) {
			   List<String> replaced = lines
			       .map(line-> line.replaceAll("fullName",fullname))
			       .map(line-> line.replaceAll("leaseStartDate",leaseStartDate))
			       .map(line-> line.replaceAll("leaseEndDate",leaseEndDate))
			       .map(line-> line.replaceAll("leaseSignedDate",leaseSignedDate))
			       .map(line-> line.replaceAll("customer_sign",sign))
			       .map(line-> line.replaceAll("month_rent", Integer.toString(property.getPropertyRent())))
			       .map(line-> line.replaceAll("home_address",property.getPropertyAddress()))
			       .map(line-> line.replaceAll("owner_name",applications.getOwnerSign()))
			       .map(line-> line.replaceAll("secuirty_deposit", Integer.toString(property.getPropertyDeposit())))
			       .map(line-> line.replaceAll("name_resident",user.getFirstName()+" "+user.getLastName()))
			       .map(line-> line.replaceAll("lease_SignedDaterm1",leaseSignedDate_rm1))
			       .map(line-> line.replaceAll("sign_rm1",sign_rm1))
			       .map(line-> line.replaceAll("name_rm1",name_rm1))
			       .map(line-> line.replaceAll("lease_SignedDaterm2",leaseSignedDate_rm2))
			       .map(line-> line.replaceAll("sign_rm2",sign_rm2))
			       .map(line-> line.replaceAll("name_rm2",name_rm2))
			       
			       .collect(Collectors.toList());
			   Files.write(path1, replaced);
			}
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream("src/main/resources/static/LeasingDocument/"+docname));
		document.open();
		Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
		  document.add(new Paragraph("Terms and Conditions"));
		try (Stream<String> lines = Files.lines(path1)) {
		    
			lines.forEach(line -> {
				Chunk chunk = new Chunk(line, font);
				try {
					document.add(chunk);
					document.add(Chunk.NEWLINE);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				});
	
		document.close();
		backgroundVerificationAndLeaseUserService.updateLeaseStatus("done",docname,applications.getEmail());
		if(application_rm1!=null )
		{
			backgroundVerificationAndLeaseUserService.updateLeaseStatus("done",docname,application_rm1.getEmail());  	
		}
		if(application_rm2!=null )
		{
			backgroundVerificationAndLeaseUserService.updateLeaseStatus("done",docname,application_rm2.getEmail());
		}
	 }
		catch (IOException e) {
			e.printStackTrace();
		}
		}
		return "redirect:/home";
	 }
	 
	 /*This method is used for displaying background verification application form to customer to start leasing process*/
	 @RequestMapping(value= {"/submit_background_verification_application/{pid}"}, method=RequestMethod.GET)
	 public ModelAndView backgv(@PathVariable int pid, HttpServletRequest request) {
	  ModelAndView model = new ModelAndView();
	  model.addObject("propId",pid);
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

	  model.setViewName("user/submit_background_verification_application");
	  
	  return model;
	 }
	 
	 /*This method is used for saving the customer responses to background verification application to database*/
	 @RequestMapping(value= {"/submit_background_verification_application/{pid}"}, method=RequestMethod.POST)
	 public String backgroundverification(@Valid Backgroundverification backgroundverification, BindingResult bindingResult,@PathVariable int pid, HttpServletRequest request) {
	  ModelAndView model = new ModelAndView();
	  HttpSession session = request.getSession();
		 Object userdetails= session.getAttribute("userEmail");
		 Object userName = session.getAttribute("userName");
		 Object userRole = session.getAttribute("userRole");
		 Object displayOptions = session.getAttribute("display_options");
			Object displayStatus = session.getAttribute("display_status");
			Object displaySignTheLease = session.getAttribute("display_signthelease");
			Object downloadPDF = session.getAttribute("downloadpdf");
			model.addObject("userName", userName);
			 model.addObject("userRole", userRole);
			 model.addObject("display_options", displayOptions);
			model.addObject("display_status", displayStatus);
			model.addObject("display_signthelease", displaySignTheLease);
			model.addObject("downloadpdf", downloadPDF);
		 User user = userService.findUserByEmail((String) userdetails);
		 
	  Properties property = propertyService.findProperty(pid);
	  System.out.println(pid);
	  backgroundverification.setFirstname_rm1(backgroundverification.getFirstname_rm1().replaceAll(",", ""));
	  backgroundverification.setLastname_rm1(backgroundverification.getLastname_rm1().replaceAll(",", ""));
	  backgroundverification.setEmailid_rm1(backgroundverification.getEmailid_rm1().replaceAll(",", ""));
	  backgroundverification.setPropertyId(pid);
	  backgroundverification.setDob(user.getDob());
	  backgroundverification.setApp_bgc_status("Under Review");
	  System.out.println(user.getEmail());
	  backgroundverification.setEmail(user.getEmail());
	  backgroundverification.setFirstname(user.getFirstName());
	  backgroundverification.setLastname(user.getLastName());
	  backgroundverification.setAddress(user.getAddressLine1());
	  backgroundverification.setPhonenumber(user.getPhoneNumber());
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
	 
	 @RequestMapping(value = "/download/leasepdf", method = RequestMethod.GET, produces = APPLICATION_PDF)
	 public @ResponseBody Resource downloadC(HttpServletResponse response,HttpServletRequest request) throws FileNotFoundException { 
		 HttpSession session = request.getSession();
		 Object email= session.getAttribute("userEmail");
		 Backgroundverification user=backgroundVerificationAndLeaseUserService.findUserByEmail(email.toString());
		 String LeasingDocName=user.getLeasedocname();
		
		 File file = getFile(LeasingDocName);
		    response.setContentType(APPLICATION_PDF);
		    response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
		    response.setHeader("Content-Length", String.valueOf(file.length()));
		    return new FileSystemResource(file);
	 }
	 
	 private File getFile(String name) throws FileNotFoundException {
		 FILE_PATH=FILE_PATH+name;
		
	        File file = new File(FILE_PATH);
	        FILE_PATH = "src/main/resources/static/LeasingDocument/";
	        if (!file.exists()){
	            throw new FileNotFoundException("file with path: " + FILE_PATH +name+ " was not found.");
	        }
	        
	        return file;
	    }

/*This method is used for viewing the property that customer is trying to lease*/
	 @RequestMapping(value = "/ViewMyProperty/", method = RequestMethod.GET)
	 public String ViewMyProperty(HttpServletRequest request) { 
		 HttpSession session = request.getSession();
		 Object email= session.getAttribute("userEmail");
		 Object status=session.getAttribute("display_status");
		 ModelAndView model = new ModelAndView();
		 int id =backgroundVerificationAndLeaseUserService.getPropertyId(status.toString(),email.toString());
		 Properties property = propertyService.findProperty(id);
		 model.addObject("id", id);
		 return "redirect:/propertyDescription/"+id;
		 
		   
	 }

}
