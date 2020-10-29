package com.iispiridis.poll.Controller;


import com.iispiridis.poll.Payload.ApiResponse;
import com.iispiridis.poll.Payload.CommentRequest;
import com.iispiridis.poll.Payload.CommentResponse;
import com.iispiridis.poll.Repositories.CommentRepository;
import com.iispiridis.poll.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController
{
    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;

    @PostMapping
    ResponseEntity<?> createComment(@RequestBody CommentRequest commentRequest)
    {
        commentService.createComment(commentRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("")
                .buildAndExpand().toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Comment posted!"));
    }

    @GetMapping("/ad/{adId}")
    List<CommentResponse> getAdComments(@PathVariable(name="adId") Long id)
    {
        return commentService.getMappedAdComments(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteComment(@PathVariable(name="id") Long id)
    {
        commentRepository.deleteById(id);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("")
                .buildAndExpand().toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "Comment deleted without errors"));
    }

    @GetMapping("/user/{uid}")
    List<CommentResponse> getUserComments(@PathVariable(name="uid") Long id)
    {
        return commentService.getUserComments(id);
    }
}
