package com.project.pinter.entities;

import jakarta.persistence.*;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "image_metadata")
public class ImageMetadata {

    @Id
    @GeneratedValue
    private UUID id;

    private String camera;

    private String resolution;

    private LocalDateTime takenAt;

    @Lob
    private String rawExif;

    @OneToOne
    @JoinColumn(name = "image_id", unique = true)
    private Image image;
}