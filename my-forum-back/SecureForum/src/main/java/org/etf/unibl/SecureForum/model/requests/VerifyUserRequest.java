package org.etf.unibl.SecureForum.model.requests;

import jakarta.validation.constraints.NotNull;
import org.etf.unibl.SecureForum.additional.sanitizer.MyStringUtils;

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
        return MyStringUtils.sanitize(verificationCode);
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

}
