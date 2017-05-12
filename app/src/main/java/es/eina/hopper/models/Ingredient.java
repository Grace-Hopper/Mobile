package es.eina.hopper.models;

import java.io.*;
import java.util.*;

public class Ingredient implements Serializable {
    private long id;
    private String name;
    private String quantity;
    private long recipe;
    private long step;

    public Ingredient() {
    }

    public Ingredient(long id, String name, String quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public long getRecipe() {
        return recipe;
    }

    public void setRecipes(long recipe) {
        this.recipe = recipe;
    }
    public long getStep() {
        return step;
    }

    public void setStep(long step) {
        this.step = step;
    }
}


