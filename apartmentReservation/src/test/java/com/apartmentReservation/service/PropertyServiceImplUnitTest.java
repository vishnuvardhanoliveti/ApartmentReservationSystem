/*Description: This class is used to test various cases such as retrieving all properties,find property by a given id, 
retrieving featured properties,retrieving properties of a owner,retrieving selected properties*/
package com.apartmentReservation.service;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.web.servlet.ModelAndView;

import com.apartmentReservation.model.Properties;
import com.apartmentReservation.model.User;
import com.apartmentReservation.repository.PropertyRepository;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class PropertyServiceImplUnitTest {
	@Mock
	private EntityManager entityManager;
	@Mock
	private PropertyRepository propertyRepository;
	@Mock
	private EntityManager fakeEntityManager;
	@Mock
	private CriteriaBuilder fakeCriteriaBuilder;
	@Mock
	private CriteriaQuery<Properties> fakeCriteriaQuery;
	@Mock
	Root<Properties> property;
	@Mock
	TypedQuery<Properties> typedQuery;
	List<Properties> properties = new ArrayList<>();
	@InjectMocks
	private PropertyServiceImpl propertyServiceImpl;

	public void propertiesBuilder(int propertyId, int apt_number, int property_bath, int propery_bedrooms,
			int property_rent, int property_deposit, int property_utility, int property_pets, int property_owner,
			Boolean property_isfeatured, String property_type, String property_name, String property_location,
			String property_address) {
		Properties property = Properties.builder().propertyId(propertyId).aptNumber(apt_number)
				.propertyBath(property_bath).propertyBedrooms(propery_bedrooms).propertyRent(property_rent)
				.propertyDeposit(property_deposit).propertyUtility(property_utility).propertyPets(property_pets)
				.propertyOwner(property_owner).isFeatured(property_isfeatured).propertyType(property_type)
				.propertyName(property_name).propertyLocation(property_location).propertyAddress(property_address)
				.build();
		properties.add(property);
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		propertiesBuilder(1, 1, 1, 1, 650, 50, 0, 0, 1, false, "Studio", "Hope Apartments", "Trust Drive, Denton",
				"Trust Drive");
		propertiesBuilder(2, 2, 1, 1, 700, 50, 0, 0, 1, false, "Studio", "Oak Glen", "214 North Texas Blvd, Denton",
				" North Texas Blvd");
		propertiesBuilder(3, 3, 1, 2, 900, 100, 1, 0, 2, false, "Studio", "Double Tree", "100 Avenue D, Denton",
				" Avenue D");
		propertiesBuilder(4, 4, 1, 2, 900, 100, 1, 0, 2, false, "Apartment", "Rayzor Creek", "Bonnie Brie, Denton",
				"Bonnie Brie");
		propertiesBuilder(5, 5, 1, 2, 1000, 100, 1, 0, 2, false, "Apartment", "University Manor", "Ave G, Denton",
				"Ave G");
		propertiesBuilder(6, 6, 2, 2, 1200, 100, 1, 1, 3, true, "Apartment", "Hope Apartments", "Trust Drive, Denton",
				"Trust Drive");
		propertiesBuilder(7, 7, 2, 2, 1200, 100, 1, 1, 3, true, "Apartment", "University Manor", "Ave G, Denton",
				"Ave G");
		propertiesBuilder(8, 8, 2, 3, 1500, 100, 1, 1, 3, true, "Apartment", "Oak Glen", "214 North Texas Blvd, Denton",
				" North Texas Blvd");
		propertiesBuilder(9, 9, 2, 3, 1500, 100, 1, 1, 3, true, "Duplex", "Rayzor Creek", "Bonnie Brie, Denton",
				"Bonnie Brie");
		propertiesBuilder(10, 10, 2, 4, 2500, 200, 1, 1, 4, true, "Duplex", "Oak Glen", "214 North Texas Blvd, Denton",
				" North Texas Blvd");
	}

	/*
	 * Description: Test for retrieving all properties 
	 * input: - when properties tab is clicked 
	 * output: checking number of properties displayed
	 */
	@Test
	public void testFindAllProperties() {
		when(propertyRepository.findAll()).thenReturn(properties);
		List<Properties> actual = propertyServiceImpl.findAllProperties();
		assertEquals(10, actual.size());
	}

	// Description: Test for retrieving a property by its id
	// input: property id
	// output: checking the property rent
	@Test
	public void testfindProperty() {
		// propertyRepository.findByPropertyId(id);
		when(propertyRepository.findByPropertyId(10)).thenReturn(properties.get(9));
		Properties property = propertyServiceImpl.findProperty(10);
		assertEquals(2500, property.getPropertyRent());
	}

	/*
	 * Description: Test for retrieving all properties which are in featured category 
	 * input: -when home page is accessed 
	 * output: checking number of properties displayed
	 */
	@Test
	public void testdisplayFeaturedProperties() {
		List<Properties> featuredProperties = properties.stream().filter(property -> property.isFeatured())
				.collect(Collectors.toList());
		when(propertyRepository.findByIsFeatured(true)).thenReturn(featuredProperties);
		List<Properties> actualProperties = propertyServiceImpl.displayFeaturedProperties();
		assertEquals(5, actualProperties.size());
	}

	/*
	 * Description: Test for retrieving all properties of an owner 
	 * input: property id 
	 * output: checking number of properties displayed
	 */
	@Test
	public void testfindPropertybyOwner() {
		List<Properties> filteredProperties = properties.stream().filter(property -> property.getPropertyOwner() == 3)
				.collect(Collectors.toList());
		when(propertyRepository.findByPropertyOwner(3)).thenReturn(filteredProperties);
		List<Properties> actualProperties = propertyServiceImpl.findPropertybyOwner(3);
		assertEquals(4, actualProperties.size());
	}

	/*
	 * Description: Test for retrieving all properties based on search preferences provided. 
	 * input: all the search preferences 
	 * output: checking number of properties displayed
	 */
	@Test
	public void testfindPropertyByAptSearchPreferencesCriteria() {
		List<Properties> filteredPropForApt = properties.stream()
				.filter(property -> property.getPropertyType().equals("Apartment")).collect(Collectors.toList());
		when(entityManager.getCriteriaBuilder()).thenReturn(fakeCriteriaBuilder);
		when(fakeCriteriaBuilder.createQuery(Properties.class)).thenReturn(fakeCriteriaQuery);
		when(fakeCriteriaQuery.from(Properties.class)).thenReturn(property);
		when(entityManager.createQuery(fakeCriteriaQuery)).thenReturn(typedQuery);
		when(typedQuery.getResultList()).thenReturn(filteredPropForApt);
		List<Properties> actual = propertyServiceImpl.findPropertyByAptSearchPreferencesCriteria("Apartment", 0, 0,
				"Select", 0, 2, 2);
		assertEquals(5, actual.size());
	}
}
