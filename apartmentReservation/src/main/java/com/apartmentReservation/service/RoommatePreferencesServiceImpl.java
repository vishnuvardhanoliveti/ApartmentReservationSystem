/*This class handles services related to the RoommatePreferences like setting the customer  roommatePrefernces details based on the values provided by the customer in the RoommatePreferences form, finding roommatePreferences by userId*/

package com.apartmentReservation.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Change;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.apartmentReservation.model.Requests;
import com.apartmentReservation.model.RoommatePreferences;
import com.apartmentReservation.model.User;
import com.apartmentReservation.repository.BackgroundVerificationAndLeaseAdminRepository;
import com.apartmentReservation.repository.RoommatePreferencesRepository;
import com.apartmentReservation.repository.UserRepository;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;
@Service
public class RoommatePreferencesServiceImpl implements RoommatePreferencesService {
	@Autowired
	RoommatePreferencesRepository roommatePreferencesRepository;

	 @Autowired
	 SendRequestsService sendRequestsService;
	@Autowired
	BackgroundVerificationAndLeaseAdminRepository backgroundVerificationAndLeaseAdminRepository;
	static int matchCounter=1;
	static int matchedScore = 0;
	
	/**
	 * Description:This method is used to get the RoommatePreferences object from database by  passing userId as input.			   
	 * @Input:The input expected is the userId . 
	 * @return : This method returns a RoommatePreference object from the database.
	 * @author : Vishnu Vardhan Oliveti
	 * Written on:Novemebr 2, 2018
	 * Modified on: November 6, 2018
	 */
	@Override
	public RoommatePreferences findRoommatePreferencesById(int id) {
		return roommatePreferencesRepository.findByUserId(id);
		
	}
	
	/**
	 * Description:This method is used to set the RoommatePreferences of a customer.			   
	 * @Input:The input expected is the  the roommatePreferences object posted from the form . 
	 * @return : This method returns a RoommatePrefernce object saved from the database.
	 * @author : Vishnu Vardhan Oliveti
	 * Written on:November 2, 2018
	 * Modified on: November 6, 2018
	 */
	@Override
	public RoommatePreferences saveUserRoommatePreferences(RoommatePreferences roommatePreferences) {
		// TODO Auto-generated method stub
		return roommatePreferencesRepository.save(roommatePreferences) ;
	}
	
	/**
	 * Description:This method is used to set the RoommatePreferences of a customer.			   
	 * @Input:The input expected is the  the roommatePreferences object posted from the form, the name of the search like roommatesearch or sharedpropertysearch . 
	 * @return : This method returns matched preferences from the database.
	 * @author : Vishnu Vardhan Oliveti
	 * Written on:November 20, 2018
	 * Modified on: November 23, 2018
	 */

	@Override
	public Map<Integer, List<Map<String, String>>> getMatchedPreferences(RoommatePreferences roommatePreferences, String module) {
		//Setting values from roommatePreferences object to variables.
		int actualAge=roommatePreferences.getAge();
		String agePreferences = roommatePreferences.getAgeGroup();
		String[] result = agePreferences.split("-");
		int lowerBoundAgePref =Integer.parseInt(result[0]);
		int upperBoundAgePref =Integer.parseInt(result[1]);
		String actualGender=roommatePreferences.getActualGender();
		String genderPref = roommatePreferences.getGender();
		
		String locationPref = roommatePreferences.getLocation();
		Boolean isVegan = roommatePreferences.isVegan();
		Boolean isSmoking =roommatePreferences.isSmoking();
		Boolean isDrinking = roommatePreferences.isDrinking();
		Boolean hasPets = roommatePreferences.isPets();
		int userId = roommatePreferences.getUserId();
		String userEmail=roommatePreferences.getUserEmail();
		
		//getting matched preferences fromdatabase
		List<RoommatePreferences> matchedPreferences =  roommatePreferencesRepository.getMatchedResults(genderPref,lowerBoundAgePref, upperBoundAgePref,locationPref,isVegan, isSmoking, isDrinking, hasPets,userId);
		
		//Map<Integer,Map<String,String>> dataforMatchedPreferences = new HashMap<Integer,Map<String,String>>();
		Map<Integer,List<Map<String,String>>> map = new TreeMap<Integer, List<Map<String,String>>>(Collections.reverseOrder());
		
		
		
		//Multimap<Integer, String> map = TreeMultimap.create(Ordering.natural().reverse(), Ordering.natural());
		
		//Creating two lists one for the logged customers roommate preferences and other the matched customer roommate preferences to build the matched and unmatched preferences
		
		RoommatePreferences listOne = RoommatePreferences
				 							.builder()
				 							.gender(roommatePreferences.getGender())
				 							.location(roommatePreferences.getLocation())
				 							.vegan(roommatePreferences.isVegan())
				 							.smoking(roommatePreferences.isSmoking())
				 							.drinking(roommatePreferences.isDrinking())
				 							.pets(roommatePreferences.isPets())				 							
				 							.build();
		
		Javers javers = JaversBuilder.javers().build();
		
		 
		matchedPreferences.forEach(match->{
			
			 RoommatePreferences listTwo = RoommatePreferences
						.builder()
						.gender(match.getActualGender())
						.location(match.getLocation())
						.vegan(match.isVegan())
						.smoking(match.isSmoking())
						.drinking(match.isDrinking())
						.pets(match.isPets())					
						.build();
			
			
			 Map<String,String> preferencesMap = new HashMap<String, String>();
			 Diff diff = javers.compare(listOne, listTwo);
			 int size = diff.getChangesByType(ValueChange.class).size();
			 
			  List<String> propertiesList = Arrays.asList("gender","vegan","drinking","smoking","pets","location","agePreferences");
			  List<String> diffPropertiesList = new ArrayList<String>() ;
			  List<String> matchedPropertiesList = new ArrayList<String>();
			  
			  //finding the diff prefeernces and adding to diffPropertiesList
			 for(int i=0;i<size;i++) {
				 ValueChange change = diff.getChangesByType(ValueChange.class).get(i);
				 diffPropertiesList.add(change.getPropertyName());
				 //preferencesMap.put("actual"+change.getPropertyName()+"Preference",String.valueOf(change.getLeft()));
				 //preferencesMap.put("matched"+change.getPropertyName()+"Preference",String.valueOf(change.getRight()));
				 
			 }
			 
			 int matchedAge=match.getAge();
			 if(!(matchedAge>= lowerBoundAgePref && matchedAge <= upperBoundAgePref)) {
				 diffPropertiesList.add("agePreferences");
				
			 }
			 
			 //finding the matched prefeernces and adding to matchedPropertiesList
			 propertiesList.forEach(property->{
				 if(!(diffPropertiesList.contains(property))){
					 matchedPropertiesList.add(property);
				 }
			 });
			 
			 //calculating the matched score based on no. of preferences matched
			 matchedScore= matchedPropertiesList.size();
			 String score=String.valueOf(matchedScore);
			 //System.out.println(matchedScore);			
						
				matchedPropertiesList.forEach(res->{
					preferencesMap.put(res, "matched");							
				});
				
				diffPropertiesList.forEach(res->preferencesMap.put(res, "notMatched"));
				
				//Adding additional attributes for matched user identification
				preferencesMap.put("matchedactualAge",String.valueOf(matchedAge));
				preferencesMap.put("matchedActualGender",match.getActualGender());
				preferencesMap.put("matchedEmail",match.getUserEmail());
				preferencesMap.put("matchedScore",score);
				preferencesMap.put("userId",String.valueOf( match.getUserId()));
				if(userEmail!=null)
				{
					Requests in=sendRequestsService.checkIfAlreadyRequestSent(userEmail, match.getUserEmail());
					
					if(in!=null)
					{
						preferencesMap.put("alreadyRequestSent","yes");
					}
					else
					{
						preferencesMap.put("alreadyRequestSent","no");
					}
				}
				if(map.containsKey(matchedScore)) {
					List<Map<String,String>> localList = map.get(matchedScore);
					localList.add(preferencesMap);
					map.put(matchedScore,localList);
				}else {
					List<Map<String,String>> localList = new ArrayList<Map<String,String>>();
					localList.add(preferencesMap);
					map.put(matchedScore, localList);
				}

				//dataforMatchedPreferences.put(matchCounter,preferencesMap);
				//map.put(matchedScore,  preferencesMap.toString());
				
				matchCounter++;
			
		});
		
		
		//System.out.println(dataforMatchedPreferences.size());
		
		Map<Integer,List<Map<String,String>>> collect = new TreeMap<Integer, List<Map<String,String>>>(Collections.reverseOrder());
		
		List<String> leasedUserEmails = new ArrayList<String>();
		backgroundVerificationAndLeaseAdminRepository.findAll().forEach(res->leasedUserEmails.add(res.getEmail()));
		
		//If request came from newRoommateSearch module only the matched results which are non-leased users is sent
		
		if(module.equals("newRoommateSearch")) {
			
			map.forEach((k,v)-> {
				List<Map<String,String>> newList =new ArrayList<Map<String,String>>();
				
				v.forEach(res->{
					String email = res.get("matchedEmail");
					if(leasedUserEmails.contains(email)) {
						System.out.println("Dropping from list leased users");
					}else {
						newList.add(res);
						
					}
					});
				if(newList.size()>0)
				collect.put(k, newList);
				
			});
		
					
		
		}else if(module.equals("fetchAll")) {
			map.forEach((k,v)-> {
				List<Map<String,String>> newList =new ArrayList<Map<String,String>>();
				
				v.forEach(res->{
					newList.add(res);
				});
				if(newList.size()>0)
				collect.put(k, newList);
			});
		}
		
		//If request came from sharedPropertySearch module only the matched results which are leased users   is sent
		else {
			map.forEach((k,v)-> {
				List<Map<String,String>> newList =new ArrayList<Map<String,String>>();
				
				v.forEach(res->{
					String email = res.get("matchedEmail");
					if(leasedUserEmails.contains(email)) {
						newList.add(res);
					}
						
					else {
						System.out.println("Dropping from list the unleased user:"+res.get("userId"));
						
					}
					});
				if(newList.size()>0)
				collect.put(k, newList);
		});
		}
		/*map.forEach((k,v)->{
			System.out.println(k+":"+v);
		});
		System.out.println("filtered map");
		collect.forEach((k,v)->{
			System.out.println(k+":"+v);
		});*/
		return collect;
	}

}
