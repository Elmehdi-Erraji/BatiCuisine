package service.implimentation;


import domain.entities.Component;
import domain.entities.Labour;
import domain.entities.Material;
import domain.entities.Project;
import repository.Interfaces.ProjectRepository;
import service.interfaces.ProjectService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Project createprojectWithComponents(Project project) {
        Project savedProject = projectRepository.save(project);

        MaterialsServiceImpl materialsServiceImpl =  new MaterialsServiceImpl();
        LabourServiceImpl labourServiceImpl =  new LabourServiceImpl();

        List<Component> components =  project.getComponents();

        components.forEach(component -> {
            if (component instanceof Material material) {

                component.setProject(savedProject);
                materialsServiceImpl.createMaterial(material);

            } else if (component instanceof Labour labour) {

                component.setProject(savedProject);
                labourServiceImpl.createLabour(labour);

            }
        });

        return savedProject;
    }


    @Override
    public Optional<Project> getprojectById(Integer id) {
        return projectRepository.findById(id);
    }

    @Override
    public List<Project> getAllprojects() {
        return projectRepository.findAll();
    }

    @Override
    public Project updateproject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public void delete(Project project) {
        projectRepository.delete(project);
    }

}
