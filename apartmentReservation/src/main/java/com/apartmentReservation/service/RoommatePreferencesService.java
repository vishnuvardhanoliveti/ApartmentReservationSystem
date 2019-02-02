/*This class contains all the methods of the RoommatePreferences Service which we have to implement. It is an abstract class

 * @author  Vishnu Vardhan Oliveti
 * Written on:November 2,2018
 */

package com.apartmentReservation.service;

import java.util.List;
import java.util.Map;

import com.apartmentReservation.model.RoommatePreferences;
import com.google.common.collect.Multimap;

public interface RoommatePreferencesService {
public RoommatePreferences findRoommatePreferencesById(int id);
public RoommatePreferences saveUserRoommatePreferences(RoommatePreferences roommatePreferences);
public Map<Integer, List<Map<String, String>>> getMatchedPreferences(RoommatePreferences roommatePreferences,String module);
}
