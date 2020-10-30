package com.iispiridis.poll.Service;

import com.iispiridis.poll.Models.Rating;
import com.iispiridis.poll.Models.User;
import com.iispiridis.poll.Payload.RatingAdminViewResponse;
import com.iispiridis.poll.Payload.RatingRequest;
import com.iispiridis.poll.Payload.RatingResponse;
import com.iispiridis.poll.Repositories.AdRepository;
import com.iispiridis.poll.Repositories.RatingRepository;
import com.iispiridis.poll.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RatingService {

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    AdRepository adRepository;

    @Autowired
    UserRepository userRepository;

    public Rating saveRating(RatingRequest request)
    {
        Rating rating = new Rating();
        rating.setAd(adRepository.findById(request.getAdId()).get());
        rating.setUser(userRepository.getOne(request.getUserId()));
        rating.setValue(request.getValue());

        return ratingRepository.save(rating);
    }

    public List<RatingAdminViewResponse> getUserRatings(Long uid)
    {
        List<Rating> ratings = ratingRepository.getUserRatings(uid);
        User u = userRepository.getOne(uid);

        List<RatingAdminViewResponse> response = new ArrayList<>();
        for ( Rating r : ratings )
        {
            RatingAdminViewResponse adminViewResponse = new RatingAdminViewResponse();
            adminViewResponse.setAd_id(r.getAd().getId());
            adminViewResponse.setAd_title(r.getAd().getTitle());
            adminViewResponse.setName(u.getName());
            adminViewResponse.setUsername(u.getUsername());
            adminViewResponse.setValue(r.getValue());
            adminViewResponse.setRating_id(r.getId());

            response.add(adminViewResponse);
        }

        return response;
    }

    public RatingResponse getAdRatingResponse(Long id, Long userId)
    {
        RatingResponse response = new RatingResponse();
        List<Rating> adRatings = ratingRepository.getAdRatings(id);

        int ratingSum = 0;
        boolean currentUserRated = false;
        int currentUserRating = -1;

        for (Rating r : adRatings){
            ratingSum += r.getValue();
            if ( r.getUser().getId() == userId ) {
                currentUserRated = true;
                currentUserRating = r.getValue();
            }
        }

        double averageRating = 0;
        if ( adRatings.size() > 0 )  averageRating = ratingSum/adRatings.size();

        if ( userId == -1 )
        {
            currentUserRated = true;
        }

        response.setTotalRatings(adRatings.size());
        response.setAverageRating(averageRating);
        response.setHasUserRated(currentUserRated);
        response.setUserRating(currentUserRating);

        return response;
    }
}
