package com.aivhich.Physics.auth.code;


import com.aivhich.Physics.auth.models.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Code {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    private int code;

    @NotNull
    private Long createdAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CodeTimeType codeTimeType;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable=false)
    public User user;
}
