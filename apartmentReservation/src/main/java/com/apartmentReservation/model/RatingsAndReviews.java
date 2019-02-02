package com.apartmentReservation.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "reviews")
public class RatingsAndReviews {
	@Id
	@Column(name = "review_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name = "review_property_name")
	private String reviewPropertyName;
	
	@Column(name = "review_user_name")
	private  String reviewUserName;
	
	@Column(name = "review_rating")
	private double reviewRating;
	
	@Column(name = "review_comment")
	private String reviewComment;
}
