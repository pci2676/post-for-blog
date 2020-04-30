package com.javabom.springdatajdbc.oneton.map;

import org.springframework.data.annotation.Id;

import java.util.Map;

public class MapSingle {
    @Id
    private Long id;

    private Map<String, MapMany> mapManyMap;

    public MapSingle() {
    }

    public MapSingle(final Map<String, MapMany> mapManyMap) {
        this.mapManyMap = mapManyMap;
    }

    public Map<String, MapMany> getMapManyMap() {
        return mapManyMap;
    }
}
