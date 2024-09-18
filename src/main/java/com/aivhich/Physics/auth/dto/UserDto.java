package com.aivhich.Physics.auth.dto;


import com.aivhich.Physics.auth.models.states.Role;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Data
@Builder
@Getter
public class UserDto {
    private UUID id;

    private String name;
    private String surname;
    private String code;

    private String nickname;
    private String email;
    private boolean emailVerified;
    private Role role;
}
