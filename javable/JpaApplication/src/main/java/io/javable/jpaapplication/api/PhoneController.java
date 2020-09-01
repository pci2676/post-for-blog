package io.javable.jpaapplication.api;

import io.javable.jpaapplication.core.service.PhoneService;
import io.javable.jpaapplication.core.service.dto.PhoneBuyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/phone")
public class PhoneController {

    private final PhoneService phoneService;

    @PostMapping
    public ResponseEntity<Void> buyPhone(@RequestBody PhoneBuyRequest phoneBuyRequest) {
        phoneService.buyNewCellPhone(phoneBuyRequest.getMemberId(), phoneBuyRequest.getPhoneNumber());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
