package com.javabom.springdatajdbc.onetoone;

import org.springframework.data.annotation.Id;

public class SubOne {
    // Embedded가 아닌 참조 엔티티는 id가 필요하다.
    @Id
    private Long id;
    // 필요없다.
    // private Long superOne;
    private String subName;

    protected SubOne() {
    }

    public SubOne(final String subName) {
        this.subName = subName;
    }

    public Long getId() {
        return id;
    }
}
