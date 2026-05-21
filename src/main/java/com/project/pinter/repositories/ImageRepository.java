package com.project.pinter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.*;
import java.util.UUID;

import com.project.pinter.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

import java.util.UUID;


public interface ImageRepository extends JpaRepository<Image, UUID> {

    Image findByHash(String hash);

    @Query("""
            SELECT i FROM Image i
            JOIN i.owner o
            WHERE LOWER(i.title) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(i.description) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(o.username) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(i.originalArtist) LIKE LOWER(CONCAT('%', :query, '%'))
            """)
    List<Image> search(@Param("query") String query);
}
