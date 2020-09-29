package com.javabom.ymlpropertiesbinding.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Getter
@Service
public class ExternalService {
    @Value("${external.record-year}")
    private String recordYear;
    @Value("${external.api.name}")
    private String apiName;
    @Value("${external.api.key}")
    private Integer apiKey;
}
