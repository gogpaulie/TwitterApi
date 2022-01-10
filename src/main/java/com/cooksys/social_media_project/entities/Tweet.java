package com.cooksys.social_media_project.entities;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Tweet {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn
    private User author;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp posted;

    @Column(nullable = false)
    private boolean deleted;

    private String content;

    @ManyToOne
    @JoinColumn
    private Tweet inReplyTo;

    @OneToMany(mappedBy = "replies")
    private List<Tweet> replies;

    @ManyToOne
    @JoinColumn
    private Tweet repostOf;

    @OneToMany(mappedBy = "repostOf")
    private List<Tweet> reposts;

    @ManyToMany
    @JoinTable
    private List<User> likes;

    @ManyToMany
    @JoinTable
    private List<Hashtag> hashtags;

    @ManyToMany
    @JoinTable
    private List<User> userMentioned;
}
