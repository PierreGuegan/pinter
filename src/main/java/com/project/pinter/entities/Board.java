package com.project.pinter.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "boards")
public class Board {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @ManyToOne
    private User user;
}