package es.eina.hopper.models;


import java.io.Serializable;

/**
 * =====================================================================================
 * Filename: User.java
 * Version: 1.0
 * Created: 3/24/17
 * Author: JORGE CHATO (651348)
 * =====================================================================================
 */

public class User implements Serializable{
    private long id;
    private String name;
    private String password;

    public User (String nombre, String pass){
        name=nombre;
        password=pass;
        id=-1;
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
