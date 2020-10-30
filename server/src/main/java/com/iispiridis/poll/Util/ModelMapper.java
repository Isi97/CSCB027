package com.iispiridis.poll.Util;

import com.iispiridis.poll.Models.Ad;
import com.iispiridis.poll.Models.User;
import com.iispiridis.poll.Payload.AdResponse;
import com.iispiridis.poll.Payload.CommentResponse;
import com.iispiridis.poll.Payload.RatingResponse;

import java.util.List;

public class ModelMapper
{

    public static AdResponse mapAdResponse(Ad ad, User creator, List<CommentResponse> comments, RatingResponse ratingResponse) // use please
    {
            AdResponse adResponse = new AdResponse();

            adResponse.setCreatedAt(ad.getCreatedAt());
            adResponse.setUser(creator);
            adResponse.setDescription(ad.getDescription());
            adResponse.setTitle(ad.getTitle());
            adResponse.setId(ad.getId());
            adResponse.setLocation(ad.getLocation());
            adResponse.setComments(comments);
            adResponse.setRatings(ratingResponse);

            adResponse.setCategories(ad.getCategories());

            return adResponse;

    }
}
