package edu.eci.dosw.tdd.persistence.nonrelational.repository;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.core.repository.UserRepository;
import edu.eci.dosw.tdd.persistence.nonrelational.mapper.UserDocumentMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("mongo")
public class UserRepositoryMongoImpl implements UserRepository {

    private final MongoUserRepository repository;
    private final UserDocumentMapper mapper;

    public UserRepositoryMongoImpl(MongoUserRepository repository, UserDocumentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        return mapper.toModel(repository.save(mapper.toDocument(user)));
    }

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(String.valueOf(id)).map(mapper::toModel);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username).map(mapper::toModel);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(String.valueOf(id));
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(String.valueOf(id));
    }
}