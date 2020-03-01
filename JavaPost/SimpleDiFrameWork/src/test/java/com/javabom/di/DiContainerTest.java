package com.javabom.di;

import com.javabom.member.repository.MemberRepository;
import com.javabom.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DiContainerTest {

    @DisplayName("생성자를 찾아서 리플렉션을 통해 가져오는 방법")
    @Test
    void getObjectByConstructor() {
        MemberRepository memberRepository = DiContainer.getObjectByConstructor(MemberRepository.class);

        assertThat(memberRepository).isNotNull();
    }

    @DisplayName("생성자를 통해 가져왔을때 어노테이션으로 주입된것도 존재하는지 확인")
    @Test
    void getObject() throws NoSuchFieldException {
        MemberService memberService = DiContainer.getObjectByConstructor(MemberService.class);
        assertThat(memberService.memberRepository).isNotNull();
    }
}