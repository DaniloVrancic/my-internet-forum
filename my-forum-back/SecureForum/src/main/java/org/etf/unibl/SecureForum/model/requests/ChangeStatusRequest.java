package org.etf.unibl.SecureForum.model.requests;

import jakarta.validation.constraints.NotBlank;
import org.etf.unibl.SecureForum.model.entities.UserEntity;

public class ChangeStatusRequest {

    @NotBlank
    private Integer id;

    @NotBlank
    private UserEntity.Status status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserEntity.Status getStatus() {
        return status;
    }

    public void setStatus(UserEntity.Status status) {
        this.status = status;
    }
}
