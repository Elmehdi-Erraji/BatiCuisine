package domain.entities;

public class Component {
    private String name;
    private Double taxRate;

    public Component(String name, Double taxRate) {
        this.name = name;
        this.taxRate = taxRate;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }
}
