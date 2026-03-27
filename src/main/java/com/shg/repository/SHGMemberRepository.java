package com.shg.repository;

import com.shg.model.SHGMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SHGMemberRepository extends JpaRepository<SHGMember, Long> {
    Optional<SHGMember> findByUsername(String username);
    Optional<SHGMember> findByEmail(String email);
    List<SHGMember> findByShgGroupId(Long shgGroupId);
    List<SHGMember> findByRole(String role);
}