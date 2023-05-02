package dev.gigadev.recipes.controller;

import dev.gigadev.recipes.service.RecipeService;
import dev.gigadev.recipes.model.Recipe;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    //    @GetMapping
    //    public ResponseEntity<List<Recipe>> getAllRecipes() {
    //        return new ResponseEntity<List<Recipe>>(recipeService.allRecipes(), HttpStatus.OK);
    //    }

    @GetMapping
    public ResponseEntity<?> getRecipesByProperties(@RequestParam(required = false) List<String> types,
                                                    @RequestParam(required = false) List<String> categories,
                                                    @RequestParam(required = false) Integer preparationTime) {
        return ResponseEntity.ok().body(recipeService.fetchRecipesByProperties(types, categories, preparationTime));
    }

    @GetMapping("/search")
    public ResponseEntity<?> getRecipesByText(@RequestParam String searchPhrase) {
        return ResponseEntity.ok().body(recipeService.findRecipesByText(searchPhrase));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Recipe>> getSingleRecipe(@PathVariable ObjectId id) {
        return new ResponseEntity<Optional<Recipe>>(recipeService.singleRecipe(id), HttpStatus.OK);
    }
}
