package domain.entities;

import domain.enums.ComponentType;

public class Component {
    private String name;
    private Double taxRate;
    private ComponentType componentType;
    private Project Project;

    public Component(String name, Double taxRate, ComponentType componentType) {
        this.name = name;
        this.taxRate = taxRate;
        this.componentType = componentType;
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

    public ComponentType getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
    }

    public Project getProject() {
        return Project;
    }

    public void setProject(Project Project) {
        this.Project = Project;
    }
}
