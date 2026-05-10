package edu.project.model.tax.property;

import edu.project.model.user.TaxPayer;

public class AJK extends Property{
    public   AJK(double urbanArea, double agriArea) {
        super(urbanArea, agriArea);
        save();
    }

    @Override
    public double calculateUrbanTax(){
        return 35000*urbanArea; //average rate of 20,000 pkr - 60,000 pkr per acre of urban property
    }

    @Override
    public double calculateAgriTax(){
        if(agriArea > 25) {
            return 450*agriArea;//large land holdings
        }
        else if(agriArea > 12.5) {
            return 150*agriArea;//average of 100-300pkr per acre
        }
        else {
            return 0;//exempt
        }
    }

    @Override
    public double calculateValuation(){
        double sum = 0;
        sum += urbanArea*20000000;//rates of 0.5 crore to 10 crore
        sum += agriArea*1500000;// avg of 4 lakh to 40 lakh per acre
        return sum;
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
        tax.add(String.format("%.2f PKR to Inland Revenue Department for Urban Property in AJK", calculateUrbanTax()));
        tax.add(String.format("%.2f PKR to Inland Revenue Department for Agricultural Property in AJK", calculateAgriTax()));
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
        return "AJK Inland Revenue Department:" +
                "\n  Urban Property Valuation: " + urbanArea*20000000 +
                "\n  Agricultural Property Valuation: " + agriArea*1500000 +
                "\nTotal Valuation: " + calculateValuation() +
                "\n  Urban Tax: " +  calculateUrbanTax() +
                "\n  Agri Tax: " +  calculateAgriTax() +
                "\nTotal Tax: " + calculateTax();
    }
}
