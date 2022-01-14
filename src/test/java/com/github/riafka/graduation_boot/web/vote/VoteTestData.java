package com.github.riafka.graduation_boot.web.vote;

import com.github.riafka.graduation_boot.to.VoteTo;
import com.github.riafka.graduation_boot.web.MatcherFactory;

import java.time.LocalDate;

import static com.github.riafka.graduation_boot.web.restaurant.RestaurantTestData.BLUE_LAGOON_ID;
import static com.github.riafka.graduation_boot.web.restaurant.RestaurantTestData.SULIKO_ID;
import static com.github.riafka.graduation_boot.web.user.UserTestData.*;

public class VoteTestData {
    public static final MatcherFactory.Matcher<VoteTo> VOTE_TO_MATCHER = MatcherFactory.usingEqualsComparator(VoteTo.class);

    public static final int USER_VOTE_ID = 1;
    public static final int ADMIN_VOTE_ID = 2;
    public static final VoteTo user_vote = new VoteTo(USER_VOTE_ID, USER_ID, BLUE_LAGOON_ID, LocalDate.now());
    public static final VoteTo admin_vote = new VoteTo(ADMIN_VOTE_ID, ADMIN_ID, SULIKO_ID, LocalDate.now());

    public static VoteTo getNew() {
        return new VoteTo(null, USER_ID_2, BLUE_LAGOON_ID, LocalDate.now());
    }
}
