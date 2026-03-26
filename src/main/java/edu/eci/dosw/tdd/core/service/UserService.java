package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.mapper.UserPersistenceMapper;
import edu.eci.dosw.tdd.persistence.repository.LoanRepository;
import edu.eci.dosw.tdd.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserPersistenceMapper userMapper;
    private final LoanRepository loanRepository;

    public UserService(UserRepository userRepository, UserPersistenceMapper userMapper, LoanRepository loanRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.loanRepository = loanRepository;
    }

    public void addUser(User user) {
        userRepository.save(userMapper.toEntity(user));
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toModel)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toModel)
                .toList();
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) throw new UserNotFoundException(id);
        long activeLoans = loanRepository.findByUserIdAndStatus(id, Loan.LoanStatus.ACTIVE.name()).size();
        if (activeLoans > 0) throw new IllegalArgumentException("No se puede eliminar un usuario con prestamos activos");
        userRepository.deleteById(id);
    }
}