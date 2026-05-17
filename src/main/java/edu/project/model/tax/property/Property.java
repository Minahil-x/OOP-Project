package edu.project.model.tax.property;

import edu.project.model.tax.Taxable;

public abstract class Property extends Taxable{
    protected double urbanArea;
    protected double agriArea;

    public Property(double urbanArea, double agriArea) {
        super();
        this.urbanArea = urbanArea;
        this.agriArea = agriArea;
    }

    abstract public double calculateUrbanTax();
    abstract public double calculateAgriTax();
    abstract  public double calculateUrbanValuation();
    abstract public double calculateAgriValuation();
    @Override
    public double calculateValuation(){
        double sum = 0;
        sum += calculateUrbanValuation();
        sum += calculateAgriValuation();
        return sum;
    }

}
