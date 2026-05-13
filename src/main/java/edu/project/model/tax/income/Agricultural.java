package edu.project.model.tax.income;

import edu.project.model.user.TaxPayer;

public class Agricultural extends Taxable{
    protected Map<String, Double> regionalIncome;
    
    public Agricultural(){
        super();
        regionalIncome = new HashMap<String, Double>();
        regionalIncome.put("punjab", 0.0);
        regionalIncome.put("sindh", 0.0);
        regionalIncome.put("baloch", 0.0);
        regionalIncome.put("kpk", 0.0);
        regionalIncome.put("ict", 0.0);
        regionalIncome.put("ajk", 0.0);
        regionalIncome.put("gb", 0.0);
    }

    public Map<String, Double> getRegionalIncome(){
        return regionalIncome;
    }

    public void addRegionalIncome(String region, Double regionalIncomeValue){
        this.regionalIncome.put(region.toLowerCase(), regionalIncomeValue);
    }

    public double getIncomeByRegion(String region){
        return regionalIncome.getOrDefault(region.toLowerCase(), 0.0);
    }

    @Override
    public double calculateValuation() {
        double sum = 0.0;
        for(String key : regionalIncome.keySet()){
            sum += regionalIncome.get(key);
        }
        sum *= 10;// estimated asset value = 10x annual agricultural income
        return sum;
    }

    @Override
    public double calculateTax() {
        double tax = 0;
        for (String region: regionalIncome.keySet()) {
            double income = regionalIncome.get(region);
            if (region.equals("ict") || region.equals("punjab") || region.equals("kpk") || region.equals("baloch") || region.equals("sindh")) {
                if (income <= 600000) {
                    tax += 0; //exempt
                } else if (income <= 1200000) {
                    tax += (income - 600000) * 0.15;  //600k - 1.2 M PKR at 15 % tax
                } else if (income <= 1600000) {
                    tax += ((income - 1200000) * 0.2) + (600000 * 0.15); //1.2 M - 1.6 M PKR at 20 % tax plus previous slab
                } else if (income <= 3200000) {
                    tax += ((income - 1600000) * 0.3) + (400000 * 0.2) + (600000 * 0.15);//1.6 M - 3.2 M PKR at 30 % tax plus previous slabs
                }else if (income <= 5600000) {
                    tax += ((income - 3200000) * 0.4) + (1600000 * 0.3) + (400000 * 0.2) + (600000 * 0.15); //3.2 M - 5.6 M PKR at 40 % tax plus previous slabs
                } else{
                    tax += ((income - 5600000) * 0.45) + (2400000 * 0.4) + (1600000 * 0.3) + (400000 * 0.2) + (600000 * 0.15); //5.6+ M PKR at 45 % tax plus previous slabs
                }
            } else if (region.equals("ajk")) {
                if (income <= 600000) {
                    tax += 0;//exempt
                } else if (income <= 1200000) {
                    tax += (income - 600000) * 0.025;
                } else if (income <= 1600000) {
                    tax += ((income - 1200000) * 0.05) + (600000 * 0.025);
                }  else if (income <= 3200000) {
                    tax += ((income - 1600000) * 0.1) + (400000 * 0.05) + (600000 * 0.025);
                }
                else {
                    tax += ((income - 3200000) * 0.17) + (1600000 * 0.1) + (400000 * 0.05) + (600000 * 0.025);
                }
            }
            // GB agricultural income is exempt from agricultural income tax
        }
        return tax;
    }

    @Override
    public void save() {
        if(!tax.isEmpty()) return;
        tax.add(String.format("%.2f PKR to Federal Board of Revenue (FBR) for Agricultural Income", calculateTax()));
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
                "\nTotal Valuation: " + calculateValuation() +
                "\nTotal Tax: " + calculateTax();
    }
}
