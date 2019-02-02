/*This class has getters and setters of the RoommatePreferences attributes. It is related to the roommate_preferences table
 * @author  Vishnu Vardhan Oliveti
 * Written on:November 2,2018
 */

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
@Table(name = "roommate_preferences")
public class RoommatePreferences {
	@Id
	@Column(name = "pref_id")
	 @GeneratedValue(strategy = GenerationType.AUTO)
	 private int id;
	
	@Column(name = "pref_user_id")
	 private int userId;
	
	@Column(name = "pref_user_email")
	 private String userEmail;
	
	@Column(name = "age")
	 private int age;
	
	@Column(name = "actual_gender")
	 private String actualGender;
	
	 @Column(name = "pref_isvegan")
	 private boolean vegan; 
	 
	 @Column(name = "pref_agegroup")
	 private String ageGroup;
	 
	 @Column(name = "pref_gender")
	 private String gender;
	 
	 @Column(name = "pref_drinking")
	 private boolean drinking;
	 
	 @Column(name = "pref_smoking")
	 private boolean smoking;
	 
	 @Column(name = "pref_pets")
	 private boolean pets;
	 
	 @Column(name = "pref_location")
	 private String location;
}
