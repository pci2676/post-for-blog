package com.tistory.javabom.member.domain;

import com.tistory.javabom.member.domain.dto.MemberUpdateDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String gitHub;

    @Builder
    public Member(String name, String email, String gitHub) {
        this.name = name;
        this.email = email;
        this.gitHub = gitHub;
    }

    public void update(MemberUpdateDTO memberUpdateDTO) {
        this.name = memberUpdateDTO.getName();
        this.email = memberUpdateDTO.getEmail();
        this.gitHub = memberUpdateDTO.getGitHub();
    }
}
