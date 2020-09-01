package io.javable.jpaapplication.core.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PhoneBuyRequest {
    private Long memberId;
    private String phoneNumber;

    public PhoneBuyRequest(final Long memberId, final String phoneNumber) {
        this.memberId = memberId;
        this.phoneNumber = phoneNumber;
    }
}
