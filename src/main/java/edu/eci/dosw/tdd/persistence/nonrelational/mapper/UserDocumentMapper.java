package edu.eci.dosw.tdd.persistence.nonrelational.mapper;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.nonrelational.document.UserDocument;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UserDocumentMapper {

    public UserDocument toDocument(User user) {
        UserDocument doc = new UserDocument();
        doc.setId(String.valueOf(user.getId()));
        doc.setName(user.getName());
        doc.setUsername(user.getUsername());
        doc.setPassword(user.getPassword());
        doc.setRole(user.getRole());
        doc.setMembership("STANDARD");
        doc.setAddedAt(new Date());
        return doc;
    }

    public User toModel(UserDocument doc) {
        return new User(doc.getName(),
                doc.getId() != null ? Long.parseLong(doc.getId()) : 0L,
                doc.getUsername(),
                doc.getPassword(),
                doc.getRole());
    }
}