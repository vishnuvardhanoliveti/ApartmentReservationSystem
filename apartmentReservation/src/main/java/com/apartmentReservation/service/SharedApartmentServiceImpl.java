package com.apartmentReservation.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apartmentReservation.model.Properties;
import com.apartmentReservation.model.SharedApartment;
import com.apartmentReservation.repository.PropertyRepository;
import com.apartmentReservation.repository.SharedApartmentsRepository;


@Service("sharedApartmentsService")
public class SharedApartmentServiceImpl implements SharedApartmentService {
	@Autowired
	private SharedApartmentsRepository sharedApartmentsRepository;
	
	@Autowired
	private PropertyRepository propertyRepository;
	
	/**
	 * Description:This method is used to add the property to sharedApartment table.			   
	 * @Input:The input expected is the  the sharedApartment object . 
	 * @return : This method returns sharedApartment object which is saved.
	 * @author : Vishnu Vardhan Oliveti
	 * Written on:November 20, 2018
	 * Modified on: November 23, 2018
	 */
	@Override
	public SharedApartment addToSharedApartment(SharedApartment sharedApartment) {
		// TODO Auto-generated method stub
		return sharedApartmentsRepository.save(sharedApartment);
	}
	
	/**
	 * Description:This method is used to find all shared properties.			   
	 * @Input:na . 
	 * @return : This method returns all the shared properties.
	 * @author : Vishnu Vardhan Oliveti
	 * Written on:November 20, 2018
	 * Modified on: November 23, 2018
	 */
	@Override
	public List<SharedApartment> findAllSharedProperties(){
		return sharedApartmentsRepository.findAll();
	}
	
	/**
	 * Description:This method is used to add the leased property details to the matched roommmate results while searching for shared properties.			   
	 * @Input:The input expected is the  matched results for roommates . 
	 * @return : This method returns the matched resulst of roommates by adding the leased property details as well.
	 * @author : Vishnu Vardhan Oliveti
	 * Written on:November 20, 2018
	 * Modified on: November 23, 2018
	 */
	@Override
	public Map<Integer, List<Map<String, String>>> addPropertyDetailsToResults(
			Map<Integer, List<Map<String, String>>> matchedResults) {
		Map<Integer,List<Map<String,String>>> collect = new TreeMap<Integer, List<Map<String,String>>>(Collections.reverseOrder());
		matchedResults.forEach((k,v)-> {
			List<Map<String,String>> newList =new ArrayList<Map<String,String>>();
			
			//For each matched result we are checking the matched email is present in sharedApartment table, if so adding the property details to the matched roommate result
			v.forEach(map->{
				Map<String,String> newMap = new HashMap<String,String>();
				String email = map.get("matchedEmail");
				SharedApartment roommateRequesterDetails = new SharedApartment();
				roommateRequesterDetails =	sharedApartmentsRepository.findByUserEmail(email);
				if(roommateRequesterDetails == null) {
					System.out.println(map.get("userId")+ "did not request for roommate");
				}else {
					map.forEach((key,value)->{
						newMap.put(key, value);
					});
					
					//Adding the leased property details to object.
					Properties property=propertyRepository.findByPropertyId(roommateRequesterDetails.getPropertyId());
					newMap.put("propertyId",Integer.toString( property.getPropertyId()));
					newMap.put("propertyName", property.getPropertyName());
					newMap.put("apartmentNumber", Integer.toString(property.getAptNumber()));
					newMap.put("propertyLocation", property.getPropertyLocation());
					newMap.put("propertyAddress", property.getPropertyAddress());
					newMap.put("propertyName", property.getPropertyName());
					newMap.put("propertyOwner", Integer.toString(property.getPropertyOwner()));
					newMap.put("propertyImagePath", property.getPropertyImagePath());
					newMap.put("propertyType", property.getPropertyType());
					newMap.put("propertyBedrooms", Integer.toString(property.getPropertyBedrooms()));
					newMap.put("propertyBath", Integer.toString(property.getPropertyBath()));
					newMap.put("propertyUtility", property.getPropertyUtility()==1?"yes":"no");
					newMap.put("propertyRent", Integer.toString(property.getPropertyRent()));
					newMap.put("propertyDeposit", Integer.toString(property.getPropertyDeposit()));
					newMap.put("propertyDescription", property.getPropertyDescription());



					newList.add(newMap);

				}
				});
			if(newList.size()>0)
			collect.put(k, newList);
			
		});
		return collect;
	}

}
