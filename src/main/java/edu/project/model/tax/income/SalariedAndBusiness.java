package edu.project.model.tax.income;

public class SalariedAndBusiness extends Income{
  private double annualSalary;

  public SalariedAndBusiness(double annualSalary){
    super();
    this.annualSalary = annualSalary;
  }

  public void setAnnualSalary(double annualSalary){this.annualSalary = annualSalary;}

  public double getAnnualSalary(){return annualSalary;}
}
