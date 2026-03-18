package edu.eci.dosw.tdd.core.model;

import lombok.*;
@Data
@NoArgsConstructor
public class User {
    private String name;
    private long id;

    public User(String name, long id){
        this.name = name;
        this.id = id;
    }
}
