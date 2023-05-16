package dev.gigadev.recipes.controller;

import dev.gigadev.recipes.service.RecipeService;
import dev.gigadev.recipes.model.Recipe;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    //    @GetMapping
    //    public ResponseEntity<List<Recipe>> getAllRecipes() {
    //        return new ResponseEntity<List<Recipe>>(recipeService.allRecipes(), HttpStatus.OK);
    //    }

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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public void createRecipe(@Valid @RequestBody Recipe recipe) {
        recipeService.saveRecipe(recipe);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateRecipe(@Valid @RequestBody Recipe newRecipe, @PathVariable ObjectId id) {
        recipeService.singleRecipe(id)
                .map(recipe -> {
                    recipe.setName(newRecipe.getName());
                    recipe.setImageURL(newRecipe.getImageURL());
                    recipe.setIngredients(newRecipe.getIngredients());
                    recipe.setCategories(newRecipe.getCategories());
                    recipe.setTypes(newRecipe.getTypes());
                    recipe.setPreparationTime(newRecipe.getPreparationTime());
                    return recipeService.saveRecipe(recipe);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found!"));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteRecipe(@PathVariable ObjectId id) {
        recipeService.deleteById(id);
    }
}
