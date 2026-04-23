package com.project.pinter.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String url;

    private String title;

    @Column(length = 1000)
    private String description;

    private String hash;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    private String path;



    public void setPath(String path) { this.path = path; }
    public void setHash(String hash) { this.hash = hash; }
}