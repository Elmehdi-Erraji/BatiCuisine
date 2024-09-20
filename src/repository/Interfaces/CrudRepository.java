package repository.Interfaces;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {
    Optional<T> create(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    Optional<T> update(T entity);
    void delete(ID id);
}
