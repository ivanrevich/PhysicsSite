package com.aivhich.Physics.auth.dto.response;


import com.aivhich.Physics.auth.models.states.UserStage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StageResponse {
    @JsonProperty("stage")
    private UserStage stage;
}
