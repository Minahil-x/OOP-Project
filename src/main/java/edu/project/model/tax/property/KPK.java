package edu.project.model.tax.property;

import edu.project.exceptions.ValidationError;

public class KPK extends Property{
    public   KPK(double urbanArea, double agriArea) throws ValidationError {
        if (urbanArea < 0 || agriArea < 0) {
            throw new ValidationError("Values can not be negative.");
        }
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
        tax.add(String.format(">%.2f PKR - ETAND for Urban Property in KPK", calculateUrbanTax()));
        tax.add(String.format(">%.2f PKR - RED for Agricultural Property in KPK", calculateAgriTax()));
    }
    @Override
    public String display(){
        StringBuilder taxes = new StringBuilder();
        for(String str : tax){
            taxes.append(str).append("\n");
        }
        return taxes.toString() + "\n";
    }

    @Override
    public String toString(){
        return String.format("Khyber Pakhtunkhwa (KPK):" +
                "\n  Urban Property Valuation: %.2f" +
                "\n  Agricultural Property Valuation: " +
                "\nTotal Valuation: " +
                "\n  Urban Tax: " +
                "\n  Agricultural Tax: " +
                "\nTotal Tax: ", calculateUrbanValuation(), calculateAgriValuation(), calculateValuation(),
                calculateUrbanTax(), calculateAgriTax(), calculateTax());
    }
}
