package com.javabom.springdatajdbc.onetoone.embedded;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class MemberTest {

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
    }

    @DisplayName("Embedded 엔티티와 함께 저장하기")
    @Test
    void save() {
        //given
        Name name = new Name("first", "last");
        Member member = new Member(name);

        //when
        Member save = memberRepository.save(member);

        //then
        assertThat(save.getName()).isNotNull();
    }
}