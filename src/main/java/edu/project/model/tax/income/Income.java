package edu.project.model.tax.income;

import edu.project.model.tax.Taxable;

import java.util.HashMap;
import java.util.Map;

public abstract class Income extends Taxable {
    protected Map<String, Double> regionalIncome;
    public Income(){
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

    abstract public double calculateValuation();
}
