package org.etf.unibl.SecureForum.model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "access_logs", schema = "my_secure_forum", catalog = "")
public class AccessLogsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "log_content", nullable = true, length = -1)
    private String logContent;
    @Basic
    @Column(name = "log_time", nullable = true)
    private Timestamp logTime;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity referencedUser;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogContent() {
        return logContent;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }

    public Timestamp getLogTime() {
        return logTime;
    }

    public void setLogTime(Timestamp logTime) {
        this.logTime = logTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccessLogsEntity that = (AccessLogsEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(logContent, that.logContent) && Objects.equals(logTime, that.logTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, logContent, logTime);
    }

    public UserEntity getReferencedUser() {
        return referencedUser;
    }

    public void setReferencedUser(UserEntity referencedUser) {
        this.referencedUser = referencedUser;
    }
}
