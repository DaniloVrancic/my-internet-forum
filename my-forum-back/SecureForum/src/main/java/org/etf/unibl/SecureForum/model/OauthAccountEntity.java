package org.etf.unibl.SecureForum.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "oauth_account", schema = "my_secure_forum", catalog = "")
public class OauthAccountEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "oauth_provider", nullable = true, length = 64)
    private String oauthProvider;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity referencedUser;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOauthProvider() {
        return oauthProvider;
    }

    public void setOauthProvider(String oauthProvider) {
        this.oauthProvider = oauthProvider;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OauthAccountEntity that = (OauthAccountEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(oauthProvider, that.oauthProvider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, oauthProvider);
    }

    public UserEntity getReferencedUser() {
        return referencedUser;
    }

    public void setReferencedUser(UserEntity referencedUser) {
        this.referencedUser = referencedUser;
    }
}
