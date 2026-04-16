package com.project.pinter.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "likes")
@IdClass(LikeId.class)
public class Like {

    @Id
    @ManyToOne
    private User user;

    @Id
    @ManyToOne
    private Image image;

    private LocalDateTime createdAt = LocalDateTime.now();
}