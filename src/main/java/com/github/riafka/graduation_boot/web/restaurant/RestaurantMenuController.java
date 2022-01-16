package com.github.riafka.graduation_boot.web.restaurant;

import com.github.riafka.graduation_boot.model.RestaurantMenu;
import com.github.riafka.graduation_boot.repository.RestaurantMenuRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static com.github.riafka.graduation_boot.util.validation.ValidationUtil.checkNotFound;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@Tag(name = "Restaurant Menu Controller", description = "Operations with restaurants menu")
public class RestaurantMenuController {
    private final RestaurantMenuRepository repository;

    @GetMapping("/{restaurant_id}/menus/{id}")
    @Operation(summary = "Get menu by restaurant_id and id")
    public ResponseEntity<RestaurantMenu> get(@PathVariable int restaurant_id, @PathVariable int id) {
        log.info("get restaurantMenu by restaurant_id={}, id={}", restaurant_id, id);
        Optional<RestaurantMenu> restaurantMenu = repository.get(id, restaurant_id);
        checkNotFound(restaurantMenu.isPresent(), "restaurant_id=" + restaurant_id + " id=" + id);
        return ResponseEntity.of(repository.get(id, restaurant_id));
    }

    @GetMapping("/{restaurant_id}/menus")
    @Cacheable("restaurant_menus")
    @Operation(summary = "Get menus by restaurant_id")
    public List<RestaurantMenu> getByRestaurantId(@PathVariable int restaurant_id) {
        log.info("get restaurantMenu by restaurant_id={}", restaurant_id);
        return repository.getAllByRestaurantId(restaurant_id);
    }
}
