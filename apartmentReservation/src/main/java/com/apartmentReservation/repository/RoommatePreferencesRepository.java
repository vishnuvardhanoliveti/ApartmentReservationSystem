//This class handles all the database operations related to the table roommate_preferences*/
package com.apartmentReservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.apartmentReservation.model.RoommatePreferences;


import java.util.List;

import javax.transaction.Transactional;
public interface RoommatePreferencesRepository extends JpaRepository<RoommatePreferences, Long>{

	/**
	 * Description:This method is used to find roommatePrefernces of a customer  from  database .			   
	 * @Input:The input expected is the  userId . 
	 * @return : Returns RoommatePrefernces object as output.
	 * @author : Vishnu Vardhan Oliveti
	 * Written on:November 2, 2018
	 * Modified on: Novemeber 6, 2018
	 */
	RoommatePreferences findByUserId(int id);
	
	/**
	 * Description:This method is used to find matched roommatePrefernces of a customer  from  database .			   
	 * @Input:The input expected is the customer's roommmate preferences . 
	 * @return : Returns matched RoommatePrefernces object as output.
	 * @author : Vishnu Vardhan Oliveti
	 * Written on:November 23, 2018
	
	 */
	@Transactional
	 @Query("SELECT p FROM RoommatePreferences p where p.userId!=?9 and (p.actualGender = ?1 or (p.age >= ?2 and p.age <= ?3) or p.location = ?4 or p.vegan =?5 or p.smoking = ?6 or p.drinking = ?7 or p.pets =?8) ")
	//@Query("SELECT p FROM RoommatePreferences p where p.userId!=?1  ")
	//List<RoommatePreferences> getMatchedResults(String genderPref,Integer lowerBoundAgePref, Integer upperBoundAgePref,String locationPref,Boolean isVegan,Boolean isSmoking, Boolean isDrinking, Boolean hasPets,int userId);
	List<RoommatePreferences> getMatchedResults(String genderPref,Integer lowerBoundAgePref, Integer upperBoundAgePref,String locationPref,Boolean isVegan,Boolean isSmoking, Boolean isDrinking, Boolean hasPets,Integer userId);

}
