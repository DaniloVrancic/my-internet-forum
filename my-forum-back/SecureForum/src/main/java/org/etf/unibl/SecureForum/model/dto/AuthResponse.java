package org.etf.unibl.SecureForum.model.dto;

public class AuthResponse {
    private String accessToken;
    private String tokenType;

    public AuthResponse(String accessToken){
        this.accessToken = accessToken;
    }
}
