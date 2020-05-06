package com.javabom.springdatajdbc.onetomany.list;

import org.springframework.data.annotation.Id;

public class ListMany {
    @Id
    private Long id;

    // 기본적으로 database에 동일한 이름의 Column이 존재한다면 주석처리해도 무방하다.
    private Long listSingle;

    // 기본적으로 database에 동일한 이름의 Column이 존재한다면 주석처리해도 무방하다.
    private Long listSingleKey;

    public ListMany() {
    }
}
