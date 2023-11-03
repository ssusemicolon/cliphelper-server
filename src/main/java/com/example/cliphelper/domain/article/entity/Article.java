package com.example.cliphelper.domain.article.entity;

import com.example.cliphelper.domain.collection.entity.ArticleCollection;
import com.example.cliphelper.domain.tag.entity.ArticleTag;
import com.example.cliphelper.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;
    @NotBlank
    private String url;
    @NotBlank
    private String title;
    private String description;
    private String memo;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "recent_access_time")
    private LocalDateTime recentAccessTime;
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<ArticleTag> articleTags = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<ArticleCollection> articleCollections = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Article(String title, String url, String description, LocalDateTime createdAt, LocalDateTime recentAccessTime) {
        this.id = null;
        this.title = title;
        this.url = url;
        this.description = description;
        this.createdAt = createdAt;
        this.recentAccessTime = recentAccessTime;
    }

    public void setUser(User user) {
        if (user != null) {
            this.user = user;
        }
    }

    public void changeUrl(String url) {
        if (url != null) {
            this.url = url;
        }
    }

    public void changeTitle(String title) {
        if (title != null) {
            this.title = title;
        }
    }

    public void changeDescription(String description) {
        if (description != null) {
            this.description = description;
        }
    }

    public void changeMemo(String memo) {
        if (memo != null) {
            this.memo = memo;
        }
    }

}
