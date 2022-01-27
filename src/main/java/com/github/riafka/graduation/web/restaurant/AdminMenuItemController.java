package com.github.riafka.graduation.web.restaurant;

import com.github.riafka.graduation.model.MenuItem;
import com.github.riafka.graduation.repository.MenuItemRepository;
import com.github.riafka.graduation.repository.RestaurantRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static com.github.riafka.graduation.util.validation.ValidationUtil.*;

@RestController
@RequestMapping(value = AdminMenuItemController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@Tag(name = "Admin Menu Item Controller", description = "Operations with menu items for admin")
public class AdminMenuItemController {
    static final String REST_URL = "/api/admin/restaurants";
    static final String REST_URL_RESTAURANT_ID = "/{restaurant_id}/menu_items";
    static final String REST_URL_WITHOUT_ID = "/menu_items";

    private final MenuItemRepository repository;
    private final RestaurantRepository restaurantRepository;

    @DeleteMapping(REST_URL_RESTAURANT_ID + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete menu item by restaurant_id and id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Menu item deleted"),
            @ApiResponse(responseCode = "422", description = "Menu item not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))})
    public void delete(@Parameter(description = "id of menu item to be deleted") @PathVariable int id,
                       @Parameter(description = "id of restaurant by which menu item to be deleted")
                       @PathVariable(name = "restaurant_id") int restaurantId) {
        log.info("delete menuItem by restaurant_id{} and id {}", restaurantId, id);
        repository.deleteExistedByRestaurantIdAndId(restaurantId, id);
    }

    @DeleteMapping(REST_URL_RESTAURANT_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete menu items by restaurant_id and date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Menu items deleted"),
            @ApiResponse(responseCode = "422", description = "Menu items not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))})
    public void deleteByRestaurantIdAndMenuDate(@Parameter(description = "id of restaurant by which menu items to be deleted")
                                                @PathVariable(name = "restaurant_id") int restaurantId,
                                                @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        log.info("delete menuItem by restaurant_id{} and menuDate{}", restaurantId, menuDate);
        repository.deleteExistedByRestaurantIdAndDate(restaurantId, menuDate);
    }

    @Transactional
    @PostMapping(value = REST_URL_RESTAURANT_ID, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create menu item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Menu items created",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MenuItem.class))}),
            @ApiResponse(responseCode = "422", description = "{Menu item validation error, Menu item must be new}",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))})
    public ResponseEntity<MenuItem> createWithLocation(@Valid @RequestBody MenuItem menuItem,
                                                       @PathVariable(name = "restaurant_id") int restaurantId) {
        log.info("create {}", menuItem);
        checkNew(menuItem);
        menuItem.setRestaurant(restaurantRepository.getById(restaurantId));
        MenuItem created = repository.save(menuItem);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body((created));
    }

    @Transactional
    @PutMapping(value = REST_URL_RESTAURANT_ID + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update menu item by restaurant_id and id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Menu item updated"),
            @ApiResponse(responseCode = "409", description = "Menu item doesn't belong to restaurant",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "422", description = "{Menu item validation error}",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))})
    public void update(@Valid @RequestBody MenuItem menuItem,
                       @Parameter(description = "id of menu item to be updated") @PathVariable int id,
                       @Parameter(description = "id of restaurant by which menu item to be updated")
                       @PathVariable(name = "restaurant_id") int restaurantId) {
        log.info("update {} with id={},restaurant_id={}", menuItem, id, restaurantId);
        assureIdConsistent(menuItem, id);
        repository.checkBelong(id, restaurantId);
        menuItem.setRestaurant(restaurantRepository.getById(restaurantId));
        repository.save(menuItem);
    }

    @GetMapping(REST_URL_RESTAURANT_ID + "/{id}")
    @Operation(summary = "Get menu_item by restaurant_id and id")
    public MenuItem get(
            @PathVariable(name = "restaurant_id")
            @Parameter(description = "id of restaurant by which menu is searched") int restaurantId,
            @PathVariable @Parameter(description = "id of menu to be searched") int id) {
        log.info("get menuItems by restaurant_id={}, id={}", restaurantId, id);
        return checkNotFoundOptional(repository.get(id, restaurantId), "restaurant_id=" + restaurantId + " id=" + id);
    }

    @GetMapping(REST_URL_RESTAURANT_ID)
    @Operation(summary = "Get menu_items by restaurant_id and menuDate")
    public List<MenuItem> getByRestaurantIdAndDate(
            @PathVariable(name = "restaurant_id")
            @Parameter(description = "id of restaurant by which menus is searched") int restaurantId,
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        log.info("get menuItems by restaurant_id={} and menuDate={}", restaurantId, menuDate);
        return repository.getAllByRestaurantId(restaurantId, menuDate);
    }
}
