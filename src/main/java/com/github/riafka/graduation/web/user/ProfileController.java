package com.github.riafka.graduation.web.user;

import com.github.riafka.graduation.model.User;
import com.github.riafka.graduation.to.UserTo;
import com.github.riafka.graduation.util.UserUtil;
import com.github.riafka.graduation.web.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static com.github.riafka.graduation.util.validation.ValidationUtil.assureIdConsistent;
import static com.github.riafka.graduation.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = ProfileController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "Profile Controller", description = "Operations with profile for user")
public class ProfileController extends AbstractUserController {
    static final String REST_URL = "/api/profile";

    @GetMapping
    @Operation(summary = "Get authenticated user")
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        return authUser.getUser();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "422", description = "User not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))})
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        super.delete(authUser.id());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "422", description = "{User validation error, User must be new}",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))})
    public ResponseEntity<User> register(@Valid @RequestBody UserTo userTo) {
        log.info("register {}", userTo);
        checkNew(userTo);
        User created = prepareAndSave(UserUtil.createNewFromTo(userTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @CachePut(value = "users", key = "#authUser.username")
    @Operation(summary = "Update authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User updated",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "422", description = "User validation error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))})
    public User update(@RequestBody @Valid UserTo userTo, @AuthenticationPrincipal AuthUser authUser) {
        assureIdConsistent(userTo, authUser.id());
        User user = authUser.getUser();
        return prepareAndSave(UserUtil.updateFromTo(user, userTo));
    }
}