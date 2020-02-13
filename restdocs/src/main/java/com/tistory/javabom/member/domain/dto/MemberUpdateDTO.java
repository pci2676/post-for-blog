package com.tistory.javabom.member.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberUpdateDTO {
    private String name;
    private String email;
    private String gitHub;

    @Builder
    public MemberUpdateDTO(String name, String email, String gitHub) {
        this.name = name;
        this.email = email;
        this.gitHub = gitHub;
    }
}
