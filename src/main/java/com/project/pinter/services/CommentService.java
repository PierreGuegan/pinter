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
                .orElseThrow(() -> new RuntimeException("USER NOT FOUND: " + email));

        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("IMAGE NOT FOUND: " + imageId));

        System.out.println("AUTH HEADER = " + authHeader);

        System.out.println("TOKEN = " + token);
        System.out.println("EMAIL = " + email);

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

    public void deleteComment(String authHeader, UUID commentId) {

        String token = authHeader.replace("Bearer ", "");
        String email = jwtService.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("USER NOT FOUND"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("COMMENT NOT FOUND"));

        // sécurité : seul l’auteur peut supprimer
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("NOT AUTHORIZED");
        }

        commentRepository.delete(comment);
    }
}