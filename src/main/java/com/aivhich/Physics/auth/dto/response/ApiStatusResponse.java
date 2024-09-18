package com.aivhich.Physics.auth.dto.response;

import com.aivhich.Physics.auth.models.ServerEvent;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Builder
@Getter
public class ApiStatusResponse {
    private ServerEvent latestEvent;
    private List<ServerEvent> serverEventList;
}
