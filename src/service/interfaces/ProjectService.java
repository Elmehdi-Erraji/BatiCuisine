package service.interfaces;

import domain.entities.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectService {
    Project createProject(Project project);

    Project createprojectWithComponents(Project project);

    Optional<Project> getprojectById(Integer id);

    List<Project> getAllprojects();

    Project updateproject(Project project);

    void delete(Project project);
}
