package com.aivhich.Physics.controllers;


import com.aivhich.Physics.auth.dto.request.ChangePasswordRequest;
import com.aivhich.Physics.auth.models.User;
import com.aivhich.Physics.models.Course;
import com.aivhich.Physics.models.Page;
import com.aivhich.Physics.services.LessonService;
import com.aivhich.Physics.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin(origins = {"http://physics-site-816529-ecdefa-77-73-132-179.traefik.me"})
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    private final LessonService lessonService;

    @PatchMapping("/change_password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<?> getLeaders(Principal principal){
        return ResponseEntity.ok(service.getLeaders(principal));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(Principal principal){
        service.delete(principal);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/statistics")
    public ResponseEntity<?> getStatistics(Principal principal){
        return ResponseEntity.ok(service.getStatistics(principal));
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUsers(@RequestParam("page") int page) {
        return ResponseEntity.ok(service.getUsers(page));
    }

    @GetMapping("/get")
    public ResponseEntity<?> user(Principal connectedUser){
        return ResponseEntity.ok(service.getUserData(connectedUser));
    }


    @GetMapping("/passed-lessons")
    public ResponseEntity<?> getPassedLessons(Principal connectedUser){
        return ResponseEntity.ok(lessonService.getPassedLessons(connectedUser, Course.PHYSICS));
    }
}
