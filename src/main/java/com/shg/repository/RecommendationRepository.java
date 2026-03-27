package com.shg.repository;

import com.shg.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    List<Recommendation> findByStatus(String status);
    List<Recommendation> findByRecommendationType(String type);
    List<Recommendation> findByPriority(String priority);
}