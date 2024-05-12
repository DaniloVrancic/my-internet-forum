package org.etf.unibl.SecureForum.model.requests;

import jakarta.validation.constraints.NotNull;

public class VerifyUserRequest {

    @NotNull
    private Integer user_id;

    @NotNull
    private String verificationCode;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

}
