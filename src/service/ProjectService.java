package service;

import domain.entities.Project;
import repository.Interfaces.ProjectRepository;

import java.util.List;

public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public void addProject(Project project) {
        projectRepository.addProject(project);
    }

    public Project getProjectById(int id) {
        return projectRepository.getProjectById(id);
    }

    public List<Project> getAllProjects() {
        return projectRepository.getAllProjects();
    }

    public void updateProject(Project project) {
        projectRepository.updateProject(project);
    }

    public void deleteProject(int id) {
        projectRepository.deleteProject(id);
    }
}
