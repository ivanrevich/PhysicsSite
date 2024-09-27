package com.aivhich.Physics.models;

import com.aivhich.Physics.auth.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.*;


@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id", nullable=false, updatable=false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    InfoType infoType = InfoType.THEORY;

    @Column(length = 10000)
    String texts;

    Integer points;

    Long timestamp;

    Long stepId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "lesson_id")
    Lesson lesson;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "form_id")
    Form form;

    @JsonIgnore
    @ManyToMany(mappedBy = "passedPages", fetch = FetchType.EAGER)
    Set<User> usersPassed = Collections.emptySet();
}
