package org.etf.unibl.SecureForum.model.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;

public class PermissionForTopicAndUserRequest {


    @NotNull
    @Positive
    private Integer userId;
    @NotNull
    @Positive
    private Integer topicId;
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }


}
