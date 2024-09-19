package domain.entities;

import domain.enums.ProjectStatus;

import java.util.ArrayList;
import java.util.List;

public class Project {
    private int id;
    private String name;
    private double profitMargin;
    private Double totalCost;
    private ProjectStatus projectStatus;
    private User user;
    private List<Component> components;

    public Project(String name, double profitMargin, Double totalCost, ProjectStatus projectStatus, User user, List<Component> components) {
        this.name = name;
        this.profitMargin = profitMargin;
        this.totalCost = totalCost;
        this.projectStatus = projectStatus;
        this.user = user;
        this.components = components;
    }

    public Project() {

    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
                ", user=" + user +
                ", components=" + components + // Include components in toString
                '}';
    }
}
