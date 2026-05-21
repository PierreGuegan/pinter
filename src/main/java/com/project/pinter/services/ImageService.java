package com.project.pinter.services;

import com.project.pinter.dto.ImageDto;
import com.project.pinter.dto.UserDto;
import com.project.pinter.entities.Image;
import com.project.pinter.entities.User;
import com.project.pinter.repositories.ImageMetadataRepository;
import com.project.pinter.repositories.ImageRepository;

import com.project.pinter.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.security.MessageDigest;

import java.util.HexFormat;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageMetadataRepository metadataRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${app.base-url}")
    private String baseUrl;

    @Autowired
    private JwtService jwtService;

    public ImageDto uploadImage(
            String authHeader,
            MultipartFile file,
            String title,
            String description,
            String originalArtist
    ) throws Exception {


        // Vérification fichier vide
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        // Vérification type MIME
        if (file.getContentType() == null ||
                !file.getContentType().startsWith("image/")) {

            throw new RuntimeException("Invalid file type");
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
        image.setOriginalArtist(originalArtist);


        // EXTRACTION TOKEN
        String token = authHeader.replace("Bearer ", "");

        // EXTRACTION EMAIL
        String email = jwtService.extractEmail(token);

        // RECUP USER
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ASSOCIER OWNER
        image.setOwner(user);

        System.out.println("USER = " + user.getUsername());

        // Sauvegarde BDD
        imageRepository.save(image);

        System.out.println("IMAGE UPLOADEE");
        System.out.println("AUTH = " + SecurityContextHolder.getContext().getAuthentication());
        System.out.println("PRINCIPAL = " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());

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
        dto.setOriginalArtist(image.getOriginalArtist());

        dto.setUrl(baseUrl + image.getPath());

        // OWNER DTO
        if (image.getOwner() != null) {

            UserDto ownerDto = new UserDto();

            ownerDto.setId(image.getOwner().getId());
            ownerDto.setUsername(image.getOwner().getUsername());

            dto.setOwner(ownerDto);
        }

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

    public void deleteImage(String authHeader, UUID imageId) {

        // 1. extract token
        String token = authHeader.replace("Bearer ", "");

        // 2. extract email
        String email = jwtService.extractEmail(token);

        // 3. find user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 4. find image
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        // 5. check ownership
        if (image.getOwner() == null ||
                !image.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("Not allowed");
        }

        // 6. delete file physically (optionnel mais propre)
        try {
            Path path = Paths.get("/app" + image.getPath());
            Files.deleteIfExists(path);
        } catch (Exception e) {
            System.out.println("File deletion error: " + e.getMessage());
        }

        // 7. delete DB
        imageRepository.delete(image);
    }

    public List<ImageDto> searchImages(String query) {

        if (query == null || query.isBlank()) {
            return getAllImages();
        }

        return imageRepository.search(query)
                .stream()
                .map(this::toDto)
                .toList();
    }


}