package com.example.cliphelper.domain.article.entity;

import com.example.cliphelper.domain.collection.entity.ArticleCollection;
import com.example.cliphelper.domain.tag.entity.ArticleTag;
import com.example.cliphelper.domain.user.entity.User;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "thumbnail")
    private String thumbnail;

    @NotBlank
    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "memo")
    private String memo;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "recent_access_time")
    private LocalDateTime recentAccessTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<ArticleTag> articleTags = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<ArticleCollection> articleCollections = new ArrayList<>();

    @Builder
    private Article(Long id, String url, String fileUrl, String uuid, String thumbnail,
                   String title, String description, String memo,
                   LocalDateTime createdAt, LocalDateTime recentAccessTime, User user) {
        this.id = id;
        this.url = url;
        this.fileUrl = fileUrl;
        this.uuid = uuid;
        this.thumbnail = thumbnail;
        this.title = title;
        this.description = description;
        this.memo = memo;
        this.createdAt = createdAt;
        this.recentAccessTime = recentAccessTime;
        this.user = user;
    }

    public void changeUrl(String url) {
        if (url != null) {
            this.url = url;
        }
    }

    public void changeThumbnail(String thumbnail) {
        if (thumbnail != null) {
            this.thumbnail = thumbnail;
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

    public List<String> getTags() {
        return this.articleTags
                .stream()
                .map(articleTag -> articleTag.getTag().getName())
                .collect(Collectors.toList());
    }

    public void updateRecentAccessTime() {
        this.recentAccessTime = LocalDateTime.now();
    }
}
