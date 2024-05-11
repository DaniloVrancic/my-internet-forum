package org.etf.unibl.SecureForum.model.requests;

import jakarta.validation.constraints.NotBlank;
import org.etf.unibl.SecureForum.model.entities.UserEntity;
import org.etf.unibl.SecureForum.model.enums.UserType;

public class ChangeRoleRequest {

    @NotBlank
    private Integer id;
    @NotBlank
    private String username;

    @NotBlank
    private UserType type;

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

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }
}
