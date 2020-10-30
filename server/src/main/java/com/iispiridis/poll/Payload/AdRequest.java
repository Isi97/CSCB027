package com.iispiridis.poll.Payload;

import com.iispiridis.poll.Models.Category;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

public class AdRequest
{
    private String description;

    @NotBlank
    private String title;

    private String location = "";

    private Set<Category> categories = new HashSet<>();

    private Long id = -1L;

    public AdRequest() {

    }

    public AdRequest(String description, @NotBlank String title) {
        this.description = description;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
}
