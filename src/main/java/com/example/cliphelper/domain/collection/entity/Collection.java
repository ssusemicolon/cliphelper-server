package com.example.cliphelper.domain.collection.entity;

import com.example.cliphelper.domain.article.entity.Article;
import com.example.cliphelper.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@NoArgsConstructor
@Entity
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collection_id")
    private Long id;

    @NotBlank
    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "is_public")
    private boolean isPublic;

    @Column(name = "article_count")
    private int articleCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL)
    private List<ArticleCollection> articleCollections = new ArrayList<>();

    public Collection(String title, String description, boolean isPublic) {
        this.id = null;
        this.title = title;
        this.description = description;
        this.isPublic = isPublic;
        this.articleCount = 0;
    }

    public List<Article> getArticles() {
        return this.articleCollections
                .stream()
                .map(articleCollection -> articleCollection.getArticle())
                .collect(Collectors.toList());
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addArticleCount() {
        this.articleCount++;
    }

    public void minusArticleCount() {
        this.articleCount--;
    }

    public void changeInfo(String title, String description, boolean isPublic) {
        changeTitle(title);
        changeDescription(description);
        changeIsPublic(isPublic);
    }

    private void changeTitle(String title) {
        if (this.title != title) {
            this.title = title;
        }
    }

    private void changeDescription(String description) {
        if (this.description != description) {
            this.description = description;
        }
    }

    private void changeIsPublic(boolean isPublic) {
        if (this.isPublic != isPublic) {
            this.isPublic = isPublic;
        }
    }
}
