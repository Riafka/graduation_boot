package com.github.riafka.graduation_boot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.riafka.graduation_boot.util.RestaurantMenuUtils;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "restaurant_menu", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "menu_date"}, name = "unique_name_menu_date_idx"))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"restaurant"})
public class RestaurantMenu extends NamedEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonBackReference
    private Restaurant restaurant;
    @NotNull
    @DecimalMax(value = "10000000000")
    @DecimalMin(value = "1")
    private long price;
    @NotNull
    @Column(name = "menu_date")
    private LocalDate menuDate;

    public RestaurantMenu(Integer id, Restaurant restaurant, String name, long price, LocalDate menuDate) {
        super(id, name);
        this.restaurant = restaurant;
        this.price = price;
        this.menuDate = menuDate;
    }

    @JsonProperty
    public String getPrice() {
        return RestaurantMenuUtils.formatPriceToString(price);
    }

    @JsonProperty
    public void setPrice(String price) {
        this.price = RestaurantMenuUtils.formatStringToPrice(price);
    }
}
