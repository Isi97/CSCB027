package com.iispiridis.poll.Payload;

import java.util.List;

public class UserAdminViewResponse
{
    List<CommentResponse> comments;
    List<AdResponse> ads;
    List<RatingAdminViewResponse> ratings;
    List<String> authorities;

    public UserAdminViewResponse(){}

    public List<CommentResponse> getComments() {
        return comments;
    }

    public void setComments(List<CommentResponse> comments) {
        this.comments = comments;
    }

    public List<AdResponse> getAds() {
        return ads;
    }

    public void setAds(List<AdResponse> ads) {
        this.ads = ads;
    }

    public List<RatingAdminViewResponse> getRatings() {
        return ratings;
    }

    public void setRatings(List<RatingAdminViewResponse> ratings) {
        this.ratings = ratings;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }
}
