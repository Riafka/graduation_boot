package com.github.riafka.graduation.web.restaurant;

import com.github.riafka.graduation.model.Restaurant;
import com.github.riafka.graduation.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import static com.github.riafka.graduation.util.validation.ValidationUtil.checkNotFoundOptional;

@Slf4j
public abstract class AbstractRestaurantController {

    @Autowired
    protected RestaurantRepository repository;

    public Restaurant get(int id) {
        log.info("get restaurant {}", id);
        return checkNotFoundOptional(repository.findById(id), id);
    }
}
