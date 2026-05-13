package edu.project.model.tax.property;

import edu.project.model.user.TaxPayer;

public class ICT extends Property{
    public   ICT(double urbanArea, double agriArea) {
        super(urbanArea, agriArea);
        save();
    }

    @Override
    public double calculateUrbanTax(){
        if (urbanArea <= 0.028){
            return 24000;
        }
        else if (urbanArea <= 0.06){
            return 70000;
        } else if (urbanArea <= 0.12) {
            return 250000;
        }
        return 2000000*(urbanArea - 0.12) + 250000;
    }

    @Override
    public double calculateAgriTax(){
        return 200000*agriArea; //farmhouses
    }

    @Override
    public double calculateUrbanValuation(){
        return urbanArea*1500000000;//rates of 870 million to 3 billion PKR per acre
    }
    @Override
    public double calculateAgriValuation(){
        return agriArea*110000000;// avg of 12 million per kanal
    }

    @Override
    public double calculateTax(){
        return calculateAgriTax() + calculateUrbanTax();
    }
    @Override
    public void save(){
        if(!tax.isEmpty()) return;
        tax.add(String.format("%.2f PKR to Metropolitan Corporation Islamabad for Urban Property in Islamabad Capital Territory", calculateUrbanTax()));
        tax.add(String.format("%.2f PKR to Metropolitan Corporation Islamabad for Farmhouse Property in Islamabad Capital Territory", calculateAgriTax()));
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
        return "Metropolitan Corporation Islamabad (MCI):" +
                "\n  Urban Property Valuation: " + calculateUrbanValuation() +
                "\n  Farmhouse Valuation: " + calculateAgriValuation() +
                "\nTotal Valuation: " + calculateValuation() +
                "\n  Urban Tax: " +  calculateUrbanTax() +
                "\n  Farmhouse Tax: " +  calculateAgriTax() +
                "\nTotal Tax: " + calculateTax();
    }
}
