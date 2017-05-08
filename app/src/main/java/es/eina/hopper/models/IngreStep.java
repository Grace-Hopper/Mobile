package es.eina.hopper.models;

import java.io.Serializable;
import java.util.*;


public class IngreStep implements Serializable {
    private PkIngreStep id;
    private long quantity;

    public PkIngreStep getIngreStep() {
        return id;
    }

    public void setIngreStep(PkIngreStep id) {
        this.id = id;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

}
