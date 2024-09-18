package com.aivhich.Physics.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateLessonRequest {

    @JsonProperty("title")
    String title;

    @JsonProperty("levelid")
    UUID level;

}
