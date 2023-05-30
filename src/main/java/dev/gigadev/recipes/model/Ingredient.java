package dev.gigadev.recipes.model;

interface Nameable {
    public String getName();
    public String getQuantity();
}

public record Ingredient(String name, String quantity) {
    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }
}
