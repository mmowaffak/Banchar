package com.boubyan.hackathon.banchar.payloads;

public class ApiResponse {
    private Boolean success;
    private String message;

    private Long id;
    public ApiResponse(Boolean success, String message, Long id) {
        this.success = success;
        this.message = message;
        this.id = id;
    }
    
    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
