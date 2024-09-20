package domain.entities;

import domain.enums.ProjectStatus;

import java.util.ArrayList;
import java.util.List;

public class Project {
    private Integer id;
    private String name;
    private Double profitMargin;
    private Double totalCost;
    private ProjectStatus projectStatus;
    private Client client;
    private List<Component> components;

    public Project(Integer id, String name, double profitMargin, Double totalCost, ProjectStatus projectStatus) {
        this.id = id;
        this.name = name;
        this.profitMargin = profitMargin;
        this.totalCost = 0.0;
        this.projectStatus = projectStatus;
        this.client = null;
        this.components = new ArrayList<>();
    }

    public Project() {
    }


    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getProfitMargin() {
        return profitMargin;
    }

    public void setProfitMargin(double profitMargin) {
        this.profitMargin = profitMargin;
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
        return client; // Changed from User to Client
    }

    public void setClient(Client client) {
        this.client = client; // Changed from User to Client
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }



    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", profitMargin=" + profitMargin +
                ", totalCost=" + totalCost +
                ", projectStatus=" + projectStatus +
                ", client=" + client + // Changed from User to Client
                ", components=" + components +
                '}';
    }
}
