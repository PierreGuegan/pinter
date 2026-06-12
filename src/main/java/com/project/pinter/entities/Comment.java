package com.project.pinter.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue
    private UUID id;

    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    private User user;

    @ManyToOne
    private Image image;

    public UUID getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public Image getImage() {
        return image;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setContent(String content) {
        this.content = content;
    }
}