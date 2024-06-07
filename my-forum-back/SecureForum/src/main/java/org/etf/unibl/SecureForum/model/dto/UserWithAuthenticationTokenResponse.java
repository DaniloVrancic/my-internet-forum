package org.etf.unibl.SecureForum.model.dto;

import org.etf.unibl.SecureForum.model.entities.UserEntity;
import org.etf.unibl.SecureForum.model.enums.UserType;

import java.sql.Timestamp;

public class UserWithAuthenticationTokenResponse {


    public UserWithAuthenticationTokenResponse(User user, String token){
        this.token = token;

        this.id = user.getId();
        this.username = user.getUsername();
        this.createTime = user.getCreateTime();
        this.email = user.getEmail();
        this.type = user.getType();
        this.status = user.getStatus();
    }

    private String token;

    private Integer id;
    private String username;
    private String email;

    private Timestamp createTime;

    private String type;

    private UserEntity.Status status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type.getValue();
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserEntity.Status getStatus() {
        return status;
    }

    public void setStatus(UserEntity.Status status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", createTime=" + createTime +
                ", type=" + type +
                ", status=" + status +
                ", token=" + token +
                '}';
    }
}
