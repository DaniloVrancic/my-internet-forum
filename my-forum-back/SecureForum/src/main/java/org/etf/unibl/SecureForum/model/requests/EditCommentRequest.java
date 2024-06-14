package org.etf.unibl.SecureForum.model.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.etf.unibl.SecureForum.additional.sanitizer.MyStringUtils;
import org.etf.unibl.SecureForum.model.entities.CommentEntity;

public class EditCommentRequest {

    @NotNull
    private Integer id;
    @NotNull
    @Size(min = 1)
    private String content;

    @NotNull
    private CommentEntity.Status status;
    public String getContent() {
        return MyStringUtils.sanitize(content);
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CommentEntity.Status getStatus() {
        return status;
    }

    public void setStatus(CommentEntity.Status status) {
        this.status = status;
    }

}
