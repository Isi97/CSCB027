package com.iispiridis.poll.Service;

import com.iispiridis.poll.Models.Role;
import com.iispiridis.poll.Models.User;
import com.iispiridis.poll.Payload.UserAdminViewResponse;
import com.iispiridis.poll.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class AdminService
{
    @Autowired
    CommentService commentService;

    @Autowired
    RatingService ratingService;

    @Autowired
    AdService adService;

    @Autowired
    UserRepository userRepository;

    public UserAdminViewResponse getUserAdminView(Long uid)
    {
        UserAdminViewResponse view = new UserAdminViewResponse();

        view.setComments(commentService.getUserComments(uid));
        view.setAds(adService.getUserAds(uid));
        view.setRatings(ratingService.getUserRatings(uid));

        User u = userRepository.getOne(uid);
        List<String> authorities = new ArrayList<>();

        for (Role r : u.getRoles())
        {
            authorities.add(r.getName().toString());
        }

        view.setAuthorities(authorities);

        return view;
    }
}
