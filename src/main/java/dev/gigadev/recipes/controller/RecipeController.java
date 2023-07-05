package dev.gigadev.recipes.controller;

import dev.gigadev.recipes.model.User;
import dev.gigadev.recipes.service.RecipeService;
import dev.gigadev.recipes.model.Recipe;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("")
    public List<Recipe> getRecipesByProperties(@RequestParam(required = false) List<String> types,
                                               @RequestParam(required = false) List<String> categories,
                                               @RequestParam(required = false) Integer preparationTime) {
        return recipeService.fetchRecipesByProperties(types, categories, preparationTime);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search")
    public List<Recipe> getRecipesByText(@RequestParam String searchPhrase) {
        return recipeService.findRecipesByText(searchPhrase);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Recipe getSingleRecipe(@PathVariable ObjectId id) {
        return recipeService.singleRecipe(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found!"));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/pdf")
    public ResponseEntity<Resource> getPdfFromRecipe(@PathVariable ObjectId id) throws Exception {
        return recipeService.exportRecipeToPdf(id);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public void createRecipe(@Valid @RequestBody Recipe recipe) {
        // Retrieve the currently authenticated user
        User currentUser = recipeService.getAuthenticatedUser();

        // Set the createdBy field to the currently authenticated user
        recipe.setCreatedBy(currentUser);

        // Save the recipe
        recipeService.saveRecipe(recipe);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public void updateRecipe(@Valid @RequestBody Recipe newRecipe, @PathVariable ObjectId id) {
        // Retrieve the currently authenticated user
        User currentUser = recipeService.getAuthenticatedUser();

        // Retrieve the recipe being updated
        Recipe recipe = recipeService.singleRecipe(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found!"));

        // Check if the current user is the creator of the recipe or an admin
        if (!currentUser.getId().equals(recipe.getCreatedBy().getId()) && recipeService.isNotUserAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to update this recipe!");
        }

        // Update the recipe
        recipe.setName(newRecipe.getName());
        recipe.setImageURL(newRecipe.getImageURL());
        recipe.setIngredients(newRecipe.getIngredients());
        recipe.setCategories(newRecipe.getCategories());
        recipe.setSteps(newRecipe.getSteps());
        recipe.setTypes(newRecipe.getTypes());
        recipe.setPreparationTime(newRecipe.getPreparationTime());

        // Save the updated recipe
        recipeService.saveRecipe(recipe);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public void deleteRecipe(@PathVariable ObjectId id) {
        // Retrieve the currently authenticated user
        User currentUser = recipeService.getAuthenticatedUser();

        // Retrieve the recipe being deleted
        Recipe recipe = recipeService.singleRecipe(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found!"));

        // Check if the current user is the creator of the recipe or an admin
        if (!currentUser.getId().equals(recipe.getCreatedBy().getId()) && recipeService.isNotUserAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to delete this recipe!");
        }

        // Delete the recipe
        recipeService.deleteById(id);
    }
}
