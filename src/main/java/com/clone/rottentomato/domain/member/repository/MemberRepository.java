package com.clone.rottentomato.domain.member.repository;

import com.clone.rottentomato.domain.member.component.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
     Optional<Member> findByMemberEmail(String email);
}
