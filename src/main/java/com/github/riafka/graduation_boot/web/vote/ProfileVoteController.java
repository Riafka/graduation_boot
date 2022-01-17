package com.github.riafka.graduation_boot.web.vote;

import com.github.riafka.graduation_boot.error.IllegalRequestDataException;
import com.github.riafka.graduation_boot.model.Restaurant;
import com.github.riafka.graduation_boot.model.Vote;
import com.github.riafka.graduation_boot.repository.RestaurantRepository;
import com.github.riafka.graduation_boot.repository.UserRepository;
import com.github.riafka.graduation_boot.repository.VoteRepository;
import com.github.riafka.graduation_boot.to.VoteTo;
import com.github.riafka.graduation_boot.util.VoteUtil;
import com.github.riafka.graduation_boot.web.AuthUser;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static com.github.riafka.graduation_boot.util.validation.ValidationUtil.checkNotFound;
import static com.github.riafka.graduation_boot.util.validation.ValidationUtil.checkRestaurantExists;

@RestController
@RequestMapping(value = ProfileVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
@Tag(name = "Profile Vote Controller", description = "Operations with own vote")
public class ProfileVoteController {
    public static final String REST_URL = "/api/profile/vote";
    public static final String TIME_ERROR = "It is too late, vote can't be changed";
    public static final LocalTime ELEVEN_HOURS = LocalTime.of(11, 0, 0);

    private final VoteRepository voteRepository;

    private final RestaurantRepository restaurantRepository;

    private final UserRepository userRepository;

    private Clock clock;

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    @GetMapping
    @Operation(summary = "Get vote")
    public ResponseEntity<VoteTo> get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get vote for user {}", authUser.id());
        Optional<Vote> vote = voteRepository.findByUserIdAndDate(authUser.id(), LocalDate.now());
        checkNotFound(vote.isPresent(), "userId=" + authUser.id() + " voteDate=" + LocalDate.now());
        return ResponseEntity.of(VoteUtil.mapTo(vote));
    }

    @PostMapping(value = "{restaurant_id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create vote by restaurant_id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vote created",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = VoteTo.class))}),
            @ApiResponse(responseCode = "422", description = "{Restaurant not found, exception.vote.duplicateUserVoteDate}",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))})
    public ResponseEntity<VoteTo> createWithLocation(@AuthenticationPrincipal AuthUser authUser,
                                                     @PathVariable @Parameter(description = "id of the restaurant the user is voting for") Integer restaurant_id) {
        log.info("create vote User {} for restaurant {}", authUser.id(), restaurant_id);

        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurant_id);
        checkRestaurantExists(restaurant, restaurant_id);
        Vote vote = new Vote(null, userRepository.getById(authUser.id()), restaurant.get(), LocalDate.now());
        Vote created = voteRepository.save(vote);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL)
                .build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(VoteUtil.createTo(created));
    }

    @PutMapping(value = "{restaurant_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @Operation(summary = "Update vote by restaurant_id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Vote updated",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "{Restaurant not found, It is too late, vote can't be changed}",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))})
    public void update(@AuthenticationPrincipal AuthUser authUser,
                       @PathVariable @Parameter(description = "id of the restaurant the user is voting for") Integer restaurant_id) {

        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurant_id);
        checkRestaurantExists(restaurant, restaurant_id);

        Optional<Vote> vote = voteRepository.findByUserIdAndDate(authUser.id(), LocalDate.now());
        Vote voteToSave;
        if (vote.isEmpty()) {
            voteToSave = new Vote(null, authUser.getUser(), restaurant.get(), LocalDate.now());
        } else {
            if (LocalTime.now(clock).isAfter(ELEVEN_HOURS)) {
                log.info(TIME_ERROR);
                throw new IllegalRequestDataException(TIME_ERROR);
            }
            voteToSave = vote.get();
            voteToSave.setRestaurant(restaurant.get());
        }
        log.info("update vote for user {}", authUser.id());
        voteRepository.save(voteToSave);
    }
}
