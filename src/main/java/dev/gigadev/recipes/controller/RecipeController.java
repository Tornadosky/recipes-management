package dev.gigadev.recipes.controller;

import dev.gigadev.recipes.service.RecipeService;
import dev.gigadev.recipes.model.Recipe;
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
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

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
        return recipeService.singleRecipe(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found!"));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public void createRecipe(@RequestBody Recipe recipe) {
        recipeService.saveRecipe(recipe);
    }
}
