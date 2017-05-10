package es.eina.hopper.models;

import java.io.Serializable;
import java.util.*;

public class Step implements Serializable {

    private long step;
    private Recipe recipe;
    private long timer;
    private String information;
    private List<Utensil> utensils = new ArrayList();
    private List<Ingredient> ingredients = new ArrayList();

    public Step(){}

    public Step(long step, Recipe recipe, long timer, String information, List<Utensil> utensils,
                List<Ingredient> ingredients){
        this.step = step;
        this.recipe = recipe;
        this.timer = timer;
        this.information = information;
        this.utensils = utensils;
        this.ingredients = ingredients;
    }

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

    public long getTime() {
        return timer;
    }

    public void setTime(long timer) {
        this.timer = timer;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public List<Utensil> getUtensils() {
        return utensils;
    }

    public ArrayList<String> getListUtensils(){
        ArrayList<String> lista = new ArrayList<String>();
        for(int i = 0; i < ingredients.size(); i++) {
            lista.add(ingredients.get(i).getName());
        }
        return lista;
    }

    public void setUtensils(List<Utensil> utensils){
        this.utensils = utensils;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public ArrayList<String> getListIngredients(){
        ArrayList<String> lista = new ArrayList<String>();
        for(int i = 0; i < ingredients.size(); i++) {
            lista.add(ingredients.get(i).getName());
        }
        return lista;
    }

    public void setIngredients(List<Ingredient> ingredients){
        this.ingredients = ingredients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Step)) return false;
        Step that = (Step) o;
        return Objects.equals(getStep(), that.getStep()) &&
                Objects.equals(getRecipe(), that.getRecipe());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStep(), getRecipe());
    }

}
