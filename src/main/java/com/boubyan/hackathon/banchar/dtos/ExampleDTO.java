package com.boubyan.hackathon.banchar.dtos;

import java.io.Serializable;

public class ExampleDTO implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String fname;
	
	private String password;

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
