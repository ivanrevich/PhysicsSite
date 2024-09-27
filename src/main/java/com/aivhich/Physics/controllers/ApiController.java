package com.aivhich.Physics.controllers;

import com.aivhich.Physics.auth.dto.request.AddServerEventRequest;
import com.aivhich.Physics.services.AppEventService;
import com.aivhich.Physics.services.LessonService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/status")
@Hidden
@RequiredArgsConstructor
public class ApiController {

    private final AppEventService appEventService;
    private final LessonService lessonService;

    @GetMapping()
    public ResponseEntity<?> apiInfo() {
        return ResponseEntity.ok(appEventService.getEventHistory());
    }


    @PreAuthorize("hasAuthority('admin:create')")
    @PostMapping("/update")
    public ResponseEntity<?> updateServerStatus(@RequestBody AddServerEventRequest addServerEventRequest) {
        return ResponseEntity.ok(appEventService.addServerEvent(addServerEventRequest));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getall(){
        return ResponseEntity.ok(lessonService.getAllLesson().toString()+" "+lessonService.getAllLvls().toString()+" "+lessonService.getAllPages().toString()+" "+lessonService.getAllForms().toString());
    }
}
