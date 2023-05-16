package dev.gigadev.recipes.service;

import dev.gigadev.recipes.model.Recipe;
import dev.gigadev.recipes.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public List<Recipe> allRecipes() {
        return recipeRepository.findAll();
    }

    public List<Recipe> fetchRecipesByProperties(List<String> types, List<String> categories,
                                                 Integer preparationTime) {
        return recipeRepository.findRecipesByProperties(types, categories, preparationTime);
    }

    public List<Recipe> findRecipesByText(String searchPhrase) {
        return recipeRepository.findRecipesByText(searchPhrase);
    }

    public Optional<Recipe> singleRecipe(ObjectId id) {
        return recipeRepository.findById(id);
    }

    public void deleteById(ObjectId id) {
        recipeRepository.deleteById(id);
    }

    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }
}
