package com.github.riafka.graduation_boot.util;

import com.github.riafka.graduation_boot.model.RestaurantMenu;
import com.github.riafka.graduation_boot.to.RestaurantMenuTo;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class RestaurantMenuUtils {
    public static RestaurantMenuTo createTo(RestaurantMenu restaurantMenu) {
        return new RestaurantMenuTo(restaurantMenu.getId(), restaurantMenu.getName(), restaurantMenu.getRestaurant().getId(), restaurantMenu.getPrice(), restaurantMenu.getMenuDate());
    }

    public static Optional<RestaurantMenuTo> mapTo(Optional<RestaurantMenu> restaurant) {
        return restaurant.map(RestaurantMenuUtils::createTo);
    }

    public static RestaurantMenu createNewFromTo(RestaurantMenuTo restaurantMenuTo) {
        return new RestaurantMenu(restaurantMenuTo.getId(), null, restaurantMenuTo.getName(), restaurantMenuTo.getPrice(), restaurantMenuTo.getMenuDate());
    }
}
