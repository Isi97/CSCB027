package com.iispiridis.poll.Service;

import com.iispiridis.poll.Models.Ad;
import com.iispiridis.poll.Models.User;
import com.iispiridis.poll.Models.adComment;
import com.iispiridis.poll.Payload.CommentRequest;
import com.iispiridis.poll.Payload.CommentResponse;
import com.iispiridis.poll.Repositories.AdRepository;
import com.iispiridis.poll.Repositories.CommentRepository;
import com.iispiridis.poll.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    AdRepository adRepository;

    @Autowired
    UserRepository userRepository;

    public adComment createComment(CommentRequest comment)
    {
        Ad a = adRepository.getOne(comment.getAdId());
        adComment newComment = new adComment(comment.getText(), a);
        return commentRepository.save(newComment);
    }

    public List<adComment> getAdComments(Long id)
    {
        return commentRepository.findAllByAd(id);
    }

    public List<CommentResponse> getMappedAdComments(Long id)
    {
        List<adComment> comments = commentRepository.findAllByAd(id);
        List<CommentResponse> commentResponseList = new ArrayList<>();

        for ( adComment c : comments )
        {
            CommentResponse newResponse = new CommentResponse();
            newResponse.setAdId(c.getAd().getId());
            newResponse.setCreatedAt(c.getCreatedAt());
            newResponse.setId(c.getId());
            newResponse.setText(c.getText());

            User u = userRepository.getOne(c.getCreatedBy());
            newResponse.setAuthor_name(u.getName());
            newResponse.setAuthor_username(u.getUsername());

            commentResponseList.add(newResponse);
        }

        return commentResponseList;
    }

    public List<CommentResponse> getUserComments(Long userId)
    {
        List<adComment> comments = commentRepository.findByUserId(userId);
        List<CommentResponse> commentResponses = new ArrayList<>();

        for ( adComment c : comments )
        {
            CommentResponse commentResponse = new CommentResponse();
            commentResponse.setText(c.getText());
            commentResponse.setCreatedAt(c.getCreatedAt());
            commentResponses.add(commentResponse);
            commentResponse.setId(c.getId());
        }

        return commentResponses;
    }
}
