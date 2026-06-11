package com.project.pinter.repositories;

import com.project.pinter.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Comment c WHERE c.image.id = :imageId")
    void deleteAllByImageId(@Param("imageId") UUID imageId);
}