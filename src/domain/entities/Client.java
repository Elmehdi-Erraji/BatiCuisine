package domain.entities;

import java.util.ArrayList;
import java.util.List;

public class Client {
    private Integer id;
    private String name;
    private String address;
    private String phone;
    private Boolean isProfessional;
    private Boolean status;
    private List<Project> Projects;

    public Client(Integer id, String name, String address, String phone, Boolean isProfessional) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.isProfessional = isProfessional;
        this.status = false;
        this.Projects = new ArrayList<>();
    }

    public Client() {
        this.status = false;
        this.Projects = new ArrayList<>();
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getProfessional() {
        return isProfessional;
    }

    public void setProfessional(Boolean professional) {
        isProfessional = professional;
    }

    public Boolean isBlocked() {
        return status;
    }

    public void setBlocked(Boolean status) {
        this.status = status;
    }

    public List<Project> getProjects() {
        return Projects;
    }

    public void setProjects(List<Project> Projects) {
        this.Projects = Projects;
    }
}
