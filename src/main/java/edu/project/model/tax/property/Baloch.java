package edu.project.model.tax.property;

import edu.project.exceptions.ValidationError;

public class Baloch extends Property {
    public  Baloch(double urbanArea, double agriArea) throws ValidationError {
        if (urbanArea < 0 || agriArea < 0) {
            throw new ValidationError("Values can not be negative.");
        }
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
            return 3500*(agriArea-50) + 1200*50;//large land holdings
        }
        else if(agriArea > 12.5) {
            return 1200*agriArea;//average of 1000-2000pkr per acre
        }
        else {
            return 0;//exempt
        }
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
    public double calculateTax(){
        return calculateAgriTax() + calculateUrbanTax();
    }
    @Override
    public void save(){
        if(!tax.isEmpty()) return;
        tax.add(String.format(">%.2f PKR - ETAND for Urban Property in Balochistan", calculateUrbanTax()));
        tax.add(String.format(">%.2f PKR - BOR for Agricultural Property in Balochistan", calculateAgriTax()));
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
        return String.format("Balochistan BRA(Balochistan Revenue Authority):" +
                "\n  Urban Property Valuation: %.2f" +
                "\n  Agricultural Property Valuation: %.2f" +
                "\nTotal Valuation: %.2f" +
                "\n  Urban Tax: %.2f" +
                "\n  Agricultural Tax: %.2f" +
                "\nTotal Tax: %.2f", calculateUrbanValuation(), calculateAgriValuation(), calculateValuation(), calculateUrbanTax(), calculateAgriTax(), calculateTax());
    }
}
