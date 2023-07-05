package dev.gigadev.recipes.service;

import dev.gigadev.recipes.model.Recipe;
import dev.gigadev.recipes.model.User;
import dev.gigadev.recipes.repository.RecipeRepository;
import dev.gigadev.recipes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = ((UserDetailsImpl) authentication.getPrincipal()).getId();

        return userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public List<Recipe> getAuthenticatedUserRecipes() {
        User currentUser = getAuthenticatedUser();

        return recipeRepository.findByCreatedBy_Id(currentUser.getId());
    }
}