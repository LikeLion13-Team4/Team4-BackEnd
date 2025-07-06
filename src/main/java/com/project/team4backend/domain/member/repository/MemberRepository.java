package com.project.team4backend.domain.member.repository;

import com.project.team4backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {

    @Query("SELECT m FROM Member m WHERE m.email = :email and m.isDeleted = false")
    Optional<Member> findByEmail(@Param("email") String email);

    Boolean existsByEmail(String email);
}
