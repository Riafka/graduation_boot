package com.github.riafka.graduation.util;

import com.github.riafka.graduation.model.MenuItem;
import com.github.riafka.graduation.to.MenuItemTo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MenuItemUtil {
    public static MenuItemTo createTo(MenuItem menuItem) {
        return new MenuItemTo(menuItem.getId(),
                menuItem.getName(),
                menuItem.getRestaurant().getId(),
                menuItem.getPrice(),
                menuItem.getMenuDate());
    }

    public static MenuItem createFromTo(MenuItemTo menuItemTo) {
        return new MenuItem(menuItemTo.getId(),
                null,
                menuItemTo.getName(),
                menuItemTo.getPrice(),
                menuItemTo.getMenuDate());
    }
}
