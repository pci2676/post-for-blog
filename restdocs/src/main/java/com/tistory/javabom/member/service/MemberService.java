package com.tistory.javabom.member.service;

import com.tistory.javabom.member.domain.Member;
import com.tistory.javabom.member.domain.dto.MemberResDTO;
import com.tistory.javabom.member.domain.dto.MemberUpdateDTO;
import com.tistory.javabom.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public MemberResDTO updateMember(Long id, MemberUpdateDTO memberUpdateDTO) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(id + "는 존재하지 않는 식별자 입니다."));
        member.update(memberUpdateDTO);
        return MemberResDTO.from(member);
    }
}
