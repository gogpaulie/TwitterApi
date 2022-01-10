package com.cooksys.social_media_project.repositories;

import com.cooksys.social_media_project.entities.Hashtag;
import com.cooksys.social_media_project.entities.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HashTagRepository extends JpaRepository<Hashtag, Long> {

    Hashtag findByLabelIgnoreCase(String label);

    Optional<Hashtag> findOneByLabelIgnoreCase(String label);


}
