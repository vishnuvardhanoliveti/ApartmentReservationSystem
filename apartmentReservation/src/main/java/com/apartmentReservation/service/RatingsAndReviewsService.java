package com.apartmentReservation.service;

import java.util.List;

import com.apartmentReservation.model.RatingsAndReviews;


public interface RatingsAndReviewsService {
	public List<RatingsAndReviews> findRatingsAndReviewsByPropertyName(String propertyName);
	public RatingsAndReviews addNewReview(RatingsAndReviews ratingsAndReviews);
}
