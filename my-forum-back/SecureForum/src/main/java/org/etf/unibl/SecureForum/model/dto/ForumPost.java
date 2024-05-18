package org.etf.unibl.SecureForum.model.dto;

import org.etf.unibl.SecureForum.model.entities.TopicEntity;

import java.sql.Timestamp;

public class ForumPost {

    private Integer id;
    private String title;
    private String content;
    private Timestamp created_at;
    private Timestamp modified_at;
    private String topic;
    private String user_creator;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getModified_at() {
        return modified_at;
    }

    public void setModified_at(Timestamp modified_at) {
        this.modified_at = modified_at;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getUser_creator() {
        return user_creator;
    }

    public void setUser_creator(String user_creator) {
        this.user_creator = user_creator;
    }

    @Override
    public String toString() {
        return "ForumPost{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", created_at=" + created_at +
                ", modified_at=" + modified_at +
                ", topic='" + topic + '\'' +
                ", user_creator='" + user_creator + '\'' +
                '}';
    }
}
