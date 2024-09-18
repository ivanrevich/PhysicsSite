package com.aivhich.Physics.repository;

import com.aivhich.Physics.models.Form;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FormsRepository extends JpaRepository<Form, UUID> {

}
