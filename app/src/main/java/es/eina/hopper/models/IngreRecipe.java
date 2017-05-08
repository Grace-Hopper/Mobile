package es.eina.hopper.models;

import java.io.Serializable;
import java.util.*;

public class IngreRecipe implements Serializable {
    private PkIngreRecipe id;
    private long quantity;

    public PkIngreRecipe getIngreRecipe() {
        return id;
    }

    public void setIngreRecipe(PkIngreRecipe id) {
        this.id = id;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

}
