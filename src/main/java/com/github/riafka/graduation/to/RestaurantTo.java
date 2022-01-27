package com.github.riafka.graduation.to;

import com.github.riafka.graduation.model.MenuItem;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantTo extends NamedTo {
    List<MenuItem> menuItems;

    public RestaurantTo(Integer id, String name, List<MenuItem> menuItems) {
        super(id, name);
        this.menuItems = menuItems;
    }
}
