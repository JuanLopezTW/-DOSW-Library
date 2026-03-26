package edu.eci.dosw.tdd.controller.mapper;

import edu.eci.dosw.tdd.controller.dto.UserDTO;
import edu.eci.dosw.tdd.core.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        return new UserDTO(user.getId(),user.getName(),user.getUsername(),null,user.getRole());
    }

    public User toModel(UserDTO dto) {
        return new User(dto.getName(), dto.getId() != null ? dto.getId() : 0L,
                dto.getUsername(), dto.getPassword(), dto.getRole());
    }
}
