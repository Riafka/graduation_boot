package com.github.riafka.graduation_boot.web.restaurant;

import com.github.riafka.graduation_boot.model.Restaurant;
import com.github.riafka.graduation_boot.repository.RestaurantRepository;
import com.github.riafka.graduation_boot.to.RestaurantTo;
import com.github.riafka.graduation_boot.util.RestaurantUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.riafka.graduation_boot.util.validation.ValidationUtil.checkNotFoundWithId;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@Tag(name = "Restaurant Controller", description = "Operations with restaurants")
public class RestaurantController {
    static final String REST_URL = "/api/restaurants";

    private final RestaurantRepository repository;

    @GetMapping
    @Cacheable("restaurants")
    @Operation(summary = "Get all restaurants")
    public List<RestaurantTo> getAll() {
        log.info("getAll restaurants");
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name", "id"))
                .stream()
                .map(RestaurantUtil::createTo)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get restaurant by id")
    public ResponseEntity<RestaurantTo> get(@PathVariable int id) {
        log.info("get restaurant {}", id);
        Optional<Restaurant> restaurant = repository.findById(id);
        checkNotFoundWithId(restaurant.isPresent(), id);
        return ResponseEntity.of(RestaurantUtil.mapTo(restaurant));
    }

    @GetMapping("/menus")
    @Operation(summary = "Get all restaurants with menu")
    public List<Restaurant> getAllWithMenu() {
        log.info("getAll restaurants with menu");
        return repository.getAllAllWithMenu();
    }
}
