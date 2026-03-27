package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.mapper.UserPersistenceMapper;
import edu.eci.dosw.tdd.persistence.repository.LoanRepository;
import edu.eci.dosw.tdd.persistence.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserPersistenceMapper userMapper;
    private final LoanRepository loanRepository;
    private final PasswordEncoder passwordEncoder; // agrega esto

    public UserService(UserRepository userRepository, UserPersistenceMapper userMapper,
                       LoanRepository loanRepository, PasswordEncoder passwordEncoder) { // agrega esto
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.loanRepository = loanRepository;
        this.passwordEncoder = passwordEncoder; // agrega esto
    }

    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // agrega esto
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