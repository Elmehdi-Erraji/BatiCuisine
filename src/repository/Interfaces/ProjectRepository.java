package repository.Interfaces;

import domain.entities.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    Project save(Project project);
    Optional<Project> findById(Integer id);
    List<Project> findAll();
    void deleteById(Integer id);
}


