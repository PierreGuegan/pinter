package com.project.pinter.repositories;

import com.project.pinter.entities.ImageMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.*;
import java.util.UUID;


public interface ImageMetadataRepository extends JpaRepository<ImageMetadata, UUID> {
}