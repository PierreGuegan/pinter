package com.project.pinter.services;

import com.project.pinter.entities.BoardImages;
import com.project.pinter.entities.BoardImages;
import com.project.pinter.entities.Image;
import com.project.pinter.entities.ImageMetadata;
import com.project.pinter.repositories.ImageMetadataRepository;
import com.project.pinter.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.UUID;

import com.project.pinter.entities.Image;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageMetadataRepository metadataRepository;

    public Image uploadImage(MultipartFile file) throws Exception {

        // 1. Générer nom unique
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // 2. Chemin de stockage
        Path path = Paths.get("uploads/" + fileName);

        // 3. Créer dossier si inexistant
        Files.createDirectories(path.getParent());

        // 4. Écrire le fichier
        Files.write(path, file.getBytes());

        // 5. Générer hash SHA-256
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(file.getBytes());
        String hash = HexFormat.of().formatHex(hashBytes);

        // 6. Créer entité Image (classe concrète)
        Image image = new Image();
        image.setPath("/uploads/" + fileName);
        image.setHash(hash);

        imageRepository.save(image);

        // 7. Créer metadata
        ImageMetadata metadata = new ImageMetadata();
        metadata.setImage(image);
        metadata.setRawExif("EXIF_PLACEHOLDER");

        metadataRepository.save(metadata);

        // 8. Retourner image
        return image;
    }

    public Image getImage(UUID id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));
    }
}