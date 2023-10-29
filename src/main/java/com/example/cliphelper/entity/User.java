package com.example.cliphelper.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Email
    private String email;

    @NotBlank
    // @Size(min = 8, max = 30)
    private String password;

    @NotBlank
    // @Size(min = 2, max = 20)
    private String username;

    public User(String email, String password, String username) {
        this.id = null;
        this.email = email;
        this.password = password;
        this.username = username;
    }
}
