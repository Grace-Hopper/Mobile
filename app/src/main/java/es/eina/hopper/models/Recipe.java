package es.eina.hopper.models;

/**
 * Created by Fernando on 01/04/2017.
 */

import java.io.Serializable;


public class Recipe implements Serializable {
    private long id;
    private String name;
    private int total_time;

    private User user;

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

    public int getTotal_time() {
        return total_time;
    }

    public void setTotal_time(int total_time) {
        this.total_time = total_time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
