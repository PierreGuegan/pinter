package com.project.pinter.repositories;

import com.project.pinter.entities.Comment;
import com.project.pinter.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

    List<Comment> findByImageOrderByCreatedAtDesc(Image image);

    long countByImage(Image image);
}