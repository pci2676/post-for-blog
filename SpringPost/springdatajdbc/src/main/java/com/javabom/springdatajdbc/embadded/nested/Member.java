package com.javabom.springdatajdbc.embadded.nested;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;

public class Member {
    @Id
    private Long id;

    @Embedded.Nullable
    private Name name;

    protected Member() {
    }

    public Member(final Name name) {
        this.name = name;
    }

    public Name getName() {
        return name;
    }
}
