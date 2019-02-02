/*This class handles all the database operations related to the table issues
 * @author : Shaline Sreemati Kocherla, Devan Tarun Kumar
 * Written on:November 1, 2018
 * Modified on: Novemeber 6, 2018
*/

package com.apartmentReservation.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.apartmentReservation.model.Issue;

@Repository("issueRepository")
public interface IssueRepository extends JpaRepository<Issue, Long> {
	@Query("SELECT i FROM Issue i WHERE  i.issueUserEmail =(:email)")
	public List<Issue> findByIssueUserEmail(@Param("email") String email);
	
	 Issue findByIssueId(int id);
		
		public List<Issue> findAll();
		
		public List<Issue> findByPropertyOwner(Integer id);
			
			@Transactional
			 @Modifying
			@Query("update Issue i set i.issueStatus = ?1, i.issueSolvedDate = ?3  where i.issueId = ?2"   )
			Integer saveIssueDetails(String status, int id, String date);
	 
}
