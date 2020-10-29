package com.iispiridis.poll.Payload;

public class RatingResponse
{
    int totalRatings;
    double averageRating;

    boolean hasUserRated = false;
    int userRating;

    public RatingResponse(){}

    public int getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(int totalRatings) {
        this.totalRatings = totalRatings;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public boolean isHasUserRated() {
        return hasUserRated;
    }

    public void setHasUserRated(boolean hasUserRated) {
        this.hasUserRated = hasUserRated;
    }

    public int getUserRating() {
        return userRating;
    }

    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }
}
