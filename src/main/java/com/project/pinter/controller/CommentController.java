package com.project.pinter.controller;

import com.project.pinter.dto.CommentRequest;
import com.project.pinter.entities.Comment;
import com.project.pinter.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/{imageId}")
    public Comment add(
            @RequestHeader("Authorization") String auth,
            @PathVariable UUID imageId,
            @RequestBody CommentRequest request
    ) {
        return commentService.addComment(auth, imageId, request.getContent());
    }

    @GetMapping("/{imageId}")
    public List<Comment> get(@PathVariable UUID imageId) {
        return commentService.getComments(imageId);
    }

    @GetMapping("/count/{imageId}")
    public long count(@PathVariable UUID imageId) {
        return commentService.countComments(imageId);
    }
}