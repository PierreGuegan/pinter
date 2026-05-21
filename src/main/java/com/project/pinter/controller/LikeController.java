package com.project.pinter.controller;

import com.project.pinter.services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/{imageId}")
    public void toggleLike(
            @RequestHeader("Authorization") String auth,
            @PathVariable UUID imageId
    ) {
        likeService.toggleLike(auth, imageId);
    }

    @GetMapping("/{imageId}")
    public long count(@PathVariable UUID imageId) {
        return likeService.countLikes(imageId);
    }
}
