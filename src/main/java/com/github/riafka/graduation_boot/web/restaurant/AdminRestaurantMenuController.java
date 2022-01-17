package com.github.riafka.graduation_boot.web.restaurant;

import com.github.riafka.graduation_boot.model.RestaurantMenu;
import com.github.riafka.graduation_boot.repository.RestaurantMenuRepository;
import com.github.riafka.graduation_boot.repository.RestaurantRepository;
import com.github.riafka.graduation_boot.to.RestaurantMenuTo;
import com.github.riafka.graduation_boot.to.RestaurantTo;
import com.github.riafka.graduation_boot.util.RestaurantMenuUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static com.github.riafka.graduation_boot.util.validation.ValidationUtil.assureIdConsistent;
import static com.github.riafka.graduation_boot.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminRestaurantMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@Tag(name = "Admin Restaurant Menu Controller", description = "Operations with restaurants menu for admin")
public class AdminRestaurantMenuController {
    static final String REST_URL = "/api/admin/restaurants/{restaurant_id}/menus";

    private final RestaurantMenuRepository repository;
    private final RestaurantRepository restaurantRepository;

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete menu by restaurant_id and id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Menu deleted",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RestaurantTo.class))}),
            @ApiResponse(responseCode = "422", description = "Menu not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))})
    public void delete(@Parameter(description = "id of menu to be deleted") @PathVariable int id,
                       @Parameter(description = "id of restaurant by which menu to be deleted") @PathVariable int restaurant_id) {
        log.info("delete restaurantMenu by restaurant_id{} and id {}", restaurant_id, id);
        repository.deleteExistedByRestaurantIdAndId(restaurant_id, id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete menus by restaurant_id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Menus deleted",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RestaurantTo.class))}),
            @ApiResponse(responseCode = "422", description = "Menus not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))})
    public void deleteByRestaurantId(@Parameter(description = "id of restaurant by which menu to be deleted") @PathVariable int restaurant_id) {
        log.info("delete restaurantMenu by restaurant_id{}", restaurant_id);
        repository.deleteExistedByRestaurantId(restaurant_id);
    }

    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create menu by restaurant_id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Menu created",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RestaurantMenuTo.class))}),
            @ApiResponse(responseCode = "422", description = "{Menu validation error, Menu must be new}",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))})
    public ResponseEntity<RestaurantMenuTo> createWithLocation(@Valid @RequestBody RestaurantMenuTo restaurantMenuTo,
                                                               @Parameter(description = "id of the restaurant for which the menu will be created")
                                                               @PathVariable int restaurant_id) {
        log.info("create {}", restaurantMenuTo);
        checkNew(restaurantMenuTo);
        RestaurantMenu restaurantMenu = RestaurantMenuUtils.createNewFromTo(restaurantMenuTo);
        restaurantMenu.setRestaurant(restaurantRepository.getById(restaurant_id));
        RestaurantMenu created = repository.save(restaurantMenu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurant_id, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(RestaurantMenuUtils.createTo(created));
    }

    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update menu by restaurant_id and id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Menu updated",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "409", description = "Menu doesn't belong to restaurant",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "422", description = "Menu validation error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))})
    public void update(@Valid @RequestBody RestaurantMenuTo restaurantMenuTo,
                       @Parameter(description = "id of menu to be updated") @PathVariable int id,
                       @Parameter(description = "id of restaurant by which menu to be updated") @PathVariable int restaurant_id) {
        log.info("update {} with id={},restaurant_id={}", restaurantMenuTo, id, restaurant_id);
        assureIdConsistent(restaurantMenuTo, id);
        repository.checkBelong(id, restaurant_id);
        RestaurantMenu restaurantMenu = RestaurantMenuUtils.createNewFromTo(restaurantMenuTo);
        restaurantMenu.setRestaurant(restaurantRepository.getById(restaurant_id));
        repository.save(restaurantMenu);
    }
}
