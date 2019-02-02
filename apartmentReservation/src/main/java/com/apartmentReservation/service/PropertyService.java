/*This class contains all the methods of the Property Service which we have to implement. It is an abstract class*/
package com.apartmentReservation.service;

import java.util.List;
import java.util.Optional;

import com.apartmentReservation.model.Properties;

public interface PropertyService {
	
	public List<Properties> findAllProperties();

	public Properties findProperty(Integer id);
	
	public void updateLeaseinfo(Integer id);
	
	public List<Properties> findPropertybyOwner(Integer id);
	
	public List<Properties> findPropertyByAptSearchPreferencesCriteria(String propertyType, int propertyBedrooms,
			int propertyBath, String propertyLocation,int propertyRent,int propertyPet, int propertyUtility); 
    
	public List<Properties> displayFeaturedProperties();
}
