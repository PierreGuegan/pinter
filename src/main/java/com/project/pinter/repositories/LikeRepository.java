package com.project.pinter.repositories;

import com.project.pinter.entities.Like;
import com.project.pinter.entities.LikeId;
import com.project.pinter.entities.Image;
import com.project.pinter.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<Like, LikeId> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Like l WHERE l.image.id = :imageId")
    void deleteAllByImageId(@Param("imageId") UUID imageId);
}