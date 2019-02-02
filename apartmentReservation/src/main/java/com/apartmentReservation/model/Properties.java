/*This class has getters and setters of the Properties attributes. It is related to the properties table.*/
package com.apartmentReservation.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import com.apartmentReservation.model.User.UserBuilder;

import lombok.AllArgsConstructor;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "properties")
@Component
public class Properties {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int propertyId;

	@Column(name = "is_featured")
	private boolean isFeatured;

	@Column(name = "property_name")
	private String propertyName;

	@Column(name = "apt_number")
	private int aptNumber;

	@Column(name = "property_location")
	private String propertyLocation;
	
	@Column(name = "property_address")
	private String propertyAddress;
	
	@Column(name = "property_owner")
	private int propertyOwner;

	@Column(name = "property_image_path")
	private String propertyImagePath;

	@Column(name = "property_type")
	private String propertyType;

	@Column(name = "property_bedrooms")
	private int propertyBedrooms;

	@Column(name = "property_bath")
	private int propertyBath;
	
	@Column(name = "property_pets")
	private int propertyPets;
	
	@Column(name = "property_utility")
	private int propertyUtility;

	@Column(name = "property_rent")
	private int propertyRent;

	@Column(name = "property_description")
	private String propertyDescription;

	@Column(name = "property_deposit")
	private int propertyDeposit;
	
	@Column(name = "is_leased")
	private String Leased;


	
}
