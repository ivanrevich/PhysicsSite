package com.aivhich.Physics.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateLevelRequest {
    @JsonProperty("title")
    String title;
}
