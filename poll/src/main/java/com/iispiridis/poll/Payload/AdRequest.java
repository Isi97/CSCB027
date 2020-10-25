package com.iispiridis.poll.Payload;

import javax.validation.constraints.NotBlank;

public class AdRequest
{
    private String description;

    @NotBlank
    private String title;

    public AdRequest() {

    }

    public AdRequest(String description, @NotBlank String title) {
        this.description = description;
        this.title = title;
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
}
