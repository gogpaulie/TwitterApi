package com.cooksys.social_media_project.repositories;

import com.cooksys.social_media_project.entities.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {

    List<Tweet> findAllByDeletedFalse();

    List<Tweet> findAllByDeletedOrderByPosted(boolean deleted);

    Optional<Tweet> findByIdAndDeletedFalse(Long id);
}
