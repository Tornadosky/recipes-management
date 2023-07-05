package dev.gigadev.recipes.repository;

import dev.gigadev.recipes.model.Recipe;

import java.util.List;

public interface RecipeCustomRepository {
    List<Recipe> findRecipesByProperties(List<String> types, List<String> categories,
                                         Integer preparationTime);
    List<Recipe> findRecipesByText(String searchPhrase);
}
