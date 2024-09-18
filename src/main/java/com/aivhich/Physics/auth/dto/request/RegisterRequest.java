package com.aivhich.Physics.auth.dto.request;


import com.aivhich.Physics.auth.models.states.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
  private String name;
  private String surname;
  private String username;
  private String email;
  private String password;
  private Role role;
}
