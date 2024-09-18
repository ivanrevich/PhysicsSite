package com.aivhich.Physics.auth.models;


import com.aivhich.Physics.auth.code.Code;
import com.aivhich.Physics.auth.models.states.Role;
import com.aivhich.Physics.auth.token.Token;
import com.aivhich.Physics.models.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(unique = true)
    private String nickname;

    private String name;
    private String surname;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;
    @JsonIgnore
    private String password;
    private boolean isEmailVerified;

    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "user",
            orphanRemoval = true,
            cascade= {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Token> tokens;

    @JsonIgnore
    @OneToMany(mappedBy = "user",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Code> codes;


    @NotEmpty
    private Set<Course> courses;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "passedLessons",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_id"))
    private Set<Lesson> passedLessons = Collections.emptySet();


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "passedPages",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "page_id"))
    private Set<Page> passedPages = Collections.emptySet();


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "userDoCorrectly",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "form_id"))
    private Set<Form> userDoCorrectly = Collections.emptySet();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "userDoNotCorrectly",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "form_id"))
    private Set<Form> userDoNotCorrectly  = Collections.emptySet();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {return email;}

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEmailVerified;
    }
}
