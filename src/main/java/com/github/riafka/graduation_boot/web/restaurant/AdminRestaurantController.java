package com.github.riafka.graduation_boot.web.restaurant;

import com.github.riafka.graduation_boot.model.Restaurant;
import com.github.riafka.graduation_boot.repository.RestaurantRepository;
import com.github.riafka.graduation_boot.to.RestaurantTo;
import com.github.riafka.graduation_boot.util.RestaurantUtil;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static com.github.riafka.graduation_boot.util.validation.ValidationUtil.assureIdConsistent;
import static com.github.riafka.graduation_boot.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@Tag(name = "Admin Restaurant Controller", description = "Operations with restaurants for admin")
public class AdminRestaurantController {
    static final String REST_URL = "/api/admin/restaurants";

    private final RestaurantRepository repository;

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete restaurant by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Restaurant deleted",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RestaurantTo.class))}),
            @ApiResponse(responseCode = "422", description = "Restaurant not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))})
    public void delete(@Parameter(description = "id of restaurant to be deleted") @PathVariable int id) {
        log.info("delete restaurant {}", id);
        repository.deleteExisted(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurant created",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RestaurantTo.class))}),
            @ApiResponse(responseCode = "422", description = "{Restaurant validation error, Restaurant must be new}" ,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))})
    public ResponseEntity<RestaurantTo> createWithLocation(@Valid @RequestBody RestaurantTo restaurantTo) {
        log.info("create restaurant {}", restaurantTo);
        checkNew(restaurantTo);
        Restaurant created = repository.save(RestaurantUtil.createNewFromTo(restaurantTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(RestaurantController.REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(RestaurantUtil.createTo(created));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update restaurant by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Restaurant updated",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "422", description = "Restaurant validation error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))})
    public void update(@Valid @RequestBody RestaurantTo restaurantTo,
                       @PathVariable @Parameter(description = "id of restaurant to be updated") int id) {
        log.info("update {} with id={}", restaurantTo, id);
        assureIdConsistent(restaurantTo, id);
        repository.save(RestaurantUtil.createNewFromTo(restaurantTo));
    }
}
