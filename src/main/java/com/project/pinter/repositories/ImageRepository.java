package com.project.pinter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.*;
import java.util.UUID;

import com.project.pinter.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface ImageRepository extends JpaRepository<Image, UUID> {
}
