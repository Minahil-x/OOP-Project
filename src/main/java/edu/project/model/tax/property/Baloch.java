package edu.project.model.tax.property;

import edu.project.model.user.TaxPayer;

public class Baloch extends Property {
    public  Baloch(double urbanArea, double agriArea) {
        super(urbanArea, agriArea);
        save();
    }

    @Override
    public double calculateUrbanTax(){
        if (urbanArea <= 0.05) return 0;
        if (urbanArea <= 0.2) return 5000*urbanArea;
        if (urbanArea <= 0.5) return 7000*urbanArea;
        return 10000*urbanArea;
    }

    @Override
    public double calculateAgriTax(){
        if(agriArea > 50) {
            return 3500*agriArea;//large land holdings
        }
        else if(agriArea > 12.5) {
            return 1200*agriArea;//average of 100-300pkr per acre
        }
        else {
            return 0;//exempt
        }
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
        return urbanArea*10000000;//rates of 0.25 crore to 2 crore
    }
    @Override
    public double calculateAgriValuation(){
        return agriArea*250000;// avg of 0.4 lakh to 10 lakh per acre
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
        tax.add(String.format( "%.2f PKR to Excise, Taxation and Anti-Narcotics Department for Urban Property in Balochistan", calculateUrbanTax()));
        tax.add(String.format("%.2f PKR to BOR Balochistan for Agricultural Property in Balochistan", calculateAgriTax()));
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
        return "Balochistan BRA(Balochistan Revenue Authority):" +
                "\n  Urban Property Valuation: " + calculateUrbanValuation() +
                "\n  Agricultural Property Valuation: " + calculateAgriValuation() +
                "\nTotal Valuation: " + calculateValuation() +
                "\n  Urban Tax: " +  calculateUrbanTax() +
                "\n  Agri Tax: " +  calculateAgriTax() +
                "\nTotal Tax: " + calculateTax();
    }
}
