package com.github.riafka.graduation_boot.util;

import com.github.riafka.graduation_boot.error.IllegalRequestDataException;
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
        return new RestaurantMenu(restaurantMenuTo.getId(), null, restaurantMenuTo.getName(), formatStringToPrice(restaurantMenuTo.getPrice()), restaurantMenuTo.getMenuDate());
    }

    public String formatPriceToString(Long price) {
        return Math.floorDiv(price, 100) + "." + (Math.floorMod(price, 100) == 0 ? "00" : Math.floorMod(price, 100));
    }

    public Long formatStringToPrice(String price) {
        String[] values = price.split("\\.");
        long result = 0L;
        if (values.length >= 1) {
            result += Long.parseLong(values[0]) * 100;
        }
        if (values.length == 2) {
            result += Long.parseLong(values[1]);
        }
        if (values.length > 2) {
            throw new IllegalRequestDataException("Incorrect format for price: " + price);
        }
        return result;
    }
}
