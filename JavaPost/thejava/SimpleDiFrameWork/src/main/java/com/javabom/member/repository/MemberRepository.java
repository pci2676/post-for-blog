package com.javabom.member.repository;

import com.javabom.member.domain.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class MemberRepository {
    private static final List<Member> memberStorage;

    static {
        memberStorage = new ArrayList<>();
        memberStorage.add(new Member(1L, "비밥", 28, true));
        memberStorage.add(new Member(2L, "박씨", 27, true));
        memberStorage.add(new Member(3L, "찬인", 26, false));
        memberStorage.add(new Member(4L, "박찬인", 25, true));
        memberStorage.add(new Member(5L, "김씨", 22, false));
        memberStorage.add(new Member(6L, "이씨", 30, false));
        memberStorage.add(new Member(7L, "최씨", 23, true));
        memberStorage.add(new Member(8L, "제임스", 40, true));
        memberStorage.add(new Member(9L, "카우보이", 19, false));
    }

    public Member findById(Long id) {
        return memberStorage.stream()
                .filter(aMember -> aMember.isSameEntity(id))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }
}
