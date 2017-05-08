package es.eina.hopper.models;

import java.io.Serializable;
import java.util.*;

public class Ingredient implements Serializable {

    private long id;
    private String name;
    private List<IngreRecipe> recipes = new ArrayList();

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

    public List<IngreRecipe> getRecipes() {
        return recipes;
    }

}
