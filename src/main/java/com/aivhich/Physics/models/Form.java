package com.aivhich.Physics.models;

import com.aivhich.Physics.auth.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Form {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    FormType formType;

    Collection<String> answers;
    Collection<String> rightAnswers;


    //////////////////////////////////////////////////////////
    @JsonIgnore
    @OneToMany(mappedBy = "form",
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    List<Page> pages;

    @JsonIgnore
    @ManyToMany(mappedBy = "userDoCorrectly", cascade = CascadeType.ALL)
    Set<User> usersDoCorrectly;

    @JsonIgnore
    @ManyToMany(mappedBy = "userDoNotCorrectly", cascade = CascadeType.ALL)
    Set<User> usersDoNotCorrectly;
}
