package edu.project.model.tax.property;

import edu.project.model.user.TaxPayer;

public class AJK extends Property{
    public   AJK(double valuation, double urbanArea, double agriArea) {
        super(valuation, urbanArea, agriArea);
    }

    //todo: methods
    @Override
    public double calculateUrbanTax(){
        return 0;
    }

    @Override
    public double calculateAgriTax(){
        return 0;
    }

    @Override
    public double calculateValuation(){
        return 0;
    }

    @Override
    public double calculateTax(TaxPayer payer){
        return 0;
    }

}
