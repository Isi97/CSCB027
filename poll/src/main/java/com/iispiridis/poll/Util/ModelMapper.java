package com.iispiridis.poll.Util;

import com.iispiridis.poll.Models.Ad;
import com.iispiridis.poll.Models.Poll;
import com.iispiridis.poll.Models.User;
import com.iispiridis.poll.Payload.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModelMapper
{
    public static PollResponse mapPollToPollResponse(Poll poll, Map<Long, Long> choiceVotesMap, User creator, Long userVote) {
        PollResponse pollResponse = new PollResponse();
        pollResponse.setId(poll.getId());
        pollResponse.setQuestion(poll.getQuestion());
        pollResponse.setCreationDateTime(poll.getCreatedAt());
        pollResponse.setExpirationDateTime(poll.getExpirationDateTime());
        Instant now = Instant.now();
        pollResponse.setExpired(poll.getExpirationDateTime().isBefore(now));

        List<ChoiceResponse> choiceResponses = poll.getChoices().stream().map(choice -> {
            ChoiceResponse choiceResponse = new ChoiceResponse();
            choiceResponse.setId(choice.getId());
            choiceResponse.setText(choice.getText());

            if(choiceVotesMap.containsKey(choice.getId())) {
                choiceResponse.setVoteCount(choiceVotesMap.get(choice.getId()));
            } else {
                choiceResponse.setVoteCount(0);
            }
            return choiceResponse;
        }).collect(Collectors.toList());

        pollResponse.setChoices(choiceResponses);
        UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName());
        pollResponse.setCreatedBy(creatorSummary);

        if(userVote != null) {
            pollResponse.setSelectedChoice(userVote);
        }

        long totalVotes = pollResponse.getChoices().stream().mapToLong(ChoiceResponse::getVoteCount).sum();
        pollResponse.setTotalVotes(totalVotes);

        return pollResponse;
    }

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
