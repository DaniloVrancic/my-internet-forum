package org.etf.unibl.SecureForum.model.dto;

public class AuthResponse {

    private String token;

    public AuthResponse(String accessToken){
        this.token = accessToken;
    }
    public AuthResponse() {this.token = "";}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }



}
