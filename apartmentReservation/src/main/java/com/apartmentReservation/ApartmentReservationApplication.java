//Main class where SpringBoot is initialized
package com.apartmentReservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//Main method for Initiating SPring boot

@SpringBootApplication
public class ApartmentReservationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApartmentReservationApplication.class, args);
	}
}
