package com.github.riafka.graduation_boot.repository;

import com.github.riafka.graduation_boot.error.DataConflictException;
import com.github.riafka.graduation_boot.model.RestaurantMenu;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.github.riafka.graduation_boot.util.validation.ValidationUtil.checkModification;

@Transactional(readOnly = true)
public interface RestaurantMenuRepository extends BaseRepository<RestaurantMenu> {

    @Transactional
    @Modifying
    @CacheEvict(value = "restaurant_menus")
    @Query("DELETE FROM RestaurantMenu rm WHERE rm.restaurant.id=:id")
    int deleteByRestaurantId(int id);

    @Transactional
    @Modifying
    @CacheEvict(value = "restaurant_menus", key = "#restaurantId")
    @Query("DELETE FROM RestaurantMenu rm WHERE rm.restaurant.id=:restaurantId AND rm.id=:id")
    int deleteByRestaurantIdAndId(int restaurantId, int id);

    @CacheEvict(value = "restaurant_menus")
    default void deleteExistedByRestaurantId(int id) {
        checkModification(deleteByRestaurantId(id), id);
    }

    @CacheEvict(value = "restaurant_menus", key = "#restaurantId")
    default void deleteExistedByRestaurantIdAndId(int restaurantId, int id) {
        checkModification(deleteByRestaurantIdAndId(restaurantId, id), id);
    }

    @Query("select r from RestaurantMenu r where r.restaurant.id = ?1 and r.menuDate = current_date order by r.name asc, r.id")
    List<RestaurantMenu> getAllByRestaurantId(int restaurantId);

    @Query("select r from RestaurantMenu r where r.id = ?1 AND r.restaurant.id = ?2 and r.menuDate = current_date order by r.name asc, r.id")
    Optional<RestaurantMenu> get(int id, int restaurantId);

    default RestaurantMenu checkBelong(int id, int restaurantId) {
        return get(id, restaurantId).orElseThrow(
                () -> new DataConflictException("RestaurantMenu id=" + id + " doesn't belong to Restaurant id=" + restaurantId));
    }

    @Override
    @Modifying
    @Transactional
    @CacheEvict(value = "restaurant_menus", key = "#restaurantMenu.restaurant.id()")
    RestaurantMenu save(RestaurantMenu restaurantMenu);

    @Override
    @Modifying
    @Transactional
    @CacheEvict(value = "restaurant_menus", key = "#restaurantMenu.restaurant.id()")
    void delete(RestaurantMenu restaurantMenu);
}
