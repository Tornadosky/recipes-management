package dev.gigadev.recipes.controller;

import dev.gigadev.recipes.model.Recipe;
import dev.gigadev.recipes.model.User;
import dev.gigadev.recipes.payload.UserProfileResponse;
import dev.gigadev.recipes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getUserProfile() {
        User user = userService.getAuthenticatedUser();

        UserProfileResponse userProfile = new UserProfileResponse(user.getId(), user.getUsername(), user.getEmail());

        return ResponseEntity.ok(userProfile);
    }

    @GetMapping("/me/recipes")
    public ResponseEntity<List<Recipe>> getUserRecipes() {
        List<Recipe> recipes = userService.getAuthenticatedUserRecipes();

        return ResponseEntity.ok(recipes);
    }
}