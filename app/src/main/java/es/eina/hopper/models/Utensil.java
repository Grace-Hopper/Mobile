package es.eina.hopper.models;

import java.io.Serializable;
import java.util.*;

public class Utensil implements Serializable {

    private long id;
    private String name;

    public Utensil(){}

    public Utensil(long id, String name){
        this.id = id;
        this.name = name;
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

}
