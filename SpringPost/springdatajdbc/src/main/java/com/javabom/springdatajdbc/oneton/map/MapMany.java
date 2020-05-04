package com.javabom.springdatajdbc.oneton.map;

import org.springframework.data.annotation.Id;

public class MapMany {
    @Id
    private Long id;

    // 참조하는 객체의 이름으로 된 외래키를 기본적으로 기대한다. 기본적으로 database에 동일한 이름의 Column이 존재한다면 주석처리해도 무방하다.
    private Long mapSingle;

    // suffix 로 [참조하는테이블이름]_KEY 형태의 컬럼을 Map의 key 사용하기 위해 필요하다. 기본적으로 database에 동일한 이름의 Column이 존재한다면 주석처리해도 무방하다.
    private String mapSingleKey;

    private String content;

    public MapMany() {
    }

    public MapMany(final String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
