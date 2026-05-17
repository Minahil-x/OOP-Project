package edu.project.model.user;

import edu.project.model.tax.Taxable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TaxPayer extends User {
    private boolean filerStatus;
    private String region;
    private Map<Class<? extends Taxable>, Taxable> taxables;

    public TaxPayer(String id, String password, boolean filerStatus, String region) {
        super(id, password);
        this.filerStatus = filerStatus;
        this.region = region;
        this.taxables = new HashMap<>();
    }

    // Returns a Collection view of the values for easy iteration
    public Collection<Taxable> getTaxables() {
        return taxables.values();
    }

    public boolean getFilerStatus() { return filerStatus; }
    public String getRegion() { return region; }

    public void addTaxable(Taxable taxable) {
        if (taxable != null) {
            this.taxables.put(taxable.getClass(), taxable);
        }
    }

    public String totalTax() {
        StringBuilder totalTax = new StringBuilder();
        for (Taxable taxable : taxables.values()) {
            totalTax.append(taxable.display());
        }
        return totalTax.toString();
    }

    public double totalTaxAmount() {
        double totalTaxAmount = 0;
        for (Taxable taxable : taxables.values()) {
            totalTaxAmount += taxable.calculateTax();
        }
        return totalTaxAmount;
    }

    public double totalValue() {
        double totalValue = 0;
        for (Taxable taxable : taxables.values()) {
            totalValue += taxable.calculateValuation();
        }
        return totalValue;
    }

    public String getData(){
        StringBuilder data = new StringBuilder();
        for (Taxable taxable : taxables.values()) {
            data.append(taxable.toString()).append("\n");
        }
        return data.toString();
    }
}
