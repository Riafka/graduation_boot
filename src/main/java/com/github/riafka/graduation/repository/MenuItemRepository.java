package com.github.riafka.graduation.repository;

import com.github.riafka.graduation.error.DataConflictException;
import com.github.riafka.graduation.model.MenuItem;
import org.springframework.cache.annotation.CacheEvict;
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
    @CacheEvict(value = "menu_items")
    @Query("DELETE FROM MenuItem rm WHERE rm.restaurant.id=:id AND rm.menuDate=:date")
    int deleteByRestaurantIdAndDate(int id, LocalDate date);

    @Transactional
    @Modifying
    @CacheEvict(value = "menu_items", key = "#restaurantId")
    @Query("DELETE FROM MenuItem rm WHERE rm.restaurant.id=:restaurantId AND rm.id=:id")
    int deleteByRestaurantIdAndId(int restaurantId, int id);

    @CacheEvict(value = "menu_items")
    default void deleteExistedByRestaurantIdAndDate(int id, LocalDate date) {
        checkModification(deleteByRestaurantIdAndDate(id, date), id);
    }

    @CacheEvict(value = "menu_items", key = "#restaurantId")
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
    @CacheEvict(value = "menu_items", key = "#menuItem.restaurant.getId()")
    MenuItem save(MenuItem menuItem);

    @Override
    @Transactional
    @CacheEvict(value = "menu_items", key = "#menuItem.restaurant.getId()")
    void delete(MenuItem menuItem);
}
