package com.project.team4backend.domain.auth.repository;

import com.project.team4backend.domain.auth.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {
    // member의 id를 통해 해당 member의 auth정 보 가져오기
    @Query("SELECT a from Auth a WHERE a.member.id = :memberId and a.isDeleted = false")
    Optional<Auth> findByMemberId(@Param("memberId") Long memberId);
}
