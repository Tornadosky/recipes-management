package dev.gigadev.recipes.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "recipes")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    @TextIndexed
    @NotBlank(message = "Name is mandatory")
    private String name;
    private String imageURL;
    @NotEmpty(message = "Ingredients can't be empty")
    private List<Ingredient> ingredients;
    @NotEmpty(message = "Steps can't be empty")
    private List<String> steps;
    private List<String> categories;
    private List<String> types;
    @DecimalMax("600")
    @Positive
    private Integer preparationTime;
    @DBRef
    private User createdBy;

    public Recipe(String name,
                  String imageURL,
                  List<Ingredient> ingredients,
                  List<String> steps,
                  List<String> categories,
                  List<String> types,
                  Integer preparationTime)
    {
        this.name = name;
        this.imageURL = imageURL;
        this.ingredients = ingredients;
        this.steps = steps;
        this.categories = categories;
        this.types = types;
        this.preparationTime = preparationTime;
    }
}

