package com.tistory.javabom.member.repository;

import com.tistory.javabom.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
