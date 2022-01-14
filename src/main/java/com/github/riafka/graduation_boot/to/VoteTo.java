package com.github.riafka.graduation_boot.to;

import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo {
    @NotNull
    Integer userId;

    @NotNull
    Integer restaurantId;

    @NotNull
    LocalDate voteDate;

    public VoteTo(Integer id, Integer userId, Integer restaurantId, LocalDate voteDate) {
        super(id);
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.voteDate = voteDate;
    }
}
