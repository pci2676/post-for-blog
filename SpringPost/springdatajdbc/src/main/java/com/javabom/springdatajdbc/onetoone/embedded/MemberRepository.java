package com.javabom.springdatajdbc.onetoone.embedded;

import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member, Long> {
}
