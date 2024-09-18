package com.aivhich.Physics.repository;

import com.aivhich.Physics.auth.models.ServerEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerEventRepository extends JpaRepository<ServerEvent, Long> {
}
