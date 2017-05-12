package es.eina.hopper.models;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.Serializable;
import java.util.*;

public class Step implements Serializable {
    private long id;
    private long step;
    private long timer;
    private String information;
    private long recipe;
    private List<Ingredient> ingredients = new ArrayList();
    private List<Utensil> utensils = new ArrayList();

    public Step(){}

    public Step(long step, long timer, String info, List<Utensil> utensils, List<Ingredient> ingredients){
        this.step = step;
        this.timer = timer;
        this.information = info;
        this.ingredients = ingredients;
        this.utensils = utensils;
    }

    public List<Utensil> getUtensils() {
        return utensils;
    }

    public void setUtensils(List<Utensil> utensils) {
        this.utensils = utensils;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public long getStep() {
        return step;
    }

    public void setStep(long step) {
        this.step = step;
    }

    public long getRecipe() {
        return recipe;
    }

    public void setRecipe(long recipe) {
        this.recipe = recipe;
    }

    public long getTimer() {
        return timer;
    }

    public void setTimer(long timer) {
        this.timer = timer;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Step)) return false;
        Step that = (Step) o;
        return Objects.equals(getStep(), that.getStep()) &&
                Objects.equals(getRecipe(), that.getRecipe());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(getStep(), getRecipe());
    }

    public ArrayList<String> getListIngredients(){
        ArrayList<String> lista = new ArrayList<String>();
        for(int i = 0; i < ingredients.size(); i++) {
            lista.add(ingredients.get(i).getName());
        }
        return lista;
    }

    public ArrayList<String> getListUtensils(){
        ArrayList<String> lista = new ArrayList<String>();
        for(int i = 0; i < utensils.size(); i++) {
            lista.add(utensils.get(i).getName());
        }
        return lista;
    }

}
