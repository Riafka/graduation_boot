package com.github.riafka.graduation_boot.web.vote;

import com.github.riafka.graduation_boot.model.Vote;
import com.github.riafka.graduation_boot.repository.VoteRepository;
import com.github.riafka.graduation_boot.to.VoteTo;
import com.github.riafka.graduation_boot.util.JsonUtil;
import com.github.riafka.graduation_boot.util.VoteUtil;
import com.github.riafka.graduation_boot.web.AbstractControllerTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static com.github.riafka.graduation_boot.web.GlobalExceptionHandler.EXCEPTION_DUPLICATE_USER_VOTE_DATE;
import static com.github.riafka.graduation_boot.web.restaurant.RestaurantTestData.KRUSTY_KRABS_ID;
import static com.github.riafka.graduation_boot.web.restaurant.RestaurantTestData.MC_DONALDS_ID;
import static com.github.riafka.graduation_boot.web.user.UserTestData.*;
import static com.github.riafka.graduation_boot.web.vote.ProfileVoteController.TIME_ERROR;
import static com.github.riafka.graduation_boot.web.vote.VoteTestData.VOTE_TO_MATCHER;
import static com.github.riafka.graduation_boot.web.vote.VoteTestData.user_vote;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileVoteControllerTest extends AbstractControllerTest {

    private static final String REST_URL = ProfileVoteController.REST_URL + '/';

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private ProfileVoteController profileVoteController;

    @Test
    @WithUserDetails(value = USER_2_MAIL)
    void create() throws Exception {
        VoteTo newVoteTo = VoteTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newVoteTo.getRestaurantId())))
                .andDo(print())
                .andExpect(status().isCreated());

        VoteTo created = VOTE_TO_MATCHER.readFromJson(action);
        int newId = created.id();
        newVoteTo.setId(newId);
        VOTE_TO_MATCHER.assertMatch(created, newVoteTo);
        VOTE_TO_MATCHER.assertMatch(VoteUtil.createTo(voteRepository.getById(newId)), newVoteTo);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateBeforeElevenHours() throws Exception {
        Clock clock = Clock.fixed(Instant.parse("2021-12-22T11:00:00.00Z"), ZoneId.of("UTC"));
        profileVoteController.setClock(clock);
        perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(MC_DONALDS_ID)))
                .andDo(print())
                .andExpect(status().isNoContent());
        Vote updated = voteRepository.findByUserIdAndDate(USER_ID, LocalDate.now()).get();
        Assertions.assertEquals(MC_DONALDS_ID, updated.getRestaurant().id());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateAfterElevenHours() throws Exception {
        Clock clock = Clock.fixed(Instant.parse("2021-12-22T12:00:00.00Z"), ZoneId.of("UTC"));
        profileVoteController.setClock(clock);
        perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(MC_DONALDS_ID)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(TIME_ERROR)));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(user_vote));
    }

    @Test
    @WithUserDetails(value = USER_2_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = USER_MAIL)
    void createDuplicate() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(KRUSTY_KRABS_ID)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(EXCEPTION_DUPLICATE_USER_VOTE_DATE)));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void createNotFound() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(NOT_FOUND)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("Restaurant with id=" + NOT_FOUND + " not found")));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateNotFound() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(NOT_FOUND)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString("Restaurant with id=" + NOT_FOUND + " not found")));
    }
}