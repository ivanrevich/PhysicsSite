package com.aivhich.Physics.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class EditPageRequest {
    @JsonProperty("id")
    UUID id;

    @JsonProperty("text")
    String text;

    @JsonProperty("points")
    int points;
}
