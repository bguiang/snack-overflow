package com.bernardguiang.SnackOverflow.dto;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Embeddable
public class Address {
	@NotBlank(message = "Address Line One cannot be null or blank")
	private String addressLineOne;
	private String addressLineTwo;
	@NotBlank(message = "City cannot be null or blank")
	private String city;
	@NotBlank(message = "State cannot be null or blank")
	private String state;
	@NotBlank(message = "PostalCode cannot be null or blank")
	private String postalCode;
	@NotBlank(message = "Country cannot be null or blank")
	private String country;
	
	public Address() {
	}
	
	public Address(String addressLineOne,
			String addressLineTwo, 
			String city,
			String state,
			String postalCode,
			String country) {
		this.addressLineOne = addressLineOne;
		this.addressLineTwo = addressLineTwo;
		this.city = city;
		this.state = state;
		this.postalCode = postalCode;
		this.country = country;
	}
	
	public String getAddressLineOne() {
		return addressLineOne;
	}
	public void setAddressLineOne(String addressLineOne) {
		this.addressLineOne = addressLineOne;
	}
	public String getAddressLineTwo() {
		return addressLineTwo;
	}
	public void setAddressLineTwo(String addressLineTwo) {
		this.addressLineTwo = addressLineTwo;
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
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	@Override
	public String toString() {
		return "Address [addressLineOne=" + addressLineOne + ", addressLineTwo=" + addressLineTwo + ", city=" + city
				+ ", state=" + state + ", postalCode=" + postalCode + ", country=" + country + "]";
	}
	
	
}
