package com.example.cliphelper.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Bookmark> bookmarks = new ArrayList<>();

    public User(String email, String password, String username) {
        this.id = null;
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public void changeInfo(String email, String password, String username) {
        changeEmail(email);
        changePassword(password);
        changeUsername(username);
    }

    private void changeEmail(String email) {
        if (this.email != null) {
            this.email = email;
        }
    }
    private void changePassword(String password) {
        if (this.password != null) {
            this.password = password;
        }
    }

    private void changeUsername(String username) {
        if (this.username != null) {
            this.username = username;
        }
    }
}
