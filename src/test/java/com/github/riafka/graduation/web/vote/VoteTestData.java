package com.github.riafka.graduation.web.vote;

import com.github.riafka.graduation.to.VoteTo;
import com.github.riafka.graduation.web.MatcherFactory;

import java.time.LocalDate;

import static com.github.riafka.graduation.web.restaurant.RestaurantTestData.BLUE_LAGOON_ID;
import static com.github.riafka.graduation.web.restaurant.RestaurantTestData.SULIKO_ID;

public class VoteTestData {
    public static final MatcherFactory.Matcher<VoteTo> VOTE_TO_MATCHER = MatcherFactory.usingEqualsComparator(VoteTo.class);

    public static final int USER_VOTE_ID = 1;
    public static final int ADMIN_VOTE_ID = 2;
    public static final VoteTo user_vote = new VoteTo(USER_VOTE_ID, BLUE_LAGOON_ID, LocalDate.now());
    public static final VoteTo admin_vote = new VoteTo(ADMIN_VOTE_ID, SULIKO_ID, LocalDate.now());

    public static VoteTo getNew() {
        return new VoteTo(null, BLUE_LAGOON_ID, LocalDate.now());
    }
}
