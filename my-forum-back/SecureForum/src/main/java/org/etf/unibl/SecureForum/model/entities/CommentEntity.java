package org.etf.unibl.SecureForum.model.entities;

import jakarta.persistence.*;
import org.etf.unibl.SecureForum.base.BaseEntity;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "comment", schema = "my_secure_forum", catalog = "")
public class CommentEntity implements BaseEntity<Integer> {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "content", nullable = true, length = -1)
    private String content;
    @Basic
    @Column(name = "posted_at", nullable = false)
    private Timestamp postedAt;
    @Basic
    @Column(name = "modified_at", nullable = true)
    private Timestamp modifiedAt;
    @Basic
    @Column(name = "status", nullable = false)
    private Status status;
    @ManyToOne
    @JoinColumn(name = "forum_post_id", referencedColumnName = "id", nullable = false)
    private ForumPostEntity referencedPost;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity referencedUser;

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentEntity that = (CommentEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(content, that.content) && Objects.equals(postedAt, that.postedAt) && Objects.equals(modifiedAt, that.modifiedAt) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, postedAt, modifiedAt, status);
    }

    public ForumPostEntity getReferencedPost() {
        return referencedPost;
    }

    public void setReferencedPost(ForumPostEntity referencedPost) {
        this.referencedPost = referencedPost;
    }

    public UserEntity getReferencedUser() {
        return referencedUser;
    }

    public void setReferencedUser(UserEntity referencedUser) {
        this.referencedUser = referencedUser;
    }

    public enum Status{
        NEW, APPROVED, REJECTED
    }
}
