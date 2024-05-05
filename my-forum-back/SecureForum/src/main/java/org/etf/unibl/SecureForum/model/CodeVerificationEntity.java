package org.etf.unibl.SecureForum.model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "code_verification", schema = "my_secure_forum", catalog = "")
public class CodeVerificationEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "verification_code", nullable = false, length = 8)
    private String verificationCode;
    @Basic
    @Column(name = "created_at", nullable = true)
    private Timestamp createdAt;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity referencedUser;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodeVerificationEntity that = (CodeVerificationEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(verificationCode, that.verificationCode) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, verificationCode, createdAt);
    }

    public UserEntity getReferencedUser() {
        return referencedUser;
    }

    public void setReferencedUser(UserEntity referencedUser) {
        this.referencedUser = referencedUser;
    }
}
