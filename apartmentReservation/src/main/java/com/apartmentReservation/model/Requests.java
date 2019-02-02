package com.apartmentReservation.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "requests")
@Component
public class Requests {
	
	@Id
	@Column(name = "id")
	 @GeneratedValue(strategy = GenerationType.AUTO)
	 private int id;
	
	@Column(name = "user_email")
	 private String userEmail;
	 
	 @Column(name = "requested_user_email")
	 private String requestedUserEmail; 
	 
	 @Column(name = "user_decision")
	 private String userDecision;
	 
	 @Column(name = "requested_user_gender")
	 private String requestedUserGender;
	 
	 @Column(name = "requested_user_age")
	 private String requestedUserAge;
	 
	 @Column(name = "matched_percent")
	 private String matchedPercent;
	 
	 @Column(name = "user_age")
	 private String userAge;
	 
	 @Column(name = "user_gender")
	 private String userGender;
	 


}
