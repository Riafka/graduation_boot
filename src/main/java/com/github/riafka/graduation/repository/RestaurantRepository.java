package com.github.riafka.graduation.repository;

import com.github.riafka.graduation.model.Restaurant;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
    @Query("SELECT DISTINCT r FROM Restaurant r JOIN FETCH r.menuItems rm where rm.menuDate =:date order by r.name asc, r.id, rm.name")
    List<Restaurant> getAllWithMenu(LocalDate date);

    @Override
    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    Restaurant save(Restaurant restaurant);

    @Override
    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    void delete(Restaurant restaurant);

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "restaurants", allEntries = true),
            @CacheEvict(value = "menu_items"),
    })
    void deleteById(Integer integer);

    @Override
    @Caching(evict = {
            @CacheEvict(value = "restaurants", allEntries = true),
            @CacheEvict(value = "menu_items"),
    })
    default void deleteExisted(int id) {
        BaseRepository.super.deleteExisted(id);
    }
}
