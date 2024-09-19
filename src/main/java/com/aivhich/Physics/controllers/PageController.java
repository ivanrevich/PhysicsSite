package com.aivhich.Physics.controllers;

import com.aivhich.Physics.dto.request.CreatePageRequest;
import com.aivhich.Physics.dto.request.EditPageRequest;
import com.aivhich.Physics.services.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/page")
@CrossOrigin(origins = {"http://physics-site-816529-ecdefa-77-73-132-179.traefik.me"})
public class PageController {
    @Autowired
    private LessonService lessonService;

    private static final String UPLOAD_PATH = "src/main/resources/static/files/";


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<?> createPage(@RequestBody CreatePageRequest createPageRequest){
        return ResponseEntity.ok(lessonService.createNewPage(createPageRequest.getLesson_id(), createPageRequest.getInfoType(), createPageRequest.getStepid()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/edit")
    public ResponseEntity<?> editPage(@RequestBody EditPageRequest editPageRequest){
        lessonService.updatePage(editPageRequest.getId(), editPageRequest.getText(), editPageRequest.getPoints());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/image/add")
    public ResponseEntity<?> addImageOnPage(@RequestParam("image") MultipartFile file,
                                            @RequestParam String pageid){
        return ResponseEntity.ok(lessonService.addImage(file, pageid));
    }


    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws MalformedURLException {
        Path filePath = Paths.get(UPLOAD_PATH).resolve(filename);
        Resource file = new UrlResource(filePath.toUri());

        if (!file.exists() || !file.isFile()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                //.contentType(MediaType.IMAGE_JPEG) // Укажите корректный MIME тип
                .body(file);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePage(@RequestParam("id") UUID id){
        lessonService.deletePage(id);
        return ResponseEntity.ok().build();
    }
}
