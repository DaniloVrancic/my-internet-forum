package org.etf.unibl.SecureForum.model.requests;

import jakarta.validation.constraints.NotNull;
import org.etf.unibl.SecureForum.model.entities.CommentEntity;

public class UpdateCommentRequest {

    @NotNull
    Integer id;
    @NotNull
    String content;
    @NotNull
    CommentEntity.Status status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CommentEntity.Status getStatus() {
        return status;
    }

    public void setStatus(CommentEntity.Status status) {
        this.status = status;
    }
}
