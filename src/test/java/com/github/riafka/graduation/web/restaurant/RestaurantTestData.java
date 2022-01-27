package com.github.riafka.graduation.web.restaurant;

import com.github.riafka.graduation.model.Restaurant;
import com.github.riafka.graduation.to.RestaurantTo;
import com.github.riafka.graduation.web.MatcherFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER;

    static {
        RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "menuItems");
    }

    public static final MatcherFactory.Matcher<RestaurantTo> RESTAURANT_TO_MATCHER =
            MatcherFactory.usingAssertions(RestaurantTo.class,
                    //     No need use ignoringAllOverriddenEquals, see https://assertj.github.io/doc/#breaking-changes
                    (a, e) -> assertThat(a).usingRecursiveComparison()
                            .ignoringFields("menuItems.restaurant").isEqualTo(e),
                    (a, e) -> assertThat(a).usingRecursiveComparison()
                            .ignoringFields("menuItems.restaurant").isEqualTo(e));
    public static final int BLUE_LAGOON_ID = 1;
    public static final int MC_DONALDS_ID = 2;
    public static final int KRUSTY_KRABS_ID = 3;
    public static final int RIGA_ID = 4;
    public static final int SULIKO_ID = 5;
    public static final int NOT_FOUND = 100;

    public static final Restaurant blueLagoon = new Restaurant(BLUE_LAGOON_ID, "Blue Lagoon", null);
    public static final Restaurant mcDonalds = new Restaurant(MC_DONALDS_ID, "MC Donald's", null);
    public static final Restaurant krustyKrabs = new Restaurant(KRUSTY_KRABS_ID, "Krusty Krabs", null);
    public static final Restaurant riga = new Restaurant(RIGA_ID, "Riga", null);
    public static final Restaurant suliko = new Restaurant(SULIKO_ID, "Suliko", null);

    public static Restaurant getNew() {
        return new Restaurant(null, "New", null);
    }

    public static Restaurant getUpdated() {
        return new Restaurant(BLUE_LAGOON_ID, "UpdatedName", null);
    }
}
