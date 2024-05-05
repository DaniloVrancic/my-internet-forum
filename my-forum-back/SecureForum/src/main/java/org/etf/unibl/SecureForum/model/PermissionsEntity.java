package org.etf.unibl.SecureForum.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "permissions", schema = "my_secure_forum", catalog = "")
public class PermissionsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "all_permissions", nullable = true, length = 512)
    private String allPermissions;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity referencedUser;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAllPermissions() {
        return allPermissions;
    }

    public void setAllPermissions(String allPermissions) {
        this.allPermissions = allPermissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionsEntity that = (PermissionsEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(allPermissions, that.allPermissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, allPermissions);
    }

    public UserEntity getReferencedUser() {
        return referencedUser;
    }

    public void setReferencedUser(UserEntity referencedUser) {
        this.referencedUser = referencedUser;
    }
}
