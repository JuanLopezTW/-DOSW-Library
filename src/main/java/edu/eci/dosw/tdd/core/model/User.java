package edu.eci.dosw.tdd.core.model;

import lombok.*;
@Data
@NoArgsConstructor
public class User {
    private String name;
    private long id;
    private String username;
    private String password;
    private String role;

    public User(String name, long id){
        this.name = name;
        this.id = id;
    }

    public User(String name, long id, String username, String password, String role) {
        this.name = name;
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
