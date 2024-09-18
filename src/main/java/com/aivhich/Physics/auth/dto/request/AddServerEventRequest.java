package com.aivhich.Physics.auth.dto.request;


import com.aivhich.Physics.auth.models.states.ServerEventType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddServerEventRequest {
    @JsonProperty("newAppVersion")
    private String newAppVersion;
    @JsonProperty("newApiVersion")
    private String newApiVersion;
    @JsonProperty("isWork")
    private Boolean isWork;
    @JsonProperty("title")
    private String title;
    @JsonProperty("message")
    private String message;
    @JsonProperty("serverEventType")
    private ServerEventType serverEventType;
}
