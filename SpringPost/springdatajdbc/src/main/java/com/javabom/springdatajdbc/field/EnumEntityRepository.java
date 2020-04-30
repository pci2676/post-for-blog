package com.javabom.springdatajdbc.field;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnumEntityRepository extends CrudRepository<EnumEntity, Long> {
    @Query("SELECT * FROM ENUM_ENTITY WHERE active = :active")
    List<EnumEntity> findAllByActive(@Param("active") String active);
}
