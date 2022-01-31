package com.github.riafka.graduation.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "menu_item", uniqueConstraints = @UniqueConstraint(columnNames = {"restaurant_id", "menu_date", "name"}, name = "unique_name_menu_date_idx"))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class MenuItem extends NamedEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonBackReference
    @ToString.Exclude
    @Hidden
    private Restaurant restaurant;

    @Column(nullable = false)
    @NotNull
    private int price;

    @NotNull
    @Column(name = "menu_date", nullable = false)
    private LocalDate menuDate;

    public MenuItem(Integer id, Restaurant restaurant, String name, @NotNull int price, LocalDate menuDate) {
        super(id, name);
        this.restaurant = restaurant;
        this.price = price;
        this.menuDate = menuDate;
    }
}
