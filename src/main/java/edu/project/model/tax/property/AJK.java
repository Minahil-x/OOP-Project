package edu.project.model.tax.property;

import edu.project.model.user.TaxPayer;

public class AJK extends Property{
    public   AJK(double urbanArea, double agriArea) {
        super(urbanArea, agriArea);
    }

    //todo: methods
    @Override
    public double calculateUrbanTax(){
        return 350000*urbanArea; //average rate of 200,00 pkr - 600,00 pkr per acre of urban property
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
        tax.add(calculateUrbanTax() + " to Inland Revenue Department for Urban Property in AJK");
        tax.add(calculateAgriTax() + " to Inland Department for Agricultural Property in AJK");
        return calculateAgriTax() + calculateUrbanTax();
    }
    public double calculateTax(){
        tax.add(calculateUrbanTax() + " to Inland Revenue Department for Urban Property in AJK");
        tax.add(calculateAgriTax() + " to Inland Department for Agricultural Property in AJK");
        return calculateAgriTax() + calculateUrbanTax();
    }

    @Override
    public String toString(){
        return "AJK Inland Revenue Department:\nUrban Property Valuation: " + urbanArea*20000000 + "\nAgricultural Property Valuation: " + agriArea*1500000 +
                "\nTotal Valuation: " + calculateValuation() + "\nUrban Tax: " +  calculateUrbanTax() +
                "\nAgri Tax: " +  calculateAgriTax() + "\nTotal Tax: " + calculateTax();
    }
}
