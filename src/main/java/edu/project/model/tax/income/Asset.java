package edu.project.model.tax.income;

import edu.project.model.user.TaxPayer;

public class Asset extends Income{
    private double bankInvestment;
    private double equityInvestment;
    private double taxD;

    public Asset(double bankInvestment, double equityInvestment) {
        this.bankInvestment = bankInvestment;
        this.equityInvestment = equityInvestment;
    }

    public double getBankInvestment() {return bankInvestment;}
    public double getEquityInvestment() {return equityInvestment;}

    public void setEquityInvestment(double annualDividend) {this.equityInvestment = annualDividend;}
    public void setBankInvestment(double bankInvestment) {this.bankInvestment = bankInvestment;}

    @Override
    public double calculateValuation() {
        return bankInvestment + equityInvestment;
    }

    @Override
    public double calculateTax(TaxPayer payer) {
        double intrest = bankInvestment * 0.12; // 11.5 - 12.5 % interest rates
        double dividend = equityInvestment * 0.24; // 12 % 6-month returns

        if(!payer.getFilerStatus()){
            intrest *= 2;
        }
        if (payer.getRegion().equals("punjab")) {
            intrest *= 0.2;
        }
        else {
            intrest *= 0.15;
        }
        dividend *= 0.15;
        if(payer.getRegion().equals("gb")){return 0;} //special status
        taxD = intrest + dividend;
        return taxD;
    }

    @Override
    public void save() {
        if(!tax.isEmpty()) return;
        tax.add(String.format("%.2f PKR to Federal Board of Revenue (FBR) for Investment Income", taxD));
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
                "\n Bank Investment: " + bankInvestment +
                "\n Equity Investment: " + equityInvestment +
                "\nTotal Valuation: " + calculateValuation() +
                "\nTotal Tax: " + taxD;
    }
}
