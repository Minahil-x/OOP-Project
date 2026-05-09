package edu.project.model.tax;

import edu.project.model.user.TaxPayer;

import java.util.ArrayList;

abstract public class Taxable {
    protected double valuation;
    protected ArrayList<String> tax;

    public  Taxable(double valuation) {
        this.valuation = valuation;
    }

    abstract public double calculateValuation();
    abstract public double calculateTax(TaxPayer payer);

    public ArrayList<String> getTaxes(){return tax;}

    public void setTaxes(ArrayList<String> tax){this.tax = tax;}
}
