package com.project.pinter.controller;

import com.project.pinter.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.UUID;

import com.project.pinter.entities.Image;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping
    public ResponseEntity<Image> upload(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(imageService.uploadImage(file));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Image> getImage(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(imageService.getImage(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}