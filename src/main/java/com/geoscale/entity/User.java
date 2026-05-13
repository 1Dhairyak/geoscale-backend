package com.geoscale.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private int eloRating = 1000;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.PLAYER;

    @Column(length = 512)
    private String refreshToken;

    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GameSession> sessions;

    public enum Role { PLAYER, ADMIN }

    public User() {}

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String username, email, passwordHash;
        private Role role = Role.PLAYER;
        private int eloRating = 1000;

        public Builder username(String v) { this.username = v; return this; }
        public Builder email(String v) { this.email = v; return this; }
        public Builder passwordHash(String v) { this.passwordHash = v; return this; }
        public Builder role(Role v) { this.role = v; return this; }
        public Builder eloRating(int v) { this.eloRating = v; return this; }

        public User build() {
            User u = new User();
            u.username = this.username;
            u.email = this.email;
            u.passwordHash = this.passwordHash;
            u.role = this.role;
            u.eloRating = this.eloRating;
            return u;
        }
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public int getEloRating() { return eloRating; }
    public Role getRole() { return role; }
    public String getRefreshToken() { return refreshToken; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public List<GameSession> getSessions() { return sessions; }

    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setEloRating(int eloRating) { this.eloRating = eloRating; }
    public void setRole(Role role) { this.role = role; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}