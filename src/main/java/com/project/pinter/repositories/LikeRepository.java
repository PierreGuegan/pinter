package com.project.pinter.repositories;

import com.project.pinter.entities.Like;
import com.project.pinter.entities.LikeId;
import com.project.pinter.entities.Image;
import com.project.pinter.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, LikeId> {

    long countByImage(Image image);

    boolean existsByUserAndImage(User user, Image image);

    void deleteByUserAndImage(User user, Image image);
}