package es.eina.hopper.models;

import java.io.Serializable;
import java.util.*;

public class Step implements Serializable {
    private PkStep id;
    private long timer;
    private String information;
    private List<Utensil> utensils = new ArrayList();
    private List<IngreStep> steps = new ArrayList();

    public PkStep getStep() {
        return id;
    }

    public void setStep(PkStep id) {
        this.id = id;
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

    public List<IngreStep> getIngreSteps() {
        return steps;
    }

}
