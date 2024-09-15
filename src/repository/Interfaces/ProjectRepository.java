package repository.Interfaces;

import domain.entities.Project;

import java.util.List;

public interface ProjectRepository {
    void addProject(Project project);
    Project getProjectById(int id);
    List<Project> getAllProjects();
    void updateProject(Project project);
    void deleteProject(int id);
}
