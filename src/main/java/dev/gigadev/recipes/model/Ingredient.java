package dev.gigadev.recipes.model;

interface Nameable {
    public String getName();
    public String getQuantity();
    public String getUnits();
}

public record Ingredient(String name, String quantity, String units) {
    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }
    public String getUnits() { return units; }
}
