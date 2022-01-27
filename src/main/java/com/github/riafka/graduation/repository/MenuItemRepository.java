package com.github.riafka.graduation.repository;

import com.github.riafka.graduation.error.DataConflictException;
import com.github.riafka.graduation.model.MenuItem;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.github.riafka.graduation.util.validation.ValidationUtil.checkModification;

@Transactional(readOnly = true)
public interface MenuItemRepository extends BaseRepository<MenuItem> {

    @Transactional
    @Modifying
    @Caching(evict = {
            @CacheEvict(value = "menu_items", key = "#restaurantId"),
            @CacheEvict(value = "restaurants_with_menu", allEntries = true)
    })
    @Query("DELETE FROM MenuItem rm WHERE rm.restaurant.id=:restaurantId AND rm.menuDate=:date")
    int deleteByRestaurantIdAndDate(int restaurantId, LocalDate date);

    @Transactional
    @Modifying
    @Caching(evict = {
            @CacheEvict(value = "menu_items", key = "#restaurantId"),
            @CacheEvict(value = "restaurants_with_menu", allEntries = true)
    })
    @Query("DELETE FROM MenuItem rm WHERE rm.restaurant.id=:restaurantId AND rm.id=:id")
    int deleteByRestaurantIdAndId(int restaurantId, int id);

    @Caching(evict = {
            @CacheEvict(value = "menu_items", key = "#restaurantId"),
            @CacheEvict(value = "restaurants_with_menu", allEntries = true)
    })
    default void deleteExistedByRestaurantIdAndDate(int restaurantId, LocalDate date) {
        checkModification(deleteByRestaurantIdAndDate(restaurantId, date), restaurantId);
    }

    @Caching(evict = {
            @CacheEvict(value = "menu_items", key = "#restaurantId"),
            @CacheEvict(value = "restaurants_with_menu", allEntries = true)
    })
    default void deleteExistedByRestaurantIdAndId(int restaurantId, int id) {
        checkModification(deleteByRestaurantIdAndId(restaurantId, id), id);
    }

    @Query("SELECT mi FROM MenuItem mi WHERE mi.restaurant.id = ?1 AND mi.menuDate = ?2 order by mi.name asc")
    List<MenuItem> getAllByRestaurantId(int restaurantId, LocalDate date);

    @Query("SELECT mi FROM MenuItem mi WHERE mi.id = ?1 AND mi.restaurant.id = ?2 AND mi.menuDate = ?3")
    Optional<MenuItem> get(int id, int restaurantId, LocalDate date);

    @Query("SELECT mi FROM MenuItem mi WHERE mi.id = ?1 AND mi.restaurant.id = ?2")
    Optional<MenuItem> get(int id, int restaurantId);

    default MenuItem checkBelong(int id, int restaurantId) {
        return get(id, restaurantId).orElseThrow(
                () -> new DataConflictException("RestaurantMenu id=" + id + " doesn't belong to Restaurant id=" + restaurantId));
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "menu_items", key = "#menuItem.restaurant.getId()"),
            @CacheEvict(value = "restaurants_with_menu", allEntries = true)
    })
    MenuItem save(MenuItem menuItem);

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "menu_items", key = "#menuItem.restaurant.getId()"),
            @CacheEvict(value = "restaurants_with_menu", allEntries = true)
    })
    void delete(MenuItem menuItem);
}
