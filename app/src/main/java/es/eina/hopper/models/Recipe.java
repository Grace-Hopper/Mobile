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
    private List<IngreRecipe> ingredients = new ArrayList();
    private List<Step> steps = new ArrayList();

    public Recipe(){

    }
    public Recipe(int id,String name, long tot, long per, byte[] pic, User user){
        this.id = id;
        this.name=name;
        total_time=tot;
        person=per;
        picture=pic;
        this.user=user;

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

    public List<IngreRecipe> getIngredients() {
        return ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

}
