package com.tistory.javabom.member.domain.dto;

import com.tistory.javabom.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResDTO {
    private String name;
    private String email;
    private String gitHub;

    @Builder
    public MemberResDTO(String name, String email, String gitHub) {
        this.name = name;
        this.email = email;
        this.gitHub = gitHub;
    }

    public static MemberResDTO from(Member member) {
        return MemberResDTO.builder()
                .email(member.getEmail())
                .name(member.getName())
                .gitHub(member.getGitHub())
                .build();
    }
}
