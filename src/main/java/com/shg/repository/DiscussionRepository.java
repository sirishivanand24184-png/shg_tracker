package com.shg.repository;

import com.shg.model.Discussion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscussionRepository extends JpaRepository<Discussion, Long> {
    List<Discussion> findByTopic(String topic);
    List<Discussion> findByCreatedByUsername(String username);
}