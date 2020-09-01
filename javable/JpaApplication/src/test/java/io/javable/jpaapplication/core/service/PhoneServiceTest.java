package io.javable.jpaapplication.core.service;

import io.javable.jpaapplication.core.domain.CellPhone;
import io.javable.jpaapplication.core.domain.Member;
import io.javable.jpaapplication.core.repository.CellPhoneRepository;
import io.javable.jpaapplication.core.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PhoneServiceTest {

    @Autowired
    private PhoneService phoneService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CellPhoneRepository cellPhoneRepository;

    //@Transactional
    @Test
    void buyNewCellPhone() {
        //given
        Member member = memberRepository.save(new Member("비밥"));
        String phoneNumber = "010-1234-5678";

        //when
        phoneService.buyNewCellPhone(member.getId(), phoneNumber);

        //then
        Member buyMember = memberRepository.findById(member.getId()).orElseThrow(RuntimeException::new);
        CellPhone cellPhone = buyMember.getCellPhones().get(0);

        assertThat(cellPhone.getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @AfterEach
    void tearDown() {
        cellPhoneRepository.deleteAll();
        memberRepository.deleteAll();
    }
}