package com.boubyan.hackathon.banchar.dtos;

import javax.persistence.Column;
import javax.persistence.Id;

public class WinchDetailsDTO {

	private Long id;
	
	private String name;
	
	private String lPlate;
	
	
	private String lat;
	
	
	private String lon;
	
	
	
	public String getlPlate() {
		return lPlate;
	}
	public void setlPlate(String lPlate) {
		this.lPlate = lPlate;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
}
