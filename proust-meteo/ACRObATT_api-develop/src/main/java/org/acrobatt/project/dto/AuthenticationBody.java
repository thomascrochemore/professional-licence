package org.acrobatt.project.dto;

/**
 * A request body used for authentication and registration only.
 * This body is a pure DTO.
 */
public class AuthenticationBody {
    private String username;
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
