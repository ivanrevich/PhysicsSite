package com.aivhich.Physics.auth.models;


import com.aivhich.Physics.auth.models.states.ServerEventType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Builder
@Getter
@Setter
public class ServerEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String latestApiVersion;
    private String latestAppVersion;
    private Boolean isWorkNow;
    private String title;
    private String message;
    @NotNull
    private ServerEventType serverEventType;
}
