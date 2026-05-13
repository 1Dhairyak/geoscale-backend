package com.geoscale.dto.response;

public class AuthResponse {
    private String token;
    private String refreshToken;
    private String username;
    private String email;
    private String role;

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String token, refreshToken, username, email, role;
        public Builder token(String v) { this.token = v; return this; }
        public Builder refreshToken(String v) { this.refreshToken = v; return this; }
        public Builder username(String v) { this.username = v; return this; }
        public Builder email(String v) { this.email = v; return this; }
        public Builder role(String v) { this.role = v; return this; }
        public AuthResponse build() {
            AuthResponse r = new AuthResponse();
            r.token = this.token;
            r.refreshToken = this.refreshToken;
            r.username = this.username;
            r.email = this.email;
            r.role = this.role;
            return r;
        }
    }

    public String getToken() { return token; }
    public String getRefreshToken() { return refreshToken; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
}