package edu.project.model.tax;

import java.io.Serializable;
import java.util.ArrayList;

abstract public class Taxable implements Serializable {
    protected ArrayList<String> tax;

    public  Taxable() {
        tax = new ArrayList<>();
    }

    public ArrayList<String> getTaxes(){return tax;}

    public void setTaxes(ArrayList<String> tax){this.tax = tax;}

    abstract public double calculateValuation();
    abstract public double calculateTax();
    abstract public void save();
    abstract public String display();
    abstract public String toString();


}
