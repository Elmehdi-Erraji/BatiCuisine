package utils.Types;

public class CostBreakdown {
    private double baseCost;
    private double taxAmount;
    private double totalCost;
    private double profit;
    private double discount;

    public CostBreakdown(double baseCost, double taxAmount) {
        this.baseCost = baseCost;
        this.taxAmount = taxAmount;
        this.totalCost = baseCost + taxAmount;
        this.profit = 0.0;
    }

    // Getters
    public double getBaseCost() { return baseCost; }
    public double getTaxAmount() { return taxAmount; }
    public double getTotalCost() { return (baseCost + taxAmount + profit) - discount; }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}