package repository.Interfaces;

import domain.entities.User;

import java.util.List;

public interface UserRepository {
    void create(User user);
    User findById(int id);
    List<User> findAll();
    void update(User user);
    void delete(int id);
}

