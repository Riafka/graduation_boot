package com.github.riafka.graduation.web.restaurant;

import com.github.riafka.graduation.model.Restaurant;
import com.github.riafka.graduation.repository.RestaurantRepository;
import com.github.riafka.graduation.to.RestaurantTo;
import com.github.riafka.graduation.util.RestaurantUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import static com.github.riafka.graduation.util.validation.ValidationUtil.checkNotFoundOptional;

@Slf4j
public abstract class AbstractRestaurantController {

    @Autowired
    protected RestaurantRepository repository;

    public RestaurantTo get(int id) {
        log.info("get restaurant {}", id);
        Restaurant restaurant = checkNotFoundOptional(repository.findById(id), id);
        return RestaurantUtil.createTo(restaurant);
    }
}
