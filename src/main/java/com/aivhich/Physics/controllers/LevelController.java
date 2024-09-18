package com.aivhich.Physics.controllers;


import com.aivhich.Physics.dto.request.CreateLevelRequest;
import com.aivhich.Physics.models.Course;
import com.aivhich.Physics.models.Level;
import com.aivhich.Physics.repository.LevelRepository;
import com.aivhich.Physics.services.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/lvl")
@CrossOrigin(origins = {"http://localhost:3000"})
public class LevelController {
    @Autowired
    LessonService lessonService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createLevel(@RequestBody CreateLevelRequest createLevelRequest){
        return ResponseEntity.ok(lessonService.createNewLevel(createLevelRequest));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteLvl(@RequestParam("id") UUID id){
        lessonService.deleteLvl(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getLevels(){
        return ResponseEntity.ok(lessonService.getLevels());
    }

}
