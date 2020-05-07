package com.javabom.springdatajdbc.embedded.nested;

/**
 * embedded Entity 이다.
 * Member Table Column의 일부분을 모아 따로 맵핑한다.
 * VO의 성격을 띈다.
 */
public class Name {
    // id 필드가 존재해선 안된다.
    private String firstName;
    private String lastName;

    protected Name() {
    }

    public Name(final String firstName, final String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
