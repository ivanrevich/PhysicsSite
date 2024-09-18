package com.aivhich.Physics.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateTitleRequest {
    @JsonProperty("id")
    UUID id;

    @JsonProperty("title")
    String title;
}
