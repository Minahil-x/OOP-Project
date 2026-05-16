package edu.project.model.tax.property;

import edu.project.exceptions.ValidationError;

public class Sindh extends Property{
    private double irrigatedPercent;
    private double coveredUrbanAreaPercent;

    public   Sindh(double urbanArea, double agriArea,double irrigatedPercent, double coveredUrbanAreaPercent) throws ValidationError {
        if (urbanArea < 0 || agriArea < 0) {
            throw new ValidationError("Values can not be negative.");
        }
        if (irrigatedPercent < 0 || irrigatedPercent > 1 || coveredUrbanAreaPercent < 0  || coveredUrbanAreaPercent > 1) {
            throw new ValidationError("Percentage must be between 0 and 1.");
        }
        super(urbanArea, agriArea);
        this.irrigatedPercent = irrigatedPercent;
        this.coveredUrbanAreaPercent = coveredUrbanAreaPercent;
        save();
    }

    public double getCoveredUrbanAreaPercent() {return coveredUrbanAreaPercent;}
    public double getIrrigatedPercent() {return irrigatedPercent;}

    public void setIrrigatedPercent(double irrigatedPercent) {this.irrigatedPercent = irrigatedPercent;}
    public void setCoveredUrbanAreaPercent(double coveredUrbanAreaPercent){this.coveredUrbanAreaPercent = coveredUrbanAreaPercent;}

    @Override
    public double calculateUrbanTax(){
        double landARV = (urbanArea * 4840) * 4; //land monthly rental value is 4 per square yard | 1 acre = 4840 sq yards
        double coveredAreaSqFeet = urbanArea * coveredUrbanAreaPercent * 43560;
        double buildingARV = coveredAreaSqFeet * 4; // building monthly rental value is calculated by sq feet

        return ((landARV + buildingARV) * 12 * 0.9) * 0.25; // 12 for months | 0.9 for 10 percent deduction for maintenance and 0.25 for 25 % tax rate
    }

    @Override
    public double calculateAgriTax(){
        return 0; //abolished
    }

    @Override
    public double calculateUrbanValuation(){
        double landValue = urbanArea * 5000000;
        double buildingValue = (urbanArea * coveredUrbanAreaPercent) * 40000000;
        return landValue + buildingValue;//rates of 20 million to 250 million per acre
    }
    @Override
    public double calculateAgriValuation(){
        double irrigatedValue = (agriArea * irrigatedPercent) * 5000000; // avg of 5 Million PKR per acre
        double unirrigatedValue = (agriArea - (agriArea * irrigatedPercent)) * 600000; // avg of 6 lakh per acre
        return irrigatedValue + unirrigatedValue;
    }

    @Override
    public double calculateTax(){
        return calculateAgriTax() + calculateUrbanTax();
    }
    @Override
    public void save(){
        if(!tax.isEmpty()) return;
        tax.add(String.format(">%.2f PKR - ETAND for Urban Property in Sindh", calculateUrbanTax()));
        tax.add(String.format(">%.2f PKR - Sindh has abolished land tax for Agricultural Property", calculateAgriTax()));
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
        return String.format("Sindh Excise, Taxation & Narcotics Control Department (ETND):" +
                "\n  Urban Property Valuation: %.2f" +
                "\n  Agricultural Property Valuation: %.2f" +
                "\nTotal Valuation: %.2f" +
                "\n  Urban Tax: %.2f" +
                "\n  Agricultural Tax: %.2f" +
                "\nTotal Tax: %.2f", calculateUrbanValuation(), calculateAgriValuation(), calculateValuation(), calculateUrbanTax(), calculateAgriTax(), calculateTax());
    }
}
