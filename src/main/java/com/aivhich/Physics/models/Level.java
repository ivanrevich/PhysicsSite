package com.aivhich.Physics.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Level {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = false)
    Long stepId;
    @NotBlank
    String title;

    @OneToMany(mappedBy = "level",
            orphanRemoval = true)
    List<Lesson> lessons;

    @NotNull
    Course course;
}
