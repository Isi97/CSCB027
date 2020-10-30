package com.iispiridis.poll.Payload;

import com.iispiridis.poll.Models.Category;
import com.iispiridis.poll.Models.DBImage;
import com.iispiridis.poll.Models.User;

import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdResponse
{
    private Long id;

    private String description;

    @NotBlank
    private String title;

    private User user;

    private Instant createdAt;

    private List<DBImage> images;

    public Set<Category> categories = new HashSet<>();

    private String location = "";

    private List<CommentResponse> comments = new ArrayList<>();


    public List<CommentResponse> getComments() {
        return comments;
    }

    public void setComments(List<CommentResponse> comments) {
        this.comments = comments;
    }

    private RatingResponse ratings;

    public RatingResponse getRatings() {
        return ratings;
    }

    public void setRatings(RatingResponse ratings) {
        this.ratings = ratings;
    }

    public AdResponse() { }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<DBImage> getImages() {
        return images;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setImages(List<DBImage> images) {
        this.images = images;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAd) {
        this.createdAt = createdAd;
    }
}
