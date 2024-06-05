package org.etf.unibl.SecureForum.model.requests;

import jakarta.validation.constraints.NotBlank;
import org.etf.unibl.SecureForum.model.entities.UserEntity;
import org.etf.unibl.SecureForum.model.enums.UserType;

public class UpdateUserPrivilegesRequest {

    @NotBlank
    private Integer id;
    @NotBlank
    private UserType type;

    @NotBlank
    private UserEntity.Status status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
