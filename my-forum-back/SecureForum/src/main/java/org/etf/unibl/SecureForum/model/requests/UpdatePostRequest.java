package org.etf.unibl.SecureForum.model.requests;

import jakarta.validation.constraints.NotNull;
import org.etf.unibl.SecureForum.additional.sanitizer.MyStringUtils;
import org.etf.unibl.SecureForum.model.entities.CommentEntity;
import org.etf.unibl.SecureForum.model.entities.ForumPostEntity;

public class UpdatePostRequest {

    @NotNull
    Integer id;
    @NotNull
    String title;
    @NotNull
    String content;
    @NotNull
    ForumPostEntity.Status status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return MyStringUtils.sanitize(title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return MyStringUtils.sanitize(content);
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ForumPostEntity.Status getStatus() {
        return status;
    }

    public void setStatus(ForumPostEntity.Status status) {
        this.status = status;
    }
}
