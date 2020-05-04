package com.javabom.springdatajdbc.oneton.set;

import org.springframework.data.annotation.Id;

public class SetMany {
    @Id
    private Long id;
    // 아래 이름으로 된 외래키를 기대한다. 주석처리 하고 사용하여도 무방하다.
    private Long setSingle;
    private String manyName;

    public SetMany() {
    }

    public SetMany(final String manyName) {
        this.manyName = manyName;
    }

    public Long getSetSingle() {
        return setSingle;
    }
}
