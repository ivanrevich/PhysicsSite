package com.aivhich.Physics.models;

import com.aivhich.Physics.auth.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@Entity

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Enumerated(EnumType.STRING)
    InfoType infoType = InfoType.THEORY;
    @Column(unique = false)
    Long stepId;
    @NotBlank
    String title;
    String description;

    /////////////////////////////////////////////////////////////
    @OneToMany(mappedBy = "lesson",
            orphanRemoval = true,
            //cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.EAGER)
    List<Page> pages;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id", nullable = false)
    @JsonIgnore
    Level level;

    @JsonIgnore
    @ManyToMany(mappedBy = "passedLessons")
    Set<User> usersPassed;
}
