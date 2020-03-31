package com.hurriyetemlak.todoapp.todoapp.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
