/*This class contains all the methods of the IssueUser Service which we have to implement. It is an abstract class

 * @author  Devan Tarun Kumar
 * Written on:November 1,2018
 */


package com.apartmentReservation.service;


import java.util.List;

import com.apartmentReservation.model.Issue;

public interface IssueUserService {

	 public Issue saveIssue(Issue issue);
	 public List<Issue> findByIssueUserEmail(String email);
}
