package com.aivhich.Physics.services;


import com.aivhich.Physics.auth.dto.request.AddServerEventRequest;
import com.aivhich.Physics.auth.dto.response.ApiStatusResponse;
import com.aivhich.Physics.auth.models.ServerEvent;
import com.aivhich.Physics.auth.models.states.ServerEventType;
import com.aivhich.Physics.repository.ServerEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppEventService {
    private final ServerEventRepository serverEventRepository;

    public ApiStatusResponse getEventHistory() {
        List<ServerEvent> serverEvents = serverEventRepository.findAll();
        if (serverEvents.isEmpty()) serverEvents.add(ServerEvent.builder()
                .latestAppVersion("1.0.0")
                .latestApiVersion("1.0.0")
                .title("")
                .message("")
                .isWorkNow(Boolean.TRUE)
                .serverEventType(ServerEventType.STATUS_UPDATE)
                .build());
        ServerEvent lastEvent = serverEvents.get(serverEvents.size() - 1);
        return ApiStatusResponse.builder()
                .serverEventList(serverEvents)
                .latestEvent(lastEvent)
                .build();
    }

    public Boolean addServerEvent(AddServerEventRequest addServerEventRequest) {
        serverEventRepository.save(
                ServerEvent.builder()
                        .serverEventType(addServerEventRequest.getServerEventType())
                        .isWorkNow(addServerEventRequest.getIsWork())
                        .latestApiVersion(addServerEventRequest.getNewApiVersion())
                        .latestAppVersion(addServerEventRequest.getNewAppVersion())
                        .title(addServerEventRequest.getTitle())
                        .message(addServerEventRequest.getMessage())
                        .build()
        );
        return true;
    }
}
