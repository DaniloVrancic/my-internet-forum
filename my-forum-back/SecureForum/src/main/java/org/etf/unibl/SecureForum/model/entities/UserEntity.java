package org.etf.unibl.SecureForum.model.entities;

import jakarta.persistence.*;
import org.etf.unibl.SecureForum.base.BaseEntity;
import org.etf.unibl.SecureForum.model.enums.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "user", schema = "my_secure_forum", catalog = "")
public class UserEntity implements BaseEntity<Integer>, UserDetails {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "username", nullable = false, length = 32)
    private String username;
    @Basic
    @Column(name = "email", nullable = false, length = 256)
    private String email;
    @Basic
    @Column(name = "password", length = 512)
    private String password;
    @Basic
    @Column(name = "create_time", nullable = false)
    private Timestamp createTime;
    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private UserType type;

    @Basic
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    private Status status;

    @Basic
    @Column(name = "oauth_account", nullable = false)
    private Boolean oauth_account;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
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
        UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(createTime, that.createTime) && type == that.type && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password, createTime, type, status);
    }

    public enum Status{
        REQUESTED, ACTIVE, BLOCKED
    }

    public Boolean getOauth_account() {
        return oauth_account;
    }

    public void setOauth_account(Boolean oauth_account) {
        this.oauth_account = oauth_account;
    }


    //OVERRIDEN USER DETAILS:

    @Override
    public String getUsername() {
        return username;
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.getType().getValue()));
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !Status.BLOCKED.equals(this.status);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
