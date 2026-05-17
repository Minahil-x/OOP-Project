package edu.project.model.tax.property;

import edu.project.exceptions.ValidationException;

public class Punjab extends Property{
    private double irrigatedPercent;
    private double coveredUrbanAreaPercent;

    public   Punjab(double urbanArea, double agriArea, double irrigatedPercent, double coveredUrbanAreaPercent) throws ValidationException {
        if (urbanArea < 0 || agriArea < 0) {
            throw new ValidationException("Values can not be negative.");
        }
        if (irrigatedPercent < 0 || irrigatedPercent > 1 || coveredUrbanAreaPercent < 0  || coveredUrbanAreaPercent > 1) {
            throw new ValidationException("Percentage must be between 0 and 1.");
        }
        super(urbanArea, agriArea);

        this.irrigatedPercent = irrigatedPercent;
        this.coveredUrbanAreaPercent = coveredUrbanAreaPercent;
        save();
    }

    @Override
    public double calculateUrbanTax(){
        double landARV = (urbanArea * 4840) * 23; //land monthly rental value is 23 per square yard | 1 acre = 4840 sq yards
        double coveredAreaSqFeet = urbanArea * coveredUrbanAreaPercent * 43560;
        double buildingARV = coveredAreaSqFeet; // building monthly rental value is calculated by sq feet
        if (coveredAreaSqFeet > 300){
            buildingARV *= 19; // decreased rates for larger buildings
        }
        else {
            buildingARV *= 23; // standard for buildings <= 300 sq feet
        }

        return ((landARV + buildingARV) * 12 * 0.9) * 0.05; // 12 for months | 0.9 for 10 percent deduction to get Net ARV and 0.05 for 5 % tax rate
    }

    @Override
    public double calculateAgriTax(){
        double irrigatedLand = agriArea * irrigatedPercent;
        double unirrigatedLand = agriArea - irrigatedLand;
        if (agriArea > 50){
            return ((irrigatedLand + (unirrigatedLand/2)) - 50) * 500 + (25 * 400) + (12.5 * 300); //large holdings
        }
        else if(agriArea > 25) {
            return ((irrigatedLand + (unirrigatedLand/2)) - 25) * 400 + (12.5 * 300);//medium land holdings
        }
        else if(agriArea > 12.5) {
            return ((irrigatedLand + (unirrigatedLand/2)) - 12.5) * 300;// small land holdings
        }
        else {
            return 0;//exempt
        }
    }

    @Override
    public double calculateUrbanValuation(){
        double landValue = urbanArea * 9000000;
        double buildingValue = (urbanArea * coveredUrbanAreaPercent) * 100000000;
        return landValue + buildingValue;//rates of 32 million to 720 million per acre
    }
    @Override
    public double calculateAgriValuation(){
        double irrigatedValue = (agriArea * irrigatedPercent) * 8000000; // avg of 8 Million PKR per acre
        double unirrigatedValue = (agriArea - (agriArea * irrigatedPercent)) * 1000000; // avg of 10 lakh per acre
        return irrigatedValue + unirrigatedValue;
    }

    @Override
    public double calculateTax(){
        return calculateAgriTax() + calculateUrbanTax();
    }
    @Override
    public void save(){
        if(!tax.isEmpty()) return;
        tax.add(String.format(">%.2f PKR - ETAND for Urban Property in Punjab", calculateUrbanTax()));
        tax.add(String.format(">%.2f PKR - BOR for Agricultural Property in Punjab", calculateAgriTax()));
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
        return String.format("Punjab Excise, Taxation & Narcotics Control Department (ETND):" +
                "\n  Urban Property Valuation: %.2f" +
                "\n  Agricultural Property Valuation: %.2f" +
                "\nTotal Valuation: %.2f" +
                "\n  Urban Tax: %.2f" +
                "\n  Agricultural Tax: %.2f" +
                "\nTotal Tax: %.2f", calculateUrbanValuation(), calculateAgriValuation(), calculateValuation(), calculateUrbanTax(),  calculateAgriTax(), calculateTax());
    }
}
