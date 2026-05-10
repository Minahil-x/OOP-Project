package edu.project.model.tax.property;

import edu.project.model.user.TaxPayer;

public class GB extends Property{
    public   GB(double urbanArea, double agriArea) {
        super(urbanArea, agriArea);
        save();
    }

    @Override
    public double calculateUrbanTax(){
        return 0; //only sales tax
    }

    @Override
    public double calculateAgriTax(){
            return 0;//exempt
    }

    @Override
    public double calculateValuation(){
        double sum = 0;
        sum += calculateUrbanValuation();
        sum += calculateAgriValuation();
        return sum;
    }
    @Override
    public double calculateUrbanValuation(){
        return urbanArea*32000000;//DC rate of 40 lakh per kanal x 8
    }
    @Override
    public double calculateAgriValuation(){
        return agriArea*3000000;// avg of 3 lakh to 2 crore per acre
    }

    @Override
    public double calculateTax(TaxPayer payer){
        payer.addTaxable(this);
        return calculateTax();
    }
    public double calculateTax(){
        return calculateAgriTax() + calculateUrbanTax();
    }
    @Override
    public void save(){
        if(!tax.isEmpty()) return;
        tax.add(String.format("%.2f PKR  for Urban Property in Gilgit Baltistan", calculateUrbanTax()));
        tax.add(String.format("%.2f PKR for Agricultural Property in Gilgit Baltistan", calculateAgriTax()));
    }
    @Override
    public String display(){
        if(tax.isEmpty()) save();
        StringBuilder taxes = new StringBuilder();
        for(String str : tax){
            taxes.append(str).append("\n");
        }
        return taxes.toString();
    }

    @Override
    public String toString(){
        return "Gilgit Baltistan DC:" +
                "\n  Urban Property Valuation: " + calculateUrbanValuation() +
                "\n  Agricultural Property Valuation: " + calculateAgriValuation() +
                "\nTotal Valuation: " + calculateValuation() +
                "\n  Urban Tax: " +  calculateUrbanTax() +
                "\n  Agri Tax: " +  calculateAgriTax() +
                "\nTotal Tax: " + calculateTax();
    }
}
