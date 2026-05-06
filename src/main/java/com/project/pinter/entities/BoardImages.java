package com.project.pinter.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@DiscriminatorValue("BOARD")
public class BoardImages {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Id
    @ManyToOne
    private Board board;

    @Id
    @ManyToOne
    private Image image;


}
