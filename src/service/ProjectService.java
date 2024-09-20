package service;

import domain.entities.Component;
import domain.entities.Material;
import domain.entities.Labour;
import domain.entities.Project;
import repository.Interfaces.ProjectRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Project createProjectWithComponents(Project project) throws SQLException {
        Project savedProject = projectRepository.save(project);

        MaterialsService materialsService = new MaterialsService();
        LabourService labourService = new LabourService();

        List<Component> components = project.getComponents();

        components.forEach(component -> {
            component.setProject(savedProject);
            if (component instanceof Material material) {
                materialsService.createMaterial(material);
            } else if (component instanceof Labour labour) {
                labourService.createLabour(labour);
            }
        });

        return savedProject;
    }

    public Optional<Project> getProjectById(Integer id) {
        return projectRepository.findById(id);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project updateProject(Project project) {
        return projectRepository.save(project);
    }

    public void deleteProject(Integer id) {
        projectRepository.deleteById(id);
    }
}
