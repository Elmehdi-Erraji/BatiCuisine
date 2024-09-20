package service;


import domain.entities.Component;
import domain.entities.Labour;
import domain.entities.Material;
import domain.entities.Project;
import repository.Interfaces.ProjectRepository;
import service.LabourService;
import service.MaterialsService;

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

    public Project createprojectWithComponents(Project project) {
        Project savedProject = projectRepository.save(project);

        MaterialsService materialsService =  new MaterialsService();
        LabourService labourService =  new LabourService();

        List<Component> components =  project.getComponents();

        components.forEach(component -> {
            if (component instanceof Material material) {

                component.setProject(savedProject);
                materialsService.createMaterial(material);

            } else if (component instanceof Labour labour) {

                component.setProject(savedProject);
                labourService.createLabour(labour);

            }
        });

        return savedProject;
    }


    public Optional<Project> getprojectById(Integer id) {
        return projectRepository.findById(id);
    }

    public List<Project> getAllprojects() {
        return projectRepository.findAll();
    }

    public Project updateproject(Project project) {
        return projectRepository.save(project);
    }

    public void deleteproject(Integer id) {
        projectRepository.deleteById(id);
    }
}
