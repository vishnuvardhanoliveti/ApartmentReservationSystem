/*This class has getters and setters of the backgroundverification attributes. It is related to the backgroundverification table.*/
package com.apartmentReservation.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "backgroundverification")
public class Backgroundverification {
	

	 @Id
	 @GeneratedValue(strategy = GenerationType.AUTO)
	 private int id;
	 

	 @Column(name = "address")
	 private String address;
	 
	 @Column(name = "app_bgc_status")
	 private String app_bgc_status; 
	 
	 @Column(name = "app_lease_status")
	 private String app_lease_status; 
	 
	 @Column(name = "email")
	 private String email;
	 
	 @Column(name = "firstname")
	 private String firstname; 
	 
	 @Column(name = "lastname")
	 private String lastname;

	 
	 @Column(name = "dob")
	 private String dob;
	 
	 @Column(name = "phoneno")
	 private String phonenumber;
	 
	 @Column(name = "ssn")
	 private String ssn;
	 
	 @Column(name = "intials")
	 private String initials;
	 
	 @Column(name = "lease_start_date")
	 private String leaseStartDate;
	 
	 @Column(name = "lease_end_date")
	 private String leaseEndDate;
	 
	 @Column(name = "lease_signed_date")
	 private String leaseSignedDate;
	 
	 @Column(name = "ethnicity")
	 private String ethnicity;
	 
	@Column(name = "occupation")
	 private String occupation;
	
	@Column(name = "Salary")
	 private String salary;
	
	 @Column(name = "smokinghabit")
	 private String smokinghabit;
	 
	@Column(name = "pets")
	 private String pets;
	
	@Column(name = "otherroommates")
	 private String otherroommates;
	 
	 @Column(name = "propertyid")
		private int propertyId;
	 
	 @Column(name = "propertyowner")
	 private int propertyOwner;
	 
	 @Column(name = "owner_sign")
	 private String ownerSign;
	 
	 @Column(name = "firstname_rm1")
	 private String firstname_rm1;
	 
	 @Column(name = "lastname_rm1")
	 private String lastname_rm1;
	 
	 @Column(name = "emailid_rm1")
	 private String emailid_rm1;
	 
	 @Column(name = "firstname_rm2")
	 private String firstname_rm2;
	 
	 @Column(name = "lastname_rm2")
	 private String lastname_rm2;
	 
	 @Column(name = "emailid_rm2")
	 private String emailid_rm2;
	 
	 @Column(name = "lease_doc_name")
	 private String leasedocname;
	 
	
	 
}
	