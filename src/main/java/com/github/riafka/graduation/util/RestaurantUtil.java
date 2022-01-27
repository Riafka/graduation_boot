package com.github.riafka.graduation.util;

import com.github.riafka.graduation.model.Restaurant;
import com.github.riafka.graduation.to.RestaurantTo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RestaurantUtil {
    public static RestaurantTo createTo(Restaurant restaurant) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getMenuItems());
    }
}
