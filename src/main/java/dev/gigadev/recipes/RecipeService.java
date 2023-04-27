package dev.gigadev.recipes;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    public List<Recipe> allRecipes() {
        return recipeRepository.findAll();
    }

    public Optional<Recipe> singleRecipe(ObjectId id) {
        return recipeRepository.findById(id);
    }
}
