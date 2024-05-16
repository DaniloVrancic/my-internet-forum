package org.etf.unibl.SecureForum.model.entities;

import jakarta.persistence.*;
import org.etf.unibl.SecureForum.base.BaseEntity;
import org.etf.unibl.SecureForum.model.enums.PermissionType;

import java.util.Objects;

@Entity
@Table(name = "permissions", schema = "my_secure_forum", catalog = "")
public class PermissionsEntity implements BaseEntity<Integer> {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity referencedUser;
    @ManyToOne
    @JoinColumn(name = "topic_id", referencedColumnName = "id", nullable = false)
    private TopicEntity topicId;
    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "permission")
    private PermissionType permission;

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionsEntity that = (PermissionsEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(permission, that.permission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, referencedUser.getId(),permission);
    }

    public UserEntity getReferencedUser() {
        return referencedUser;
    }

    public void setReferencedUser(UserEntity referencedUser) {
        this.referencedUser = referencedUser;
    }

    public int getUserId() {
        return referencedUser.getId();
    }

    public void setUserId(int userId) {
        this.referencedUser.setId(userId);
    }

    public PermissionType getPermission() {
        return permission;
    }

    public void setPermission(PermissionType permission) {
        this.permission = permission;
    }
}
