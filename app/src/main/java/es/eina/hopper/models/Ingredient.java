package es.eina.hopper.models;

import java.io.*;
import java.util.*;

public class Ingredient implements Serializable {
    private long id;
    private String name;
    private String quantity;
    private List<Recipe> recipes = new ArrayList();

    public Ingredient() {
    }

    public Ingredient(long id, String name, String quantity, List<Recipe> recipes) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.recipes = recipes;
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

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}


