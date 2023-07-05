package dev.gigadev.recipes.payload;

import lombok.Data;

@Data
public class UserProfileResponse {
    private String id;
    private String username;
    private String email;

    public UserProfileResponse(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
}