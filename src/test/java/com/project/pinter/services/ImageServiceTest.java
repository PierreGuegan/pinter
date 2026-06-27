package com.project.pinter.services;

import com.project.pinter.entities.Image;
import com.project.pinter.entities.User;
import com.project.pinter.repositories.ImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageService imageService;

    private Image image;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(UUID.randomUUID());

        image = new Image();
        image.setTitle("test image");
        image.setPath("/uploads/test.png");
        image.setOwner(user);
    }

    @Test
    void shouldSaveImage() {
        when(imageRepository.save(any(Image.class))).thenReturn(image);

        Image saved = imageService.save(image);

        assertNotNull(saved);
        assertEquals("test image", saved.getTitle());

        verify(imageRepository, times(1)).save(any(Image.class));
    }
}