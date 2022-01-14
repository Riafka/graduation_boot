package com.github.riafka.graduation_boot.repository;

import com.github.riafka.graduation_boot.model.Restaurant;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
    @Query("select distinct r from Restaurant r JOIN FETCH r.menuItems rm where rm.menuDate = current_date order by r.name asc, r.id, rm.name")
    List<Restaurant> getAllAllWithMenu();

    @Override
    @Modifying
    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    Restaurant save(Restaurant restaurant);

    @Override
    @Modifying
    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    void delete(Restaurant restaurant);

    @Override
    @Modifying
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "restaurants", allEntries = true),
            @CacheEvict(value = "restaurant_menus"),
    })
    void deleteById(Integer integer);

    @Override
    @Caching(evict = {
            @CacheEvict(value = "restaurants", allEntries = true),
            @CacheEvict(value = "restaurant_menus"),
    })
    default void deleteExisted(int id) {
        BaseRepository.super.deleteExisted(id);
    }
}
