package com.aivhich.Physics.dto.request;

import com.aivhich.Physics.models.Form;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.UUID;


@Data
public class UpdateProgress {
    @JsonProperty("rightForms")
    List<UUID> rightForms;

    @JsonProperty("passedPages")
    List<UUID> passedPages;
}
