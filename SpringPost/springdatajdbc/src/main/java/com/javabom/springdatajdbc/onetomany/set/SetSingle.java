package com.javabom.springdatajdbc.onetomany.set;

import org.springframework.data.annotation.Id;

import java.util.Set;

public class SetSingle {
    @Id
    private Long id;

    private Set<SetMany> manies;

    protected SetSingle() {
    }

    public SetSingle(final Set<SetMany> manies) {
        this.manies = manies;
    }

    public Long getId() {
        return id;
    }

    public Set<SetMany> getManies() {
        return manies;
    }
}
