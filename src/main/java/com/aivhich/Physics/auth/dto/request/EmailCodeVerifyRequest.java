package com.aivhich.Physics.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class EmailCodeVerifyRequest {
    @JsonProperty("code")
    private Integer code;
    @JsonProperty("email")
    private String email;
    @JsonProperty("accessToken")
    private String accessToken;

}