package dev.gigadev.recipes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "recipes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {
    @Id
    private ObjectId id;
    @TextIndexed
    private String name;
    private String imageURL;
    private List<Ingredient> ingredients;
    private List<String> categories;
    private List<String> types;
    private Integer preparationTime;

}
