package edu.eci.dosw.tdd.core.repository;

import edu.eci.dosw.tdd.core.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    List<User> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
}
