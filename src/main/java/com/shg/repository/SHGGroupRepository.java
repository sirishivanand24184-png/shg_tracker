package com.shg.repository;

import com.shg.model.SHGGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SHGGroupRepository extends JpaRepository<SHGGroup, Long> {
    Optional<SHGGroup> findByName(String name);
}