package org.etf.unibl.SecureForum.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.etf.unibl.SecureForum.base.BaseEntity;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "forum_post", schema = "my_secure_forum", catalog = "")
public class ForumPostEntity implements BaseEntity<Integer> {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "title", nullable = true, length = 128)
    private String title;
    @Basic
    @Column(name = "content", nullable = true, length = -1)
    private String content;
    @Basic
    @Column(name = "posted_at", nullable = false)
    private Timestamp postedAt;
    @Basic
    @Column(name = "modified_at", nullable = true)
    private Timestamp modifiedAt;
    @OneToMany(mappedBy = "referencedPost")
    @JsonIgnore
    private List<CommentEntity> commentsOnPost;
    @ManyToOne
    @JoinColumn(name = "topic_id", referencedColumnName = "id", nullable = false)
    private TopicEntity postTopic;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity postCreator;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForumPostEntity that = (ForumPostEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(content, that.content) && Objects.equals(postedAt, that.postedAt) && Objects.equals(modifiedAt, that.modifiedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, postedAt, modifiedAt);
    }

    public List<CommentEntity> getCommentsOnPost() {
        return commentsOnPost;
    }

    public void setCommentsOnPost(List<CommentEntity> commentsOnPost) {
        this.commentsOnPost = commentsOnPost;
    }

    public TopicEntity getPostTopic() {
        return postTopic;
    }

    public void setPostTopic(TopicEntity postTopic) {
        this.postTopic = postTopic;
    }

    public UserEntity getPostCreator() {
        return postCreator;
    }

    public void setPostCreator(UserEntity postCreator) {
        this.postCreator = postCreator;
    }
}
