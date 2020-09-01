package io.javable.jpaapplication.core.repository;

import io.javable.jpaapplication.core.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
