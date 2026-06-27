package com.project.pinter.services;

import com.project.pinter.dto.ImageDto;
import com.project.pinter.entities.Image;
import com.project.pinter.entities.User;
import com.project.pinter.repositories.ImageRepository;
import com.project.pinter.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private ImageService imageService;

    private MultipartFile file;

    @BeforeEach
    void setUp() {
        file = mock(MultipartFile.class);
    }

    // CAS 1 : fichier vide
    @Test
    void uploadImage_shouldThrowException_whenFileEmpty() {

        when(file.isEmpty()).thenReturn(true);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> imageService.uploadImage(
                        "Bearer token",
                        file,
                        "title",
                        "desc",
                        "artist"
                )
        );

        assertEquals("File is empty", ex.getMessage());
    }

    // CAS 2 : mauvais type MIME
    @Test
    void uploadImage_shouldThrowException_whenInvalidContentType() {

        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("text/plain");

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> imageService.uploadImage(
                        "Bearer token",
                        file,
                        "title",
                        "desc",
                        "artist"
                )
        );

        assertEquals("Invalid file type", ex.getMessage());
    }

    // CAS 3 : utilisateur introuvable
    @Test
    void uploadImage_shouldThrowException_whenUserNotFound() {

        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("image/png");

        when(jwtService.extractEmail(anyString()))
                .thenReturn("missing@mail.com");

        when(userRepository.findByEmail("missing@mail.com"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> imageService.uploadImage(
                        "Bearer token",
                        file,
                        "title",
                        "desc",
                        "artist"
                )
        );

        assertEquals("User not found", ex.getMessage());
    }

    // CAS 4 : image déjà existante (hash duplicate)
    @Test
    void uploadImage_shouldReturnExistingImage_whenHashExists() throws Exception {

        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("image/png");
        when(file.getOriginalFilename()).thenReturn("test.png");
        when(file.getBytes()).thenReturn("fake".getBytes());

        when(jwtService.extractEmail(anyString()))
                .thenReturn("test@mail.com");

        User user = new User();

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));

        Image existing = new Image();
        existing.setTitle("existing");

        when(imageRepository.findByHash(anyString()))
                .thenReturn(existing);

        ImageDto result = imageService.uploadImage(
                "Bearer token",
                file,
                "title",
                "desc",
                "artist"
        );

        assertNotNull(result);
        assertEquals("existing", result.getTitle());

        verify(imageRepository, never()).save(any(Image.class));
    }

    // CAS 5 : upload normal (cas nominal)
    @Test
    void uploadImage_shouldReturnDto_whenValidRequest() throws Exception {

        when(file.isEmpty()).thenReturn(false);
        when(file.getContentType()).thenReturn("image/png");
        when(file.getOriginalFilename()).thenReturn("test.png");
        when(file.getBytes()).thenReturn("fake".getBytes());

        when(jwtService.extractEmail(anyString()))
                .thenReturn("test@mail.com");

        User user = new User();

        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));

        when(imageRepository.findByHash(anyString()))
                .thenReturn(null);

        when(imageRepository.save(any(Image.class)))
                .thenAnswer(i -> i.getArgument(0));

        ImageDto result = imageService.uploadImage(
                "Bearer token",
                file,
                "title",
                "desc",
                "artist"
        );

        assertNotNull(result);
        assertEquals("title", result.getTitle());

        verify(imageRepository, times(1)).save(any(Image.class));
    }
}