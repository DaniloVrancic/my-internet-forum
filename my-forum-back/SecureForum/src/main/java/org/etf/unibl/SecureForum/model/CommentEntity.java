package org.etf.unibl.SecureForum.model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "comment", schema = "my_secure_forum", catalog = "")
public class CommentEntity {
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
    @Column(name = "approved", nullable = false)
    private Boolean approved;
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

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentEntity that = (CommentEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(content, that.content) && Objects.equals(postedAt, that.postedAt) && Objects.equals(modifiedAt, that.modifiedAt) && Objects.equals(approved, that.approved);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, postedAt, modifiedAt, approved);
    }

    public ForumPostEntity getReferencedPost() {
        return referencedPost;
    }

    public void setReferencedPost(ForumPostEntity referencedPost) {
        this.referencedPost = referencedPost;
    }
}
