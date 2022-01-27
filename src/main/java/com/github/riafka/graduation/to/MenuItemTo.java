package com.github.riafka.graduation.to;

import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
public class MenuItemTo extends NamedTo {

    @NotNull
    Integer restaurantId;

    @NotNull
    int price;

    @NotNull
    LocalDate menuDate;

    public MenuItemTo(Integer id, String name, Integer restaurantId, int price, LocalDate menuDate) {
        super(id, name);
        this.restaurantId = restaurantId;
        this.price = price;
        this.menuDate = menuDate;
    }

}
