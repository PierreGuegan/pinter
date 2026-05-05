package com.project.pinter.services;

import com.project.pinter.dto.ImageDto;
import com.project.pinter.entities.BoardImages;
import com.project.pinter.entities.BoardImages;
import com.project.pinter.entities.Image;
import com.project.pinter.entities.ImageMetadata;
import com.project.pinter.repositories.ImageMetadataRepository;
import com.project.pinter.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

import com.project.pinter.entities.Image;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageMetadataRepository metadataRepository;

    @Value("${app.base-url}")
    private String baseUrl;



    public ImageDto uploadImage(MultipartFile file) throws Exception {

        // 1. Nom du fichier
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // 2. Stockage physique
        Path path = Paths.get("uploads/" + fileName);

        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        // 3. Hash
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(file.getBytes());
        String hash = HexFormat.of().formatHex(hashBytes);

        // 4. Entity
        Image image = new Image();

        String relativePath = "/uploads/" + fileName;

        image.setPath(relativePath);
        image.setHash(hash);
        image.setTitle(file.getOriginalFilename());
        image.setDescription("Uploaded image");

        imageRepository.save(image);

        imageRepository.save(image);

        return toDto(image);
    }

    private ImageDto toDto(Image image) {

        ImageDto dto = new ImageDto();

        dto.setId(image.getId());
        dto.setTitle(image.getTitle());
        dto.setDescription(image.getDescription());
        dto.setHash(image.getHash());
        dto.setCreatedAt(image.getCreatedAt());

        dto.setUrl(baseUrl + image.getPath());

        return dto;
    }

    public ImageDto getImage(UUID id) {

        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        return toDto(image);
    }

    public List<ImageDto> getAllImages() {
        return imageRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }
}