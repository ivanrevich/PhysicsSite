package com.aivhich.Physics.repository;

import com.aivhich.Physics.models.Course;
import com.aivhich.Physics.models.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LessonRepository  extends JpaRepository<Lesson, UUID> {
//    List<Lesson> findAllByCourse(Course course);
}
