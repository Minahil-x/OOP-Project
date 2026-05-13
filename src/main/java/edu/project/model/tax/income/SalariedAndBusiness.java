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
  public double calculateTax(){
    
  }

  public double getSalaryTax(){
    if(annualSalary <= 600,000){
      return 0;
    }else if(annualSalary <= 1,200,000){
      return (annualSalary - 600,000) * 0.01;
    }else if(annualSalary <= 2,200,000){
      return (annualSalary - 1,200,00) * 0.11 + 600,000 * 0.01;
    }else if (annualSalary <= 3,200,000){
      return (annualSalary - 2,200,00) * 0.23 + 1,000,00 * 0.11 + 600,000 * 0.01;
    }
  }
}
