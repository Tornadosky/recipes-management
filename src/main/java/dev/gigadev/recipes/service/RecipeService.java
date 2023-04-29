package dev.gigadev.recipes.service;

import dev.gigadev.recipes.model.Recipe;
import dev.gigadev.recipes.repository.RecipeRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    // api/v1/recipes return all recipes
//    public List<Recipe> allRecipes() {
//        return recipeRepository.findAll();
//    }

    public List<Recipe> fetchRecipesByProperties(List<String> types, List<String> categories,
                                                 Integer preparationTime) {
        return recipeRepository.findRecipesByProperties(types, categories, preparationTime);
    }

    // api/v1/recipes/{id} return json recipe with {id}
    // if no {id} exists in db return null
    public Optional<Recipe> singleRecipe(ObjectId id) {
        return recipeRepository.findById(id);
    }
}
