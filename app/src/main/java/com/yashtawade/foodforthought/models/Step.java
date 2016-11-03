package com.yashtawade.foodforthought.models;

import java.util.ArrayList;
import java.util.List;

public class Step {
    private int number;
    private String step;
    private List<Ingredient> ingredients = new ArrayList<>();

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
