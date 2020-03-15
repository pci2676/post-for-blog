package com.javabom.member.domain;

public class Member {
    private Long id;
    private String name;
    private int age;
    private boolean activate;

    public Member(Long id, String name, int age, boolean activate) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.activate = activate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public boolean isActivate() {
        return activate;
    }

    public boolean isSameEntity(Long id) {
        return this.id.equals(id);
    }
}
