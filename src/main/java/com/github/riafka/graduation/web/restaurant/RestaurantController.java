package com.github.riafka.graduation.web.restaurant;

import com.github.riafka.graduation.model.Restaurant;
import com.github.riafka.graduation.to.RestaurantTo;
import com.github.riafka.graduation.util.RestaurantUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@Tag(name = "Restaurant Controller", description = "Operations with restaurants")
public class RestaurantController extends AbstractRestaurantController {
    static final String REST_URL = "/api/restaurants";

    @GetMapping
    @Cacheable("restaurants")
    @Operation(summary = "Get all restaurants")
    public List<Restaurant> getAll() {
        log.info("getAll restaurants");
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get restaurant by id")
    public Restaurant get(@PathVariable @Parameter(description = "id of restaurant to be searched") int id) {
        return super.get(id);
    }

    @GetMapping("/menu-for-today")
    @Operation(summary = "Get all restaurants with actual menu")
    @Cacheable("restaurants_with_menu")
    public List<RestaurantTo> getAllWithMenu() {
        log.info("getAll restaurants with menu");
        return repository.getAllWithMenu(LocalDate.now()).stream()
                .map(RestaurantUtil::createTo)
                .collect(Collectors.toList());
    }
}
