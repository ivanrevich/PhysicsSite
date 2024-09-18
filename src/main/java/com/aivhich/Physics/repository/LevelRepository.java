package com.aivhich.Physics.repository;

import com.aivhich.Physics.models.Level;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LevelRepository extends JpaRepository<Level, UUID> {

}
