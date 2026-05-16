package edu.project.model.tax.property;

import edu.project.exceptions.ValidationError;

public class ICT extends Property{
    public   ICT(double urbanArea, double agriArea) throws ValidationError {
        if (urbanArea < 0 || agriArea < 0) {
            throw new ValidationError("Values can not be negative.");
        }
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
        tax.add(String.format(">%.2f PKR - MCI for Urban Property in Islamabad Capital Territory", calculateUrbanTax()));
        tax.add(String.format(">%.2f PKR - MCI for Farmhouse Property in Islamabad Capital Territory", calculateAgriTax()));
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
        return String.format("Metropolitan Corporation Islamabad (MCI):" +
                "\n  Urban Property Valuation: %.2f" +
                "\n  Farmhouse Valuation: %.2f" +
                "\nTotal Valuation: %.2f" +
                "\n  Urban Tax: %.2f" +
                "\n  Farmhouse Tax: %.2f" +
                "\nTotal Tax: %.2f", calculateUrbanValuation(), calculateAgriValuation(), calculateValuation(), calculateUrbanTax(), calculateAgriTax(), calculateTax());
    }
}
