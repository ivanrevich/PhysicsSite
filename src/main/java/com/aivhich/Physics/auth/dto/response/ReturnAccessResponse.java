package com.aivhich.Physics.auth.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReturnAccessResponse {
    Boolean successfully;
    String accessToken;
    String refreshToken;
}
