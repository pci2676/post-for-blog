package io.javable.jpaapplication.core.repository;

import io.javable.jpaapplication.core.domain.CellPhone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CellPhoneRepository extends JpaRepository<CellPhone, Long> {
    Optional<CellPhone> findByPhoneNumber(String phoneNumber);
}
