package com.example.cliphelper.domain.user.entity;

import com.example.cliphelper.domain.alarm.entity.AlarmTime;
import com.example.cliphelper.domain.article.entity.Article;
import com.example.cliphelper.domain.bookmark.entity.Bookmark;
import com.example.cliphelper.domain.collection.entity.Collection;

import lombok.Builder;
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
    @Column(name = "email")
    private String email;

    @NotBlank
    // @Size(min = 2, max = 20)
    @Column(name = "username")
    private String username;

    @Column(name = "picture")
    private String picture;

    @Column(name = "enable_notifications")
    private boolean enableNotifications;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Article> articles = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Bookmark> bookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Collection> collections = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AlarmTime> alarmTimeList = new ArrayList<>();

    @Builder
    public User(String email, String username, String picture) {
        this.email = email;
        this.username = username;
        this.picture = picture;
        this.enableNotifications = false;
    }

    public void changeEnableNotifications(boolean enableNotifications) {
        this.enableNotifications = enableNotifications;
    }

    public void changeUsername(String username) {
        if (this.username != null) {
            this.username = username;
        }
    }

    public void changePicture(String picture) {
        if (this.picture != null) {
            this.picture = picture;
        }
    }
}
