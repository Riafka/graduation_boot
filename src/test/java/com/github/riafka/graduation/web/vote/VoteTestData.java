package com.github.riafka.graduation.web.vote;

import com.github.riafka.graduation.to.VoteTo;
import com.github.riafka.graduation.web.MatcherFactory;

import java.time.LocalDate;

import static com.github.riafka.graduation.web.restaurant.RestaurantTestData.*;

public class VoteTestData {
    public static final MatcherFactory.Matcher<VoteTo> VOTE_TO_MATCHER = MatcherFactory.usingEqualsComparator(VoteTo.class);

    public static final int USER_VOTE_ID = 1;
    public static final int USER_VOTE_ID_1DAY_OLD = 3;
    public static final int USER_VOTE_ID_2DAYS_OLD = 5;
    public static final VoteTo user_vote = new VoteTo(USER_VOTE_ID, BLUE_LAGOON_ID, LocalDate.now());
    public static final VoteTo user_vote_1day_old = new VoteTo(USER_VOTE_ID_1DAY_OLD, MC_DONALDS_ID, LocalDate.now().minusDays(1));
    public static final VoteTo user_vote_2days_old = new VoteTo(USER_VOTE_ID_2DAYS_OLD, RIGA_ID, LocalDate.now().minusDays(2));

    public static VoteTo getNew() {
        return new VoteTo(null, BLUE_LAGOON_ID, LocalDate.now());
    }
}
