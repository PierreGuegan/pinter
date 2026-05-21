package com.project.pinter.services;

import com.project.pinter.dto.ImageDto;
import com.project.pinter.entities.Image;
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

import org.springframework.security.core.context.SecurityContextHolder;
import com.project.pinter.entities.User;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageMetadataRepository metadataRepository;

    @Value("${app.base-url}")
    private String baseUrl;



    public ImageDto uploadImage(MultipartFile file, String title, String description) throws Exception {


        // Vérification fichier vide
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        // Vérification type MIME
        if (file.getContentType() == null ||
                !file.getContentType().startsWith("image/")) {

            throw new RuntimeException("Invalid file type");
        }


        // RÉCUPÉRATION USER CONNECTÉ
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof User user)) {
            throw new RuntimeException("User not authenticated");
        }

        // Nettoyage nom fichier
        String originalName = Paths.get(file.getOriginalFilename())
                .getFileName()
                .toString();

        // Nom unique
        String fileName = UUID.randomUUID() + "_" + originalName;

        // Dossier uploads
        Path uploadPath = Paths.get("/app/uploads");

        // Création dossier si absent
        Files.createDirectories(uploadPath);

        // Chemin final
        Path filePath = uploadPath.resolve(fileName);

        // Sauvegarde physique
        Files.write(filePath, file.getBytes());

        // Génération hash SHA-256
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        byte[] hashBytes = digest.digest(file.getBytes());

        String hash = HexFormat.of().formatHex(hashBytes);

        Image existing = imageRepository.findByHash(hash);

        if (existing != null) {
            return toDto(existing);
        }

        // Chemin relatif stocké en BDD
        String relativePath = "/uploads/" + fileName;

        // Création entity
        Image image = new Image();

        image.setPath(relativePath);
        image.setHash(hash);
        image.setTitle(title);
        image.setDescription(description);
        image.setOwner(user);

        // Sauvegarde BDD
        imageRepository.save(image);

        // Retour DTO
        return toDto(image);
    }

    private ImageDto toDto(Image image) {

        ImageDto dto = new ImageDto();

        dto.setId(image.getId());
        dto.setTitle(image.getTitle());
        dto.setDescription(image.getDescription());
        dto.setHash(image.getHash());
        dto.setCreatedAt(image.getCreatedAt());

        // URL publique complète
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