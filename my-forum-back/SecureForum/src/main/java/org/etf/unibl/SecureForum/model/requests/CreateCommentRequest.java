package org.etf.unibl.SecureForum.model.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.etf.unibl.SecureForum.model.entities.CommentEntity;
import org.etf.unibl.SecureForum.model.entities.ForumPostEntity;
import org.etf.unibl.SecureForum.model.entities.UserEntity;

public class CreateCommentRequest {

    @NotNull
    @Size(min = 1)
    private String content;
    @NotNull
    private ForumPostEntity forum_post;
    @NotNull
    private UserEntity user;
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ForumPostEntity getForum_post() {
        return forum_post;
    }

    public void setForum_post(ForumPostEntity forum_post) {
        this.forum_post = forum_post;
    }

    public void setForum_post(Integer forum_post_id){
        this.forum_post = new ForumPostEntity();
        this.forum_post.setId(forum_post_id);
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

}
