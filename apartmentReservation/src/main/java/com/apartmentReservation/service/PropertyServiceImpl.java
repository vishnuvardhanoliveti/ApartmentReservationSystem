/*This class handles all the services related to the Properties like displaying featured properties, all properties, properties based on preferences*/
package com.apartmentReservation.service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.apartmentReservation.model.Properties;
import com.apartmentReservation.repository.PropertyRepository;

@Service("propertyService")
public class PropertyServiceImpl implements PropertyService {
	@PersistenceContext
    private EntityManager entityManager;
	@Autowired
	private PropertyRepository propertyRepository;

    //This method to used to fetch all properties.
	@Override
	public List<Properties> findAllProperties() {
		// TODO Auto-generated method stub
		return propertyRepository.findAll();
	}
     
	//This method is used to fetch a property by property id
	@Override
	public Properties findProperty(Integer id) {
		return propertyRepository.findByPropertyId(id);
		//return null;
	}

	//This  method is used to fetch the properties based on user property search preferences.
	@Override
	public List<Properties> findPropertyByAptSearchPreferencesCriteria(String propertyType, int propertyBedrooms,
			int propertyBath, String propertyLocation,int propertyRent,int propertyPet, int propertyUtility) {
		
		  
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<Properties> createQuery = criteriaBuilder.createQuery(Properties.class);
			Root<Properties> property = createQuery.from(Properties.class);
			List<Predicate> criteriaList = new ArrayList();
			
			if(!propertyType.equals("Select")) {
				 Predicate typePredicate = criteriaBuilder.equal(property. get("propertyType"), propertyType);
                 
					criteriaList.add(typePredicate);
			}
			
			if(!propertyLocation.equals("Select")) {
				 Predicate locationPredicate = criteriaBuilder.equal(property. get("propertyLocation"), propertyLocation);
                
					criteriaList.add(locationPredicate);
			}
			
			if(propertyBedrooms!=0) {
				 Predicate bedroomsPredicate = criteriaBuilder.equal(property. get("propertyBedrooms"), propertyBedrooms);
               
					criteriaList.add(bedroomsPredicate);
			}
			if(propertyBath!=0) {
				 Predicate bathPredicate = criteriaBuilder.equal(property. get("propertyBath"), propertyBath);
              
					criteriaList.add(bathPredicate);
			}
			
			if(propertyRent!=0) {
				 Predicate rentPredicate = criteriaBuilder.equal(property. get("propertyRent"), propertyRent);
	              
				criteriaList.add(rentPredicate);
			}
			
			if(propertyUtility!=2) {
				Predicate utilityPredicate = criteriaBuilder.equal(property. get("propertyUtility"), propertyUtility);
				criteriaList.add(utilityPredicate);
			}
	              
			
			if(propertyPet!=2) {
				 Predicate petPredicate = criteriaBuilder.equal(property. get("propertyPets"), propertyPet);
				 criteriaList.add(petPredicate);
			}
					       
					   

			createQuery.select(property);
			createQuery.where(criteriaBuilder.and(criteriaList.toArray(new Predicate[0])));
			TypedQuery<Properties> typedQuery = entityManager.createQuery(createQuery);
			List<Properties> propertiesList = typedQuery.getResultList();
			return propertiesList;		
	}
	//This method is used to display featured properties
	@Override
	public List<Properties> displayFeaturedProperties() {
		return propertyRepository.findByIsFeatured(true);
		//return null;
	}
	
	//This method is used to display all properties of an owner by passing his id
	@Override
	public List<Properties> findPropertybyOwner(Integer id) {
		// TODO Auto-generated method stub
		return propertyRepository.findByPropertyOwner(id);
		
	}

	@Override
	public void updateLeaseinfo(Integer id) {
		// TODO Auto-generated method stub
		 propertyRepository.updateLeaseinfo(id);
		
	}
	
}
