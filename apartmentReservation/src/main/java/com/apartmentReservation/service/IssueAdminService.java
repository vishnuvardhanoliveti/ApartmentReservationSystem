package com.apartmentReservation.service;

import java.util.List;


import com.apartmentReservation.model.Issue;

public interface IssueAdminService {
	public List<Issue> findAllApplications();
	public Issue updateStatus(int id, String status);
	public Issue findById(int id);
	public Integer saveDetails(int id, String status, String date);
	public List<Issue> findByPropertyOwnerId(Integer id);
	
}
