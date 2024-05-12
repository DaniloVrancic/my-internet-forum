package org.etf.unibl.SecureForum.model.requests;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import org.etf.unibl.SecureForum.model.entities.UserEntity;
import org.etf.unibl.SecureForum.model.enums.UserType;

import java.sql.Timestamp;

public class UserInsertRequest {

    @NotNull
    private String username;
    @NotNull
    private String email;
    @NotNull
    private String password;

    private Timestamp createTime;
    @NotNull
    private UserType type;

    @NotNull
    private UserEntity.Status status;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
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
