package com.bernardguiang.SnackOverflow.model;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class ShippingDetails {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String name;
	private String phone;
	
	@Embedded
//	@AttributeOverrides({
//		  @AttributeOverride( name = "addressLineOne", column = @Column(name = "billing_address_line_one")),
//		  @AttributeOverride( name = "addressLineTwo", column = @Column(name = "billing_address_line_two")),
//		  @AttributeOverride( name = "city", column = @Column(name = "billing_city")),
//		  @AttributeOverride( name = "state", column = @Column(name = "billing_state")),
//		  @AttributeOverride( name = "postalCode", column = @Column(name = "billing_postal_code")),
//		  @AttributeOverride( name = "country", column = @Column(name = "billing_country"))
//	})
	private Address address;
	
	@OneToOne(mappedBy = "billingDetails")
	private Order order;
	
	public ShippingDetails() {
	}
	

	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}


	public Order getOrder() {
		return order;
	}


	public void setOrder(Order order) {
		this.order = order;
	}
	
	
}
