package dev.gigadev.recipes.repository;

import dev.gigadev.recipes.model.Recipe;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends MongoRepository<Recipe, ObjectId>, RecipeCustomRepository {

    public List<Recipe> findRecipesByProperties(List<String> types, List<String> categories, Integer prepTime);

}
