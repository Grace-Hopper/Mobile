package es.eina.hopper.models;

import java.io.*;
import java.util.*;

public class PkStep implements Serializable {

    private long step;

    private Recipe recipe;
 
    public long getStep() {
        return step;
    }
 
    public void setStep(long step) {
        this.step = step;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PkStep)) return false;
        PkStep that = (PkStep) o;
        return Objects.equals(getStep(), that.getStep()) &&
                Objects.equals(getRecipe(), that.getRecipe());
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(getStep(), getRecipe());
    }
}