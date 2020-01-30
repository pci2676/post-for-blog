package com.tistory.javabom.config.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.util.NoSuchElementException;

@Slf4j
public class RiotErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return super.hasError(response);
    }

    @Override
    public void handleError(ClientHttpResponse response) {
        HttpStatus statusCode = getStatusCode(response);
        //상태코드에 맞게 대응하는 코드 작성
        if (statusCode.is4xxClientError()) {
            log.error("400번대 에러");
            throw new NoSuchElementException();
        }
        if (statusCode.is5xxServerError()) {
            log.error("500번대 에러");
            throw new RuntimeException();
        }
    }

    private HttpStatus getStatusCode(ClientHttpResponse response) {
        try (ClientHttpResponse res = response) {
            return res.getStatusCode();
        } catch (IOException ioException) {
            return HttpStatus.UNPROCESSABLE_ENTITY;
        }
    }

}