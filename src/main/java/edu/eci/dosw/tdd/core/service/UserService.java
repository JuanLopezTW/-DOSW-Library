package edu.eci.dosw.tdd.core.service;

import edu.eci.dosw.tdd.core.util.IdGeneratorUtil;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;
import edu.eci.dosw.tdd.core.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final List<User> users = new ArrayList<>();

    public void addUser(User user) {
        if (user.getId() == 0) user.setId(IdGeneratorUtil.generateNumericId());
        users.add(user);
    }

    public User getUser(Long id) {
        User user = users.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(id));

        return user;
    }

    public List<User> getAllUsers() {
        return users;
    }

    public void deleteUser(Long id) {
        User user = getUser(id);
        users.remove(user);
    }
}