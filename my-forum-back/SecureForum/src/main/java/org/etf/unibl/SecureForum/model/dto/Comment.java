package org.etf.unibl.SecureForum.model.dto;

import org.etf.unibl.SecureForum.model.entities.CommentEntity;
import org.etf.unibl.SecureForum.model.entities.UserEntity;

import java.sql.Timestamp;

public class Comment {


    private Integer id;
    private String content;
    private Timestamp postedAt;
    private Timestamp modifiedAt;

    private Integer user_id;
    private String username;
    private CommentEntity.Status status;
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

    public Timestamp getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(Timestamp postedAt) {
        this.postedAt = postedAt;
    }

    public Timestamp getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Timestamp modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public CommentEntity.Status getStatus() {
        return status;
    }

    public void setStatus(CommentEntity.Status status) {
        this.status = status;
    }
}
