package edu.project.model.tax.income;

import edu.project.model.tax.Taxable;

public class SalariedAndBusiness extends Taxable {
    private double annualSalary;
    private double annualBusinessIncome;

    public SalariedAndBusiness(double annualSalary, double annualBusinessIncome) {
        super();
        this.annualBusinessIncome = annualBusinessIncome;
        this.annualSalary = annualSalary;
    }

    public void setAnnualSalary(double annualSalary){this.annualSalary = annualSalary;}
    public void setAnnualBusinessIncome(double annualBusinessIncome){this.annualBusinessIncome = annualBusinessIncome;}

    public double getAnnualSalary(){return annualSalary;}
    public double getAnnualBusinessIncome(){return annualBusinessIncome;}

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
        tax.add(String.format("%.2f PKR to Federal Board of Revenue (FBR) for Business Income", getBusinessTax()));
        tax.add(String.format("%.2f PKR to Federal Board of Revenue (FBR) for Salaried Income", getSalaryTax()));
    }

    @Override
    public String display() {
        StringBuilder taxes = new StringBuilder();
        for(String str : tax){
            taxes.append(str).append("\n");
        }
        return taxes.toString();
    }

    @Override
    public String toString() {
        return "FBR Pakistan:" +
                "\n Business Income: " + annualBusinessIncome +
                "\n Annual Salary: " + annualSalary +
                "\nTotal Valuation: " + calculateValuation() +
                "\nTotal Tax: " + calculateTax();
    }

    public double getSalaryTax(){
        if(annualSalary <= 600000){
            return 0;
        }else if(annualSalary <= 1200000){
            return (annualSalary - 600000) * 0.01;
        }else if(annualSalary <= 2200000){
            return (annualSalary - 120000) * 0.11 + 6000;
        }else if (annualSalary <= 3200000){
            return (annualSalary - 220000) * 0.23 + 11600;
        }else if(annualSalary <= 4100000){
            return (annualSalary - 320000) * 0.3 + 346000;
        }else{
            return (annualSalary - 410000) * 0.35 + 616000;
        }
    }
    public double getBusinessTax(){
        if(annualBusinessIncome <= 600000){
            return 0;
        }else if(annualBusinessIncome<= 1200000){
            return (annualBusinessIncome - 600000) * 0.05;
        }else if(annualBusinessIncome <= 2200000){
            return (annualBusinessIncome - 120000) * 0.15 + 30000;
        }else if (annualBusinessIncome <= 3200000){
            return (annualBusinessIncome - 220000) * 0.25 + 210000;
        }else if(annualBusinessIncome <= 4100000){
            return (annualBusinessIncome - 320000) * 0.3 + 510000;
        }else{
            return (annualBusinessIncome - 410000) * 0.35 + 1230000;
        }
    }
}