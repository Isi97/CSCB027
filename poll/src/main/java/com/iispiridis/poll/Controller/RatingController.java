package com.iispiridis.poll.Controller;


import com.iispiridis.poll.Payload.ApiResponse;
import com.iispiridis.poll.Payload.RatingRequest;
import com.iispiridis.poll.Payload.RatingResponse;
import com.iispiridis.poll.Repositories.RatingRepository;
import com.iispiridis.poll.Security.CurrentUser;
import com.iispiridis.poll.Security.UserPrincipal;
import com.iispiridis.poll.Service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/ratings")
public class RatingController
{
    @Autowired
    RatingService ratingService;

    @Autowired
    RatingRepository ratingRepository;

    @PostMapping
    ResponseEntity<?> createRating(@RequestBody RatingRequest ratingRequest)
    {
        ratingService.saveRating(ratingRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("")
                .buildAndExpand().toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Rating accepted!"));
    }

    @GetMapping("/{adId}")
    RatingResponse getAdRatingResponse(@PathVariable(name="adId") Long id, @CurrentUser UserPrincipal currentUser)
    {
        return ratingService.getAdRatingResponse(id, currentUser.getId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRating(@PathVariable(name="id") Long id)
    {
        ratingRepository.deleteById(id);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("")
                .buildAndExpand().toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "Ad deleted without errors"));
    }
}
