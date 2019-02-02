//This class handles the WebMVC stuff which helps to create views and models
package com.apartmentReservation.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
 //In this method the password set by the user while registering is encrypted and returned.
 @Bean
 public BCryptPasswordEncoder passwordEncoder() {
  BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
  return bCryptPasswordEncoder;
 }
}