package com.apartmentReservation.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.apartmentReservation.model.RatingsAndReviews;
import com.apartmentReservation.repository.RatingsAndReviewsRepository;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)

public class RatingsAndReviewsServiceImplTest {
	@Mock
	private RatingsAndReviewsRepository ratingsAndReviewsRepository;
	
	@InjectMocks
	private RatingsAndReviewsServiceImpl ratingsAndReviewsServiceImpl;
	
	/**
	 * Description : Setup all mocks to run JUnit tests.
	 * @author Anoop Sai Vengala
	 * Written on : November 09, 2018
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	RatingsAndReviews mockedRatingsAndReviews = RatingsAndReviews.builder()
											.id(3)
											.reviewPropertyName("Oak Glen")
											.reviewComment("Test Comment")
											.reviewRating(4.0)
											.reviewUserName("Dhoni MS")
											.build();
	
	/**
	 * Description: This method adds reviews to a list and return it.
	 * @return List A list of reviews used for mocking to test methods.
	 * @author Anoop Sai Vengala
	 * Written on : November 09, 2018
	 */
	private List<RatingsAndReviews> createReviewsList(){
		List<RatingsAndReviews> listRatings = new ArrayList<RatingsAndReviews>();
		listRatings.add(mockedRatingsAndReviews);
		return listRatings;
	}
	
	/**
	 * Description: This method tests if the ratings and reviews object is persisted correctly.
	 * @author Anoop Sai Vengala
	 * Written on : November 09, 2018
	 */
	@Test
	public void testaddNewReview() {
		when(ratingsAndReviewsRepository.save(mockedRatingsAndReviews)).thenReturn(mockedRatingsAndReviews);
		RatingsAndReviews ratingsAndReviews = ratingsAndReviewsServiceImpl.addNewReview(mockedRatingsAndReviews);
		assertEquals("Oak Glen", ratingsAndReviews.getReviewPropertyName());
		assertEquals("Test Comment", ratingsAndReviews.getReviewComment());
		assertEquals(mockedRatingsAndReviews,ratingsAndReviews);
	}
	
	/**
	 * Description: This method tests if the ratings and reviews are properly retrieved.
	 * @author Anoop Sai Vengala
	 * Written on : November 09, 2018
	 */
	@Test
	public void testfindRatingsAndReviewsByPropertyName() {
		when(ratingsAndReviewsRepository.findByReviewPropertyName("Oak Glen")).thenReturn(createReviewsList());
		List<RatingsAndReviews> ratingsAndReviewsList = ratingsAndReviewsServiceImpl.findRatingsAndReviewsByPropertyName("Oak Glen");
		assertEquals(createReviewsList().size(),ratingsAndReviewsList.size());
		assertEquals(createReviewsList(), ratingsAndReviewsList);
	}
}
