package edu.project.model.tax;

import java.util.ArrayList;

abstract public class Taxable {
    protected ArrayList<String> tax;

    public  Taxable() {
        tax = new ArrayList<>();
    }

    abstract public double calculateValuation();
    abstract public double calculateTax();
    abstract public void save();
    abstract public String display();
    abstract public String toString();

    public ArrayList<String> getTaxes(){return tax;}

    public void setTaxes(ArrayList<String> tax){this.tax = tax;}

}
