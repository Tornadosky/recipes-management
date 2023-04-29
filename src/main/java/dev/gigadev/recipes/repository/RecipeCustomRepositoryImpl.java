package dev.gigadev.recipes.repository;

import dev.gigadev.recipes.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

public class RecipeCustomRepositoryImpl implements RecipeCustomRepository{

    @Autowired
    MongoTemplate mongoTemplate;

    public List<Recipe> findRecipesByProperties(List<String> types, List<String> categories,
                                                Integer preparationTime) {
        final Query query = new Query();
        // query.fields().include("id").include("name");
        final List<Criteria> criteria = new ArrayList<>();
        if (types != null && !types.isEmpty())
            criteria.add(Criteria.where("types").in(types));
        if (categories != null && !categories.isEmpty())
            criteria.add(Criteria.where("categories").in(categories));
        if (preparationTime != null)
            criteria.add(Criteria.where("preparationTime").lte(preparationTime));

        if (!criteria.isEmpty())
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));
        return mongoTemplate.find(query, Recipe.class);
    }
}
