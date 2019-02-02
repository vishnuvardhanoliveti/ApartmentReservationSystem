/*This class has getters and setters of the Issue attributes. It is related to the issues table.*/
package com.apartmentReservation.model;

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
@Table(name = "issues")
public class Issue {

	@Id
	 @GeneratedValue(strategy = GenerationType.AUTO)
	 @Column(name = "issue_id")
	 private int issueId;
	
	 @Column(name = "issue_user_email")
	 private String issueUserEmail;
	 
	 @Column(name = "issue_user_firstname")
	 private String issueUserFirstName;
	 
	 @Column(name = "issue_user_lastname")
	 private String issueUserLastName;
	 
	 @Column(name = "issue_status")
	 private String issueStatus;
	 
	 @Column(name = "issue_type")
	 private String issueType;
	 
	 @Column(name = "issue_description")
	 private String issueDescription;
	 
	 @Column(name = "issue_address")
	 private String issueAddress;
	 
	 @Column(name = "issue_phone_number")
	 private String issuePhoneNumber;
	 
	 @Column(name = "issue_submitted_date")
	 private String issueSubmittedDate;
	 
	 @Column(name = "issue_solved_date")
	 private String issueSolvedDate;
	 
	 @Column(name = "propertyowner")
	 private int propertyOwner;

	
	 
}
