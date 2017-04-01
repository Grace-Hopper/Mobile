package es.eina.hopper.models;

/**
 * Created by Fernando on 31/03/2017.
 */

import java.io.Serializable;

public class User{

    private long id;

    private String name;

    private String password;

    public User(String n, String pass){
        this.name = n;
        this.password = pass;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
