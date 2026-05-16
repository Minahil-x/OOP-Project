package edu.project.model.tax.property;

import edu.project.exceptions.ValidationError;

public class AJK extends Property{
    public   AJK(double urbanArea, double agriArea) throws ValidationError {
        if (urbanArea < 0 || agriArea < 0) {
            throw new ValidationError("Values can not be negative.");
        }
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
            return 450*(agriArea-25) + 150*25;//large land holdings
        }
        else if(agriArea > 12.5) {
            return 150*(agriArea - 12.5);//average of 100-300pkr per acre
        }
        else {
            return 0;//exempt
        }
    }

    @Override
    public double calculateUrbanValuation(){
        return urbanArea*20000000;//rates of 0.5 crore to 10 crore
    }
    @Override
    public double calculateAgriValuation(){
        return agriArea*1500000;// avg of 4 lakh to 40 lakh per acre
    }

    @Override
    public double calculateTax(){
        return calculateAgriTax() + calculateUrbanTax();
    }
    @Override
    public void save(){
        if(!tax.isEmpty()) return;
        tax.add(String.format(">%.2f PKR - IRD for Urban Property in AJK", calculateUrbanTax()));
        tax.add(String.format(">%.2f PKR - IRD for Agricultural Property in AJK", calculateAgriTax()));
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
        return String.format("AJK Inland Revenue Department:" +
                "\n  Urban Property Valuation: %.2f" +
                "\n  Agricultural Property Valuation: %.2f" +
                "\nTotal Valuation: %.2f" +
                "\n  Urban Tax: %.2f" +
                "\n  Agricultural Tax: %.2f" +
                "\nTotal Tax: %.2f", calculateUrbanValuation(), calculateAgriValuation(), calculateValuation(), calculateUrbanTax(),
                calculateAgriTax(), calculateTax());
    }
}
