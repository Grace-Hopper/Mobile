package es.eina.hopper.models;

import java.io.Serializable;
import java.util.*;

/**
 * =====================================================================================
 * Filename: Recipe.java
 * Version: 1.0
 * Created: 3/24/17
 * Author: JORGE CHATO (651348)
 * =====================================================================================
 */

public class Recipe implements Serializable {

    private long id;
    private String name;
    private long total_time;
    private long person;
    private int outstanding;
    private byte[] picture;
    private User user;
    private List<Utensil> utensils = new ArrayList();
    private List<Ingredient> ingredients = new ArrayList();
    private List<Step> steps = new ArrayList();

    public Recipe(){}

    public Recipe(long id, String name, long total_time, long person, int outstanding, byte[] picture,
                 User user, List<Utensil> utensils, List<Ingredient> ingredients,List<Step> steps){
        this.id = id;
        this.name = name;
        this.total_time = total_time;
        this.person = person;
        this.outstanding = outstanding;
        this.picture = picture;
        this.user = user;
        this.utensils = utensils;
        this.ingredients = ingredients;
        this.steps = steps;
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

    public long getTotal_time() {
        return total_time;
    }

    public void setTotal_time(long total_time) {
        this.total_time = total_time;
    }

    public long getPerson() {
        return person;
    }

    public void setPerson(long person) {
        this.person = person;
    }

    public int getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(int outstanding) {
        this.outstanding = outstanding;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Utensil> getUtensils() {
        return utensils;
    }

    public ArrayList<String> getListUtensils(){
        ArrayList<String> lista = new ArrayList<String>();
        for(int i = 0; i < utensils.size(); i++) {
            lista.add(utensils.get(i).getName());
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

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps){
        this.steps = steps;
    }
}