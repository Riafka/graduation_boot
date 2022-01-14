package com.github.riafka.graduation_boot.web.restaurant;

import com.github.riafka.graduation_boot.model.RestaurantMenu;
import com.github.riafka.graduation_boot.repository.RestaurantMenuRepository;
import com.github.riafka.graduation_boot.repository.RestaurantRepository;
import com.github.riafka.graduation_boot.to.RestaurantMenuTo;
import com.github.riafka.graduation_boot.util.RestaurantMenuUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static com.github.riafka.graduation_boot.util.validation.ValidationUtil.assureIdConsistent;
import static com.github.riafka.graduation_boot.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminRestaurantMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminRestaurantMenuController {
    static final String REST_URL = "/api/admin/restaurants/{restaurant_id}/menus";

    private final RestaurantMenuRepository repository;
    private final RestaurantRepository restaurantRepository;

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id, @PathVariable int restaurant_id) {
        log.info("delete restaurantMenu by restaurant_id{} and id {}", restaurant_id, id);
        repository.deleteExistedByRestaurantIdAndId(restaurant_id, id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByRestaurantId(@PathVariable int restaurant_id) {
        log.info("delete restaurantMenu by restaurant_id{}", restaurant_id);
        repository.deleteExistedByRestaurantId(restaurant_id);
    }

    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantMenuTo> createWithLocation(@Valid @RequestBody RestaurantMenuTo restaurantMenuTo, @PathVariable int restaurant_id) {
        log.info("create {}", restaurantMenuTo);
        checkNew(restaurantMenuTo);
        RestaurantMenu restaurantMenu = RestaurantMenuUtils.createNewFromTo(restaurantMenuTo);
        restaurantMenu.setRestaurant(restaurantRepository.getById(restaurant_id));
        RestaurantMenu created = repository.save(restaurantMenu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurant_id, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(RestaurantMenuUtils.createTo(created));
    }

    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody RestaurantMenuTo restaurantMenuTo, @PathVariable int id, @PathVariable int restaurant_id) {
        log.info("update {} with id={},restaurant_id={}", restaurantMenuTo, id, restaurant_id);
        assureIdConsistent(restaurantMenuTo, id);
        repository.checkBelong(id, restaurant_id);
        RestaurantMenu restaurantMenu = RestaurantMenuUtils.createNewFromTo(restaurantMenuTo);
        restaurantMenu.setRestaurant(restaurantRepository.getById(restaurant_id));
        repository.save(restaurantMenu);
    }
}
