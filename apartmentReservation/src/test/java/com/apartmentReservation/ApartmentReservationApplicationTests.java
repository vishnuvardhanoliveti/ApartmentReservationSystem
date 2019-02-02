//Description: To test the main class of springboot 
package com.apartmentReservation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApartmentReservationApplicationTests {
	// Testing the main method
	@Test
	public void main() {
		ApartmentReservationApplication.main(new String[] {});
	}

}
