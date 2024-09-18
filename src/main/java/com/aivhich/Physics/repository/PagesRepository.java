package com.aivhich.Physics.repository;

import com.aivhich.Physics.models.Lesson;
import com.aivhich.Physics.models.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PagesRepository extends JpaRepository<Page, UUID> {

}
