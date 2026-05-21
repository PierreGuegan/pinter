package com.project.pinter.services;

import com.project.pinter.entities.Image;
import com.project.pinter.entities.Like;
import com.project.pinter.entities.User;
import com.project.pinter.repositories.ImageRepository;
import com.project.pinter.repositories.LikeRepository;
import com.project.pinter.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

    @Service
    public class LikeService {

        @Autowired
        private LikeRepository likeRepository;

        @Autowired
        private ImageRepository imageRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private JwtService jwtService;

        @Transactional
        public void toggleLike(String authHeader, UUID imageId) {

            String token = authHeader.replace("Bearer ", "");
            String email = jwtService.extractEmail(token);

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Image image = imageRepository.findById(imageId)
                    .orElseThrow(() -> new RuntimeException("Image not found"));

            boolean exists = likeRepository.existsByUserAndImage(user, image);

            if (exists) {
                likeRepository.deleteByUserAndImage(user, image);
            } else {
                Like like = new Like();
                like.setUser(user);
                like.setImage(image);
                likeRepository.save(like);
            }
        }

        public long countLikes(UUID imageId) {
            Image image = imageRepository.findById(imageId)
                    .orElseThrow();
            return likeRepository.countByImage(image);
        }
    }

