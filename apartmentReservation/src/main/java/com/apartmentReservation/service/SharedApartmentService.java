package com.apartmentReservation.service;


import java.util.List;
import java.util.Map;

import com.apartmentReservation.model.SharedApartment;

public interface SharedApartmentService {
	public SharedApartment addToSharedApartment(SharedApartment sharedApartment );

	public Map<Integer, List<Map<String, String>>> addPropertyDetailsToResults(
			Map<Integer, List<Map<String, String>>> matchedResults);

	public List<SharedApartment> findAllSharedProperties();
}
