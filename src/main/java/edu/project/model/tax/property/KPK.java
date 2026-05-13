package edu.project.model.tax.property;

import edu.project.model.user.TaxPayer;

public class KPK extends Property{
    public   KPK(double urbanArea, double agriArea) {
        super(urbanArea, agriArea);
        save();
    }

    @Override
    public double calculateUrbanTax(){
        return 30000*16*urbanArea; //average rate of 20,000 - 40,000 pkr per 10 marla of urban property | 1 acre = 160 marla
    }

    @Override
    public double calculateAgriTax(){
        if(agriArea > 50) {
            return 900*(agriArea-50);//large land holdings
        }
        else {
            return 0;//exempt
        }
    }

    @Override
    public double calculateUrbanValuation(){
        return urbanArea*120000000;//rates of 3 crore to 40 crore per acre
    }
    @Override
    public double calculateAgriValuation(){
        return agriArea*10000000;// avg of 20 lakh to 4.5 crore per acre
    }

    @Override
    public double calculateTax(){
        return calculateAgriTax() + calculateUrbanTax();
    }
    @Override
    public void save(){
        if(!tax.isEmpty()) return;
        tax.add(String.format("%.2f PKR to Excise, Taxation & Narcotics Control Department for Urban Property in KPK", calculateUrbanTax()));
        tax.add(String.format("%.2f PKR to Revenue & Estate Department for Agricultural Property in KPK", calculateAgriTax()));
    }
    @Override
    public String display(){
        StringBuilder taxes = new StringBuilder();
        for(String str : tax){
            taxes.append(str).append("\n");
        }
        return taxes.toString();
    }

    @Override
    public String toString(){
        return "Khyber Pakhtunkhwa (KPK):" +
                "\n  Urban Property Valuation: " + calculateUrbanValuation() +
                "\n  Agricultural Property Valuation: " + calculateAgriValuation() +
                "\nTotal Valuation: " + calculateValuation() +
                "\n  Urban Tax: " +  calculateUrbanTax() +
                "\n  Agricultural Tax: " +  calculateAgriTax() +
                "\nTotal Tax: " + calculateTax();
    }
}
