package com.github.riafka.graduation_boot.util;

import com.github.riafka.graduation_boot.model.Restaurant;
import com.github.riafka.graduation_boot.to.RestaurantTo;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class RestaurantUtil {
    public static RestaurantTo createTo(Restaurant restaurant) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName());
    }

    public static Optional<RestaurantTo> mapTo(Optional<Restaurant> restaurant) {
        return restaurant.map(RestaurantUtil::createTo);
    }

    public static Restaurant createNewFromTo(RestaurantTo restaurantTo) {
        return new Restaurant(restaurantTo.getId(), restaurantTo.getName(), null);
    }
}
