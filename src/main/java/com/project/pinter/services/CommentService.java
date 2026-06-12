package com.project.pinter.services;

import com.project.pinter.entities.Comment;
import com.project.pinter.entities.Image;
import com.project.pinter.entities.User;
import com.project.pinter.repositories.CommentRepository;
import com.project.pinter.repositories.ImageRepository;
import com.project.pinter.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    public Comment addComment(String authHeader, UUID imageId, String content) {

        String token = authHeader.replace("Bearer ", "");
        String email = jwtService.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setImage(image);
        comment.setContent(content);

        return commentRepository.save(comment);
    }

    public List<Comment> getComments(UUID imageId) {

        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        return commentRepository.findByImageOrderByCreatedAtDesc(image);
    }

    public long countComments(UUID imageId) {

        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        return commentRepository.countByImage(image);
    }
}