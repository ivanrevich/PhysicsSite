package com.aivhich.Physics.controllers;

import com.aivhich.Physics.dto.request.CreateLessonRequest;
import com.aivhich.Physics.dto.request.UpdateFormRequest;
import com.aivhich.Physics.dto.request.UpdateProgress;
import com.aivhich.Physics.dto.request.UpdateTitleRequest;
import com.aivhich.Physics.models.InfoType;
import com.aivhich.Physics.services.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/lessons")
@CrossOrigin(origins = {"http://localhost:3005"})
public class LessonController {
    @Autowired
    private LessonService lessonService;

    @GetMapping("/get")
    public ResponseEntity<?> getOne(@RequestParam("id") UUID id){
        //get one
        return ResponseEntity.ok(lessonService.getLesson(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CreateLessonRequest createLessonRequest){
        return ResponseEntity.ok(lessonService.createNewLesson(createLessonRequest));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-title")
    public ResponseEntity<?> updateLessonTitle(@RequestBody UpdateTitleRequest updateTitleRequest){
        lessonService.updateLessonTitle(updateTitleRequest.getId(), updateTitleRequest.getTitle());
        return ResponseEntity.ok().build();
    }


    @PostMapping("/updateProgress")
    public ResponseEntity<?> updateProgress(Principal principal, @RequestBody UpdateProgress updRequest){
        return ResponseEntity.ok(lessonService.updateProgress(principal, updRequest));
    }

    @GetMapping("/weekPoints")
    public ResponseEntity<?> getWeekPoints(Principal principal){
        return ResponseEntity.ok(lessonService.getWeekPoints(principal));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteLesson(@RequestParam("id") UUID id){
        lessonService.deleteLesson(id);
        return ResponseEntity.ok().build();
    }
}
