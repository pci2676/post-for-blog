package com.javabom.springdatajdbc.wither;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

public class Sample {
    @Id
    private final Long id;
    @Column("created")
    private final LocalDateTime createdAt;
    private String sampleName;

    private Sample() {
        this.id = null;
        this.createdAt = null;
    }

    public Sample(final Long id, final String sampleName, final LocalDateTime createdAt) {
        this.id = id;
        this.sampleName = sampleName;
        this.createdAt = createdAt;
    }

    public Sample(final String sampleName) {
        this();
        this.sampleName = sampleName;
    }

//    //wrong
//    public Sample withId(Long id, LocalDateTime createdAt) {
//        return new Sample(id, this.sampleName, createdAt);
//    }

    //wither method
    public Sample withId(Long id) {
        return new Sample(id, this.sampleName, this.createdAt);
    }

    //wither method
    public Sample withCreatedAt(LocalDateTime createdAt) {
        return new Sample(this.id, this.sampleName, createdAt);
    }

    public Long getId() {
        return id;
    }

    public String getSampleName() {
        return sampleName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
