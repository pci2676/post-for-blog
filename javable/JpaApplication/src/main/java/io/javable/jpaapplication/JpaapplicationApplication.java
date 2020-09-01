package io.javable.jpaapplication;

import io.javable.jpaapplication.core.domain.Member;
import io.javable.jpaapplication.core.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@SpringBootApplication
public class JpaapplicationApplication {

    private final MemberRepository memberRepository;

    public static void main(String[] args) {
        SpringApplication.run(JpaapplicationApplication.class, args);
    }

    @PostConstruct
    public void memberSave() {
        memberRepository.save(new Member("비밥"));
    }
}
