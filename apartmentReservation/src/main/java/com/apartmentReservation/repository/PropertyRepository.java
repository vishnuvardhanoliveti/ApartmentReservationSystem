//This class handles all the database operations related to table properties.
package com.apartmentReservation.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.apartmentReservation.model.Properties;


@Repository("propertiesRepository")
public interface PropertyRepository extends JpaRepository<Properties, Integer> {
	@Query("SELECT p FROM Properties p WHERE p.propertyType = (:propertyType) and p.propertyLocation=(:propertyLocation) and p.propertyBedrooms = (:propertyBedrooms) and p.propertyBath = (:propertyBath)")
	public List<Properties> find(@Param("propertyType") String propertyType,
			@Param("propertyLocation") String propertyLocation, @Param("propertyBedrooms") Integer propertyBedrooms,
			@Param("propertyBath") Integer propertyBath);

	Properties findByPropertyId(int propertyId);

	 @Transactional
	 @Modifying
	 @Query("update Properties p set p.Leased ='1' where p.id = ?1"   )
	void updateLeaseinfo(Integer id);
	 
	public List<Properties> findByIsFeatured(Boolean val);

	public List<Properties> findByPropertyOwner(int id);
}
