package com.shg.repository;

import com.shg.model.GovernmentScheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GovernmentSchemeRepository extends JpaRepository<GovernmentScheme, Long> {
    List<GovernmentScheme> findByGovernmentBody(String governmentBody);
}