package edu.project.model.tax.income;

import edu.project.exceptions.ValidationException;
import edu.project.model.tax.Taxable;

public class SalariedAndBusiness extends Taxable {
    private double annualSalary;
    private double annualBusinessIncome;

    public SalariedAndBusiness(double annualSalary, double annualBusinessIncome) throws ValidationException {
        if (annualSalary < 0 || annualBusinessIncome < 0) {
            throw new ValidationException("Values can not be negative.");
        }
        super();
        this.annualBusinessIncome = annualBusinessIncome;
        this.annualSalary = annualSalary;
    }

    @Override
    public double calculateValuation() {
        return annualSalary + annualBusinessIncome;
    }

    @Override
    public double calculateTax(){
        return getBusinessTax() + getSalaryTax();
    }

    @Override
    public void save() {
        tax.add(String.format(">%.2f PKR - FBR for Business Income", getBusinessTax()));
        tax.add(String.format(">%.2f PKR - FBR for Salaried Income", getSalaryTax()));
    }

    @Override
    public String display() {
        StringBuilder taxes = new StringBuilder();
        for(String str : tax){
            taxes.append(str).append("\n");
        }
        return taxes.toString() + "\n";
    }

    @Override
    public String toString() {
        return String.format("FBR Pakistan:" +
                "\n Business Income: %.2f" +
                "\n Annual Salary: %.2f" +
                "\nTotal Valuation: %.2f" +
                "\nTotal Tax: %.2f", annualBusinessIncome, annualSalary,  calculateValuation(), calculateTax());
    }

    public double getSalaryTax(){
        if(annualSalary <= 600000){
            return 0;
        }else if(annualSalary <= 1200000){
            return (annualSalary - 600000) * 0.01;
        }else if(annualSalary <= 2200000){
            return (annualSalary - 1200000) * 0.11 + 6000;
        }else if (annualSalary <= 3200000){
            return (annualSalary - 2200000) * 0.23 + 116000;
        }else if(annualSalary <= 4100000){
            return (annualSalary - 3200000) * 0.3 + 346000;
        }else{
            return (annualSalary - 4100000) * 0.35 + 616000;
        }
    }
    public double getBusinessTax(){
        if(annualBusinessIncome <= 600000){
            return 0;
        }else if(annualBusinessIncome<= 1200000){
            return (annualBusinessIncome - 600000) * 0.05;
        }else if(annualBusinessIncome <= 2200000){
            return (annualBusinessIncome - 1200000) * 0.15 + 30000;
        }else if (annualBusinessIncome <= 3200000){
            return (annualBusinessIncome - 2200000) * 0.25 + 180000;
        }else if(annualBusinessIncome <= 4100000){
            return (annualBusinessIncome - 3200000) * 0.3 + 510000;
        }else{
            return (annualBusinessIncome - 4100000) * 0.35 + 1230000;
        }
    }
}