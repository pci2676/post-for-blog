package com.javabom.member.service;

import com.javabom.di.annotation.Autowired;
import com.javabom.member.domain.Member;
import com.javabom.member.repository.MemberRepository;

public class MemberService {

    @Autowired
    public MemberRepository memberRepository;

    public Member findMember() {
        return null;
    }

}
