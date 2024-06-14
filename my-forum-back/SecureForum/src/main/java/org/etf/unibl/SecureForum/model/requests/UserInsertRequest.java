package org.etf.unibl.SecureForum.model.requests;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.etf.unibl.SecureForum.additional.sanitizer.MyStringUtils;
import org.etf.unibl.SecureForum.model.entities.UserEntity;
import org.etf.unibl.SecureForum.model.enums.UserType;

import java.sql.Timestamp;

public class UserInsertRequest {

    @NotNull(message = "Username is required")
    private String username;
    @NotNull(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    @NotNull(message = "Password is required")
    @Size(min = 5, message = "Password must be at least 5 characters long")
    private String password;

    private Timestamp createTime;
    @NotNull(message = "User Type is required")
    private UserType type;

    @NotNull
    private UserEntity.Status status;
    public String getUsername() {
        return MyStringUtils.sanitize(username);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return MyStringUtils.sanitize(email);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public UserEntity.Status getStatus() {
        return status;
    }

    public void setStatus(UserEntity.Status status) {
        this.status = status;
    }
}
