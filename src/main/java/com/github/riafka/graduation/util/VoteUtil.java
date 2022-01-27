package com.github.riafka.graduation.util;

import com.github.riafka.graduation.model.Vote;
import com.github.riafka.graduation.to.VoteTo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class VoteUtil {
    public static VoteTo createTo(Vote vote) {
        return new VoteTo(vote.id(), vote.getRestaurant().getId(), vote.getVoteDate());
    }
}
