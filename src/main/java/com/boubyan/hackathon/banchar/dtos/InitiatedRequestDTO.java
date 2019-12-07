package com.boubyan.hackathon.banchar.dtos;

public class InitiatedRequestDTO {

	private String customerName; 
	private String customerLat;
	private String customerLon;
	private String reqDetails;
	private Double customerRating;
	private Long id;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerLat() {
		return customerLat;
	}
	public void setCustomerLat(String customerLat) {
		this.customerLat = customerLat;
	}
	public String getCustomerLon() {
		return customerLon;
	}
	public void setCustomerLon(String customerLon) {
		this.customerLon = customerLon;
	}
	public String getReqDetails() {
		return reqDetails;
	}
	public void setReqDetails(String reqDetails) {
		this.reqDetails = reqDetails;
	}
	public Double getCustomerRating() {
		return customerRating;
	}
	public void setCustomerRating(Double customerRating) {
		this.customerRating = customerRating;
	}
	
	
}
