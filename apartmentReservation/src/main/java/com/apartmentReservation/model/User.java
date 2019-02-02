/*This class has getters and setters of the User attributes. It is related to the user table.*/
package com.apartmentReservation.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "user")

public class User {

 @Id
 @Column(name = "user_id")
 @GeneratedValue(strategy = GenerationType.AUTO)
 private int id;
 
 @Column(name = "email")
 private String email;
 
 @Column(name = "first_name")
 private String firstName; 
 
 @Column(name = "last_name")
 private String lastName;
 
 @Column(name = "password")
 private String password;
 
 @Column(name = "active")
 private int active;
 
 @Column(name = "phone_number")
 private String phoneNumber;
 
 @Column(name = "address_line_1")
 private String addressLine1;
 
 @Column(name = "address_line_2")
 private String addressLine2;
 
 @Column(name = "city")
 private String city;
 
 @Column(name = "state")
 private String state;
 
 @Column(name = "zip")
 private String zip;
 
 @Column(name ="dob")
 @DateTimeFormat(pattern = "yyyy-MM-dd")
 private String dob;
 
 @Column(name ="gender")
 private String gender;
 
 @ManyToMany(cascade=CascadeType.ALL)
 @JoinTable(name="user_role", joinColumns=@JoinColumn(name="user_id"), inverseJoinColumns=@JoinColumn(name="role_id"))
 private Set<Role> roles;



 public void setRoles(Set<Role> roles) {
  this.roles = roles;
  
 }



public int getId() {
	return id;
}



public void setId(int id) {
	this.id = id;
}



public String getEmail() {
	return email;
}



public void setEmail(String email) {
	this.email = email;
}



public String getFirstName() {
	return firstName;
}



public void setFirstName(String firstName) {
	this.firstName = firstName;
}



public String getLastName() {
	return lastName;
}



public void setLastName(String lastName) {
	this.lastName = lastName;
}



public String getPassword() {
	return password;
}



public void setPassword(String password) {
	this.password = password;
}



public int getActive() {
	return active;
}



public void setActive(int active) {
	this.active = active;
}



public String getPhoneNumber() {
	return phoneNumber;
}



public void setPhoneNumber(String phoneNumber) {
	this.phoneNumber = phoneNumber;
}



public String getAddressLine1() {
	return addressLine1;
}



public void setAddressLine1(String addressLine1) {
	this.addressLine1 = addressLine1;
}



public String getAddressLine2() {
	return addressLine2;
}



public void setAddressLine2(String addressLine2) {
	this.addressLine2 = addressLine2;
}



public String getCity() {
	return city;
}



public void setCity(String city) {
	this.city = city;
}



public String getState() {
	return state;
}



public void setState(String state) {
	this.state = state;
}



public String getZip() {
	return zip;
}



public void setZip(String zip) {
	this.zip = zip;
}



public Set<Role> getRoles() {
	return roles;
}


 
 /*@OneToOne(optional=false)
 @JoinColumn(name = "user_id") 
 private UserRole userRole;  */     
}