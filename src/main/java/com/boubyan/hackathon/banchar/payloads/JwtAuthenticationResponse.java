package com.boubyan.hackathon.banchar.payloads;

public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private Long id; 
    private Boolean isDriver;

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }
    
    public JwtAuthenticationResponse(String accessToken, Long id, Boolean isDriver) {
        this.accessToken = accessToken;
        this.id = id;
        this.isDriver = isDriver;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getIsDriver() {
		return isDriver;
	}

	public void setIsDriver(Boolean isDriver) {
		this.isDriver = isDriver;
	}
    
    
}
