package edu.project.model.tax.income;

public class SalariedAndBusiness extends Income{
  private double annualSalary;
  private double taxD;

  public SalariedAndBusiness(double annualSalary){
    super();
    this.annualSalary = annualSalary;
  }

  public void setAnnualSalary(double annualSalary){this.annualSalary = annualSalary;}

  public double getAnnualSalary(){return annualSalary;}

  @Override
  public double calculateTax(TaxPayer payer){
    
  }
}
