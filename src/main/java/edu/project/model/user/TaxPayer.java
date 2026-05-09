package edu.project.model.user;

import edu.project.model.tax.Taxable;

import java.util.ArrayList;

public class TaxPayer extends User{
    private boolean filerStatus;
    private String region;
    private ArrayList<Taxable> taxables;

    public TaxPayer(String id, String password, boolean filerStatus, String region) {
        super(id, password);
        this.filerStatus = filerStatus;
        this.region = region;
        this.taxables = new ArrayList<>();
    }

    public ArrayList<Taxable> getTaxables() {return taxables;}
    public boolean getFilerStatus() {return filerStatus;}
    public String getRegion() {return region;}

    public void setFilerStatus(boolean filerStatus) {this.filerStatus = filerStatus;}
    public void setRegion(String region) {this.region = region;}

    public void addTaxable(Taxable taxable){this.taxables.add(taxable);}

    public String totalTax(){
        String totalTax = "";
        for(Taxable taxable : taxables){

        }
        return totalTax;
    }

    public double totalTaxAmount(){
        double totalTaxAmount = 0;
        for(Taxable taxable : taxables){

        }
        return totalTaxAmount;
    }

    public double totalValue(){
        double totalValue = 0;
        for(Taxable taxable : taxables){

        }
        return totalValue;
    }
}
