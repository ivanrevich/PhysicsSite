package com.aivhich.Physics.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExistCheckResponse {
    @JsonProperty("nickname")
    private Boolean nickname;
    @JsonProperty("email")
    private Boolean email;
}
