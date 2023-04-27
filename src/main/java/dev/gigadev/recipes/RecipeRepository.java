package dev.gigadev.recipes;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends MongoRepository<Recipe, ObjectId> {

}
