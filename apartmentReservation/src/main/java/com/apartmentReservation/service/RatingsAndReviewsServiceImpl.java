package com.apartmentReservation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apartmentReservation.model.RatingsAndReviews;
import com.apartmentReservation.repository.RatingsAndReviewsRepository;


@Service("ratingsAndReviewsService")
public class RatingsAndReviewsServiceImpl implements RatingsAndReviewsService {
	@Autowired
	private RatingsAndReviewsRepository ratingsAndReviewsRepository;
	
	/**
	 * Description:This methods contacts the repository and fetches all related records from reviews table for the specific 
	 * propertyName.
	 * @param propertyName
	 * @return List : a list of all ratings and reviews for the specific property.
	 * @author Anoop Sai Vengala
	 * Written on : November 08, 2018
	 */
	@Override
	public List<RatingsAndReviews> findRatingsAndReviewsByPropertyName(String propertyName) {
		List<RatingsAndReviews> ratingsAndReviewsByPropertyNameList = ratingsAndReviewsRepository.findByReviewPropertyName(propertyName);
		return ratingsAndReviewsByPropertyNameList;
	}

	/**
	 * Description: This method stores the RatingsAndReviews bean in the reviews table.
	 * @param RatingsAndReviews : a java object containing all related information to be stored as a record
	 * in the reviews table.
	 * @return RatingsAndReviews
	 * @author Anoop Sai Vengala
	 * Written on : November 08, 2018
	 */
	@Override
	public RatingsAndReviews addNewReview(RatingsAndReviews ratingsAndReviews) {
		return ratingsAndReviewsRepository.save(ratingsAndReviews);
	}
}
