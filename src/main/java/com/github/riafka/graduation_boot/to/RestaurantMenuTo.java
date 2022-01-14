package com.github.riafka.graduation_boot.to;

import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.persistence.Column;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
public class RestaurantMenuTo extends NamedTo {
    @NotNull
    Integer restaurantId;

    @NotNull
    @DecimalMax(value = "10000000000")
    @DecimalMin(value = "1")
    long price;
    @NotNull
    @Column(name = "menu_date")
    LocalDate menuDate;

    public RestaurantMenuTo(Integer id, String name, Integer restaurantId, long price, LocalDate menuDate) {
        super(id, name);
        this.restaurantId = restaurantId;
        this.price = price;
        this.menuDate = menuDate;
    }
}
