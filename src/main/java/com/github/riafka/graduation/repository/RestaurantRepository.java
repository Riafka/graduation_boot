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
    @Query("SELECT DISTINCT r FROM Restaurant r JOIN FETCH r.menuItems rm WHERE rm.menuDate =:date ORDER BY r.name asc, r.id, rm.name")
    List<Restaurant> getAllWithMenu(LocalDate date);

    @Query("SELECT DISTINCT r FROM Restaurant r JOIN FETCH r.menuItems rm" +
            " WHERE r.id =:restaurantId AND rm.menuDate =:date ORDER BY r.name asc, r.id, rm.name")
    Restaurant getWithMenuByIdAndDate(int restaurantId, LocalDate date);

    @Override
    @Transactional
    @CacheEvict(value = {"restaurants", "restaurants_with_menu"}, allEntries = true)
    Restaurant save(Restaurant restaurant);

    @Override
    @Transactional
    @CacheEvict(value = {"restaurants", "restaurants_with_menu"}, allEntries = true)
    void delete(Restaurant restaurant);

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = {"restaurants", "restaurants_with_menu"}, allEntries = true),
            @CacheEvict(value = "menu_items")
    })
    void deleteById(Integer integer);

    @Override
    @Caching(evict = {
            @CacheEvict(value = {"restaurants", "restaurants_with_menu"}, allEntries = true),
            @CacheEvict(value = "menu_items")
    })
    default void deleteExisted(int id) {
        BaseRepository.super.deleteExisted(id);
    }
}
