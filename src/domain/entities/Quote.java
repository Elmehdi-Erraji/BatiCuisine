package domain.entities;

import java.time.LocalDate;

public class Quote {
    private Integer id;
    private Double estimatedPrice;
    private LocalDate issueDate;
    private LocalDate validityDate;
    private Boolean accepted;
    private Project project;

    public Quote(Integer id, Double estimatedPrice, LocalDate issueDate, LocalDate validityDate, Boolean accepted, Project project) {
        this.id = id;
        this.estimatedPrice = estimatedPrice;
        this.issueDate = issueDate;
        this.validityDate = validityDate;
        this.accepted = accepted;
        this.project = project;
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getEstimatedPrice() {
        return estimatedPrice;
    }

    public void setEstimatedPrice(Double estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(LocalDate validityDate) {
        this.validityDate = validityDate;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "id=" + id +
                ", estimatedPrice=" + estimatedPrice +
                ", issueDate=" + issueDate +
                ", validityDate=" + validityDate +
                ", accepted=" + accepted +
                ", project=" + project +
                '}';
    }
}
