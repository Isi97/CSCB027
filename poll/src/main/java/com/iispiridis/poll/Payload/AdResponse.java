package com.iispiridis.poll.Payload;

import com.iispiridis.poll.Models.DBImage;
import com.iispiridis.poll.Models.User;

import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;

public class AdResponse
{
    private Long id;

    private String description;

    @NotBlank
    private String title;

    private User user;

    private Instant createdAt;

    private List<DBImage> images;

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
