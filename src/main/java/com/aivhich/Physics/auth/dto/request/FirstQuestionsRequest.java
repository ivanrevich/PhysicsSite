package com.aivhich.Physics.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
public class FirstQuestionsRequest {
    @JsonProperty("birthday")
    private Date birthday;
    @JsonProperty("startInterests")
    private List<String> startInterests;

    @JsonProperty("dayWords")
    private int dayWords;
    
}
