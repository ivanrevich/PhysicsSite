package com.aivhich.Physics.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Data
@Getter
@Setter
public class PasswordRememberSecondRequest {
    @JsonProperty("email")
    private String email;
    @JsonProperty("birthday")
    private Date birthday;
    @JsonProperty("code")
    private Integer code;
    @JsonProperty("newpassword")
    private String newpassword;
}
