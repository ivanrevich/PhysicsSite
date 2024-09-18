package com.aivhich.Physics.auth.code;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CodeRepository extends JpaRepository<Code, UUID> {
}
