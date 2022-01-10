package com.cooksys.social_media_project.entities;

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
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private Credentials credentials;

    @Embedded
    private Profile profile;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp joined;

    @Column(nullable = false)
    private boolean deleted;

    @OneToMany(mappedBy = "author")
    private List<Tweet> tweets;

    @ManyToMany(mappedBy = "likes")
    private List<Tweet> likedTweets;

    @ManyToMany(mappedBy = "followers")
    private List<User> following;

    @ManyToMany
    @JoinTable
    private List<User> followers;

    @ManyToMany(mappedBy = "userMentioned")
    private List<Tweet> mentions;


}
