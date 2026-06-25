package com.project.pinter.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;

    @Column(length = 1000)
    private String description;

    private String hash;

    private LocalDateTime createdAt = LocalDateTime.now();

    private String originalArtist;

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getHash() { return hash; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getPath() { return path; }
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getOriginalArtist() {
        return originalArtist;
    }

    public void setOriginalArtist(String originalArtist) {
        this.originalArtist = originalArtist;
    }

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private User owner;

    private String path;

    public void setPath(String path) { this.path = path; }
    public void setHash(String hash) { this.hash = hash; }
    public void setTitle(String title) {this.title = title; }
    public void setDescription(String description) {this.description = description; }

}