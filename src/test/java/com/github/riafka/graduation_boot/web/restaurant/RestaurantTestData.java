package com.github.riafka.graduation_boot.web.restaurant;

import com.github.riafka.graduation_boot.model.Restaurant;
import com.github.riafka.graduation_boot.to.RestaurantTo;
import com.github.riafka.graduation_boot.util.RestaurantUtil;
import com.github.riafka.graduation_boot.web.MatcherFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<RestaurantTo> RESTAURANT_TO_MATCHER = MatcherFactory.usingEqualsComparator(RestaurantTo.class);
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_WITH_MENUS_MATCHER = //MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class);
            MatcherFactory.usingAssertions(Restaurant.class,
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

    public static final RestaurantTo blueLagoonTo = RestaurantUtil.createTo(blueLagoon);
    public static final RestaurantTo mcDonaldsTo = RestaurantUtil.createTo(mcDonalds);
    public static final RestaurantTo krustyKrabsTo = RestaurantUtil.createTo(krustyKrabs);
    public static final RestaurantTo rigaTo = RestaurantUtil.createTo(riga);
    public static final RestaurantTo sulikoTo = RestaurantUtil.createTo(suliko);

    public static RestaurantTo getNew() {
        return new RestaurantTo(null, "New");
    }

    public static RestaurantTo getUpdated() {
        return new RestaurantTo(BLUE_LAGOON_ID, "UpdatedName");
    }
}
