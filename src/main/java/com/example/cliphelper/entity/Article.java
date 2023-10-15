package com.example.cliphelper.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    private String title;

    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "recent_access_time")
    private LocalDateTime recentAcessTime;

    @ManyToOne
    @JoinColumn(name = "collection_id")
    private Collection collection;

    /*
    @OneToMany(mappedBy = "article")
    private List<ArticleTag> articleTag;
    */

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
