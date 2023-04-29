package dev.gigadev.recipes.repository;

import dev.gigadev.recipes.model.Recipe;

import java.util.List;

public interface RecipeCustomRepository {
    public List<Recipe> findRecipesByProperties(List<String> types, List<String> categories,
                                                Integer preparationTime);
}

