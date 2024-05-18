package org.etf.unibl.SecureForum.model.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.etf.unibl.SecureForum.model.entities.TopicEntity;
import org.etf.unibl.SecureForum.model.entities.UserEntity;

public class UpdatePostRequest {

    @NotNull
    private Integer id;
    @NotNull
    @Size(min = 1)
    private String title;
    @NotNull
    @Size(min = 1)
    private String content;
    @NotNull
    private TopicEntity topic;
    @NotNull
    private UserEntity user;

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

    public TopicEntity getTopic() {
        return topic;
    }

    public void setTopic(TopicEntity topic) {
        this.topic = topic;
    }

    public void setTopic(Integer topic_id){
        this.topic = new TopicEntity();
        this.topic.setId(topic_id);
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public void setUser(Integer user_id){
        this.user = new UserEntity();
        this.user.setId(user_id);
    }

    public Integer getId(){
        return this.id;
    }

    public void setId(Integer id){
        this.id = id;
    }

}
