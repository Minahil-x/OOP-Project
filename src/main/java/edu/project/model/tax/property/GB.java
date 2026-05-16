package edu.project.model.tax.property;

import edu.project.exceptions.ValidationError;

public class GB extends Property{
    public   GB(double urbanArea, double agriArea) throws ValidationError {
        if (urbanArea < 0 || agriArea < 0) {
            throw new ValidationError("Values can not be negative.");
        }
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
    public double calculateUrbanValuation(){
        return urbanArea*32000000;//DC rate of 40 lakh per kanal x 8
    }
    @Override
    public double calculateAgriValuation(){
        return agriArea*3000000;// avg of 3 lakh to 2 crore per acre
    }

    @Override
    public double calculateTax(){
        return calculateAgriTax() + calculateUrbanTax();
    }
    @Override
    public void save(){
        if(!tax.isEmpty()) return;
        tax.add(String.format(">%.2f PKR - Urban Property exempt in Gilgit Baltistan", calculateUrbanTax()));
        tax.add(String.format(">%.2f PKR - Agricultural Property exempt in Gilgit Baltistan", calculateAgriTax()));
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
        return String.format("Gilgit Baltistan DC:" +
                "\n  Urban Property Valuation: %.2f" +
                "\n  Agricultural Property Valuation: %.2f" +
                "\nTotal Valuation: %.2f" +
                "\n  Urban Tax: %.2f" +
                "\n  Agricultural Tax: %.2f" +
                "\nTotal Tax: %.2f", calculateUrbanValuation(), calculateAgriValuation(), calculateValuation(), calculateUrbanTax(),  calculateAgriTax(), calculateTax());
    }
}
