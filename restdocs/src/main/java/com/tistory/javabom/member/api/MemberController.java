package com.tistory.javabom.member.api;

import com.tistory.javabom.member.api.dto.CustomResponseEntity;
import com.tistory.javabom.member.domain.dto.MemberResDTO;
import com.tistory.javabom.member.domain.dto.MemberUpdateDTO;
import com.tistory.javabom.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponseEntity<MemberResDTO>> updateMember(@PathVariable(name = "id") Long id, @RequestBody MemberUpdateDTO memberUpdateDTO) {
        return ResponseEntity.ok(CustomResponseEntity.ok("변경 완료", memberService.updateMember(id, memberUpdateDTO)));
    }
}
