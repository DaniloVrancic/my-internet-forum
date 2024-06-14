package org.etf.unibl.SecureForum.model.requests;

import org.etf.unibl.SecureForum.additional.sanitizer.MyStringUtils;

public class AuthenticationRequest {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
