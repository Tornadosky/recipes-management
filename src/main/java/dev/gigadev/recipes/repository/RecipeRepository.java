package dev.gigadev.recipes.repository;

import dev.gigadev.recipes.model.Recipe;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends MongoRepository<Recipe, ObjectId>, RecipeCustomRepository {
    List<Recipe> findRecipesByProperties(List<String> types, List<String> categories, Integer prepTime);
    List<Recipe> findRecipesByText(String searchPhrase);
    List<Recipe> findByCreatedBy_Id(String userId);
}
