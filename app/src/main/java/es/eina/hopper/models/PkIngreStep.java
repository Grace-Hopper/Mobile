package es.eina.hopper.models;

import java.io.*;
import java.util.*;

public class PkIngreStep implements Serializable {
    /*@JoinColumns({
            @JoinColumn(name = "step", referencedColumnName = "step"),
            @JoinColumn(name = "recipe", referencedColumnName = "recipe")})*/
    private Step step;
    private Ingredient ingredient;
 
    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }
 
    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }
 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PkIngreStep)) return false;
        PkIngreStep that = (PkIngreStep) o;
        return Objects.equals(getStep(), that.getStep()) &&
                Objects.equals(getIngredient(), that.getIngredient());
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(getStep(), getIngredient());
    }
}
