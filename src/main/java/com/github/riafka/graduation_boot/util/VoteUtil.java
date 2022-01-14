package com.github.riafka.graduation_boot.util;

import com.github.riafka.graduation_boot.model.Vote;
import com.github.riafka.graduation_boot.to.VoteTo;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class VoteUtil {
    public static VoteTo createTo(Vote vote) {
        return new VoteTo(vote.id(), vote.getUser().getId(), vote.getRestaurant().getId(), vote.getVoteDate());
    }

    public static Optional<VoteTo> mapTo(Optional<Vote> vote) {
        return vote.map(VoteUtil::createTo);
    }
}
