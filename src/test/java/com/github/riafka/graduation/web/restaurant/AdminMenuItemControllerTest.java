package com.github.riafka.graduation.web.restaurant;

import com.github.riafka.graduation.model.MenuItem;
import com.github.riafka.graduation.repository.MenuItemRepository;
import com.github.riafka.graduation.util.JsonUtil;
import com.github.riafka.graduation.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.github.riafka.graduation.web.GlobalExceptionHandler.EXCEPTION_DUPLICATE_NAME_DATE;
import static com.github.riafka.graduation.web.restaurant.AdminMenuItemController.REST_URL_WITHOUT_ID;
import static com.github.riafka.graduation.web.restaurant.MenuItemTestData.*;
import static com.github.riafka.graduation.web.restaurant.RestaurantTestData.BLUE_LAGOON_ID;
import static com.github.riafka.graduation.web.user.UserTestData.ADMIN_MAIL;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminMenuItemControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminRestaurantController.REST_URL + '/';
    @Autowired
    private MenuItemRepository repository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + BLUE_LAGOON_ID + "/menu_items/" + SALMON_WITH_LEMON_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(repository.findById(SALMON_WITH_LEMON_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteByRestaurantIdAndMenuDate() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + BLUE_LAGOON_ID + REST_URL_WITHOUT_ID)
                .param("menuDate", LocalDate.now().toString()))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertTrue(repository.getAllByRestaurantId(BLUE_LAGOON_ID, LocalDate.now()).isEmpty());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        MenuItem newRestaurantMenu = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + BLUE_LAGOON_ID + REST_URL_WITHOUT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurantMenu)))
                .andDo(print())
                .andExpect(status().isCreated());

        MenuItem created = MENU_ITEM_MATCHER.readFromJson(action);
        int newId = created.id();
        newRestaurantMenu.setId(newId);
        MENU_ITEM_MATCHER.assertMatch(created, newRestaurantMenu);
        MENU_ITEM_MATCHER.assertMatch(repository.getById(newId), newRestaurantMenu);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        MenuItem updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + BLUE_LAGOON_ID + "/menu_items/" + OYSTERS_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        MENU_ITEM_MATCHER.assertMatch(repository.getById(OYSTERS_ID), getUpdated());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_MAIL)
    void updateDuplicate() throws Exception {
        MenuItem invalid = getInvalid();
        perform(MockMvcRequestBuilders.put(REST_URL + BLUE_LAGOON_ID + "/menu_items/" + OYSTERS_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(EXCEPTION_DUPLICATE_NAME_DATE)));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_MAIL)
    void createDuplicate() throws Exception {
        MenuItem invalid = getInvalid();
        invalid.setId(null);
        perform(MockMvcRequestBuilders.post(REST_URL + BLUE_LAGOON_ID + REST_URL_WITHOUT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(EXCEPTION_DUPLICATE_NAME_DATE)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + BLUE_LAGOON_ID + "/menu_items/" + RED_WINE_ID_2_DAYS_OLD))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_MATCHER.contentJson(redWineTwoDaysOld));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getByRestaurantIdAndDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + BLUE_LAGOON_ID + REST_URL_WITHOUT_ID)
                .param("menuDate", LocalDate.now().minusDays(2).toString()))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_MATCHER.contentJson(redWineTwoDaysOld, salmonWithLemonTwoDaysOld, tastyDesertTwoDaysOld));
    }
}