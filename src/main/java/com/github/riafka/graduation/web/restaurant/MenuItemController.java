package com.github.riafka.graduation.web.restaurant;

import com.github.riafka.graduation.model.MenuItem;
import com.github.riafka.graduation.repository.MenuItemRepository;
import com.github.riafka.graduation.to.MenuItemTo;
import com.github.riafka.graduation.util.MenuItemUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.riafka.graduation.util.validation.ValidationUtil.checkNotFoundOptional;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@Tag(name = "Menu Item Controller", description = "Operations with menu items")
public class MenuItemController {
    private final MenuItemRepository repository;

    @GetMapping("/{restaurant_id}/menu_items-for-today/{id}")
    @Operation(summary = "Get actual menu_item by restaurant_id and id")
    public MenuItemTo get(@PathVariable(name = "restaurant_id")
                          @Parameter(description = "id of restaurant by which menu is searched") int restaurantId,
                          @PathVariable @Parameter(description = "id of menu to be searched") int id) {
        log.info("get menuItems by restaurant_id={}, id={}", restaurantId, id);
        MenuItem restaurantMenu = checkNotFoundOptional(repository.get(id,
                restaurantId,
                LocalDate.now()), "restaurant_id=" + restaurantId + " id=" + id);
        return MenuItemUtil.createTo(restaurantMenu);
    }

    @GetMapping("/{restaurant_id}/menu_items-for-today")
    @Cacheable("menu_items")
    @Operation(summary = "Get actual menu_items by restaurant_id")
    public List<MenuItemTo> getByRestaurantId(@PathVariable(name = "restaurant_id")
                                              @Parameter(description = "id of restaurant by which menus is searched") int restaurantId) {
        log.info("get menuItems by restaurant_id={}", restaurantId);
        return repository.getAllByRestaurantId(restaurantId, LocalDate.now())
                .stream()
                .map(MenuItemUtil::createTo)
                .collect(Collectors.toList());
    }
}
