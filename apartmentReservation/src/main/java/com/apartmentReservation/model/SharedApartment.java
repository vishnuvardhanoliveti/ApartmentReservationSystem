/*This class has getters and setters of the SharedApartment attributes. It is related to the SharedApartment table
 * @author  Vishnu Vardhan Oliveti
 * Written on:November 20,2018
 */
package com.apartmentReservation.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.apartmentReservation.model.Backgroundverification.BackgroundverificationBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity

@Data

@Table(name = "sharedapartment")
public class SharedApartment {
	@Id
	@Column(name = "sharedapt_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name = "property_id")
	private int propertyId;
	
	@Column(name = "user_email")
	private  String userEmail;
}
