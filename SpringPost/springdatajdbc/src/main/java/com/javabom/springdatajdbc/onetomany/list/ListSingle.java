package com.javabom.springdatajdbc.onetomany.list;

import org.springframework.data.annotation.Id;

import java.util.List;

public class ListSingle {
    @Id
    private Long id;

    private List<ListMany> listManyList;

    public ListSingle() {
    }

    public ListSingle(final List<ListMany> listManyList) {
        this.listManyList = listManyList;
    }

    public Long getId() {
        return id;
    }

    public List<ListMany> getListManyList() {
        return listManyList;
    }
}
