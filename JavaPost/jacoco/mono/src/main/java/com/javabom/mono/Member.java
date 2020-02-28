package com.javabom.mono;

public class Member {
    private Long id;
    private String name;
    private boolean activate;

    public Member(Long id, String name, boolean activate) {
        this.id = id;
        this.name = name;
        this.activate = activate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActivate() {
        return activate;
    }
}
