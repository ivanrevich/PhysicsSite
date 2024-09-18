package com.aivhich.Physics.dto.request;

import com.aivhich.Physics.models.InfoType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Data
@Getter
public class CreatePageRequest {
    @JsonProperty("lesson_id")
    UUID lesson_id;
    @JsonProperty("type")
    InfoType infoType;
    @JsonProperty("stepid")
    Long stepid;
}
