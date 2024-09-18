package com.aivhich.Physics.controllers;

import com.aivhich.Physics.dto.request.UpdateFormRequest;
import com.aivhich.Physics.services.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/forms")
@CrossOrigin(origins = {"http://localhost:3005"})
public class FormsController {
    @Autowired
    private LessonService lessonService;

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/edit")
    public ResponseEntity<?> editForm(@RequestBody UpdateFormRequest updateFormRequest){
        lessonService.updateForm(updateFormRequest);
        return ResponseEntity.ok().build();
    }
}
