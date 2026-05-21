package com.project.pinter.controller;

import com.project.pinter.dto.ImageDto;
import com.project.pinter.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping
    public ResponseEntity<ImageDto> upload(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("originalArtist") String originalArtist
    ) {
        try {
            return ResponseEntity.ok(imageService.uploadImage(authHeader, file, title, description, originalArtist));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageDto> getImage(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(imageService.getImage(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable UUID id
    ) {
        try {
            imageService.deleteImage(authHeader, id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ImageDto>> getAll() {
        return ResponseEntity.ok(imageService.getAllImages());
    }


    @GetMapping("/search")
    public ResponseEntity<List<ImageDto>> search(@RequestParam String q) {
        return ResponseEntity.ok(imageService.searchImages(q));
    }
}

