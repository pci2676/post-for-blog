package io.javable.jpaapplication.core.service;

import io.javable.jpaapplication.core.domain.CellPhone;
import io.javable.jpaapplication.core.domain.Member;
import io.javable.jpaapplication.core.repository.CellPhoneRepository;
import io.javable.jpaapplication.core.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PhoneService {

    private final MemberRepository memberRepository;
    private final CellPhoneRepository cellPhoneRepository;

    @Transactional
    public void buyNewCellPhone(Long memberId, String phoneNumber) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(RuntimeException::new);

        cellPhoneRepository.findByPhoneNumber(phoneNumber)
                .ifPresent(cellPhone -> {
                    throw new RuntimeException("이미 존재하는 휴대전화 번호입니다.");
                });

        CellPhone cellPhone = new CellPhone(phoneNumber);
        member.buyCellphone(cellPhone);

        cellPhoneRepository.save(cellPhone);
    }
}
