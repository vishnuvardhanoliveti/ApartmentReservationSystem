package com.apartmentReservation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apartmentReservation.model.RatingsAndReviews;


@Repository("ratingsAndReviewsRepository")
public interface RatingsAndReviewsRepository extends JpaRepository<RatingsAndReviews,Long> {
	/**
	 * Description : This method finds all records in the reviews table with matching property name.
	 * @param propertyName
	 * @return List A list of ratings and reviews record for the service to apply business logic on.
	 * @author Anoop Sai Vengala
	 * Written on: November 08, 2018
	 */
	public List<RatingsAndReviews> findByReviewPropertyName(String propertyName);
}
