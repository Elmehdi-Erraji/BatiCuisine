package service;

import domain.entities.Component;
import domain.entities.Labour;
import domain.entities.Material;
import domain.entities.Project;
import repository.Interfaces.ProjectRepository;

import java.util.List;

public class ProjectService {
    private final ProjectRepository projectRepository;
    private final LabourService labourService;
    private final MaterialsService materialsService;


    public ProjectService(ProjectRepository projectRepository, LabourService labourService, MaterialsService materialsService) {
        this.projectRepository = projectRepository;
        this.labourService = labourService;
        this.materialsService = materialsService;

    }



    public void addProject(Project project) {
        // Save the project itself
        projectRepository.addProject(project);

        // Save components of the project
        for (Component component : project.getComponents()) {
            if (component instanceof Labour) {
                labourService.createLabour((Labour) component);
            } else if (component instanceof Material) {
                materialsService.createMaterials((Material) component);
            }
        }
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
