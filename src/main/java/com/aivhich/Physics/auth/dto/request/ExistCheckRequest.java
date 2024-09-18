package com.aivhich.Physics.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExistCheckRequest {
    @JsonProperty("nickname")
    private String nickname;
    @JsonProperty("email")
    private String email;
}
