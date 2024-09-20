package domain.entities;

import domain.enums.ProjectStatus;

import java.util.ArrayList;
import java.util.List;

public class Project {
    private Integer id;
    private String projectName;
    private Double profit;
    private Double totalCost;
    private Double discount;
    private ProjectStatus projectStatus;//status
    private Client client;
    List<Component> components;

    public Project(Integer id, String projectName, Double profit, Double totalCost, Double discount, ProjectStatus projectStatus) {
        this.id = id;
        this.projectName = projectName;
        this.profit = profit;
        this.totalCost = totalCost;
        this.discount = discount;
        this.projectStatus = projectStatus;
        this.client = null;
        this.components = new ArrayList<>();
    }

    public Project() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public ProjectStatus getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}
