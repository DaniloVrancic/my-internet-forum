package org.etf.unibl.SecureForum.model.requests;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import org.etf.unibl.SecureForum.model.enums.PermissionType;

public class PermissionsRequest {

    @NotNull
    private Integer user_id;
    @NotNull
    private Integer topic_id;
    @NotNull
    @Enumerated(EnumType.STRING)
    private PermissionType type;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(Integer topic_id) {
        this.topic_id = topic_id;
    }

    public PermissionType getType() {
        return type;
    }

    public void setType(PermissionType type) {
        this.type = type;
    }
}
