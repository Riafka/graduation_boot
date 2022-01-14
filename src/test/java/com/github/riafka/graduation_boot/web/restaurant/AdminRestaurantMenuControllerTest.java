package com.github.riafka.graduation_boot.web.restaurant;

import com.github.riafka.graduation_boot.repository.RestaurantMenuRepository;
import com.github.riafka.graduation_boot.to.RestaurantMenuTo;
import com.github.riafka.graduation_boot.util.JsonUtil;
import com.github.riafka.graduation_boot.util.RestaurantMenuUtils;
import com.github.riafka.graduation_boot.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.github.riafka.graduation_boot.web.GlobalExceptionHandler.EXCEPTION_DUPLICATE_NAME_DATE;
import static com.github.riafka.graduation_boot.web.restaurant.RestaurantMenuTestData.*;
import static com.github.riafka.graduation_boot.web.restaurant.RestaurantTestData.BLUE_LAGOON_ID;
import static com.github.riafka.graduation_boot.web.user.UserTestData.ADMIN_MAIL;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminRestaurantMenuControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminRestaurantController.REST_URL + '/';
    @Autowired
    private RestaurantMenuRepository repository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + BLUE_LAGOON_ID + "/menus/" + SALMON_WITH_LEMON_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(repository.findById(SALMON_WITH_LEMON_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteByRestaurantId() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + BLUE_LAGOON_ID + "/menus"))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertTrue(repository.getAllByRestaurantId(BLUE_LAGOON_ID).isEmpty());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        RestaurantMenuTo newRestaurantMenu = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + BLUE_LAGOON_ID + "/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurantMenu)))
                .andDo(print())
                .andExpect(status().isCreated());

        RestaurantMenuTo created = RESTAURANT_MENU_TO_MATCHER.readFromJson(action);
        int newId = created.id();
        newRestaurantMenu.setId(newId);
        RESTAURANT_MENU_TO_MATCHER.assertMatch(created, newRestaurantMenu);
        RESTAURANT_MENU_TO_MATCHER.assertMatch(RestaurantMenuUtils.createTo(repository.getById(newId)), newRestaurantMenu);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        RestaurantMenuTo updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + BLUE_LAGOON_ID + "/menus/" + OYSTERS_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        RESTAURANT_MENU_TO_MATCHER.assertMatch(RestaurantMenuUtils.createTo(repository.getById(OYSTERS_ID)), getUpdated());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_MAIL)
    void updateDuplicate() throws Exception {
        RestaurantMenuTo invalid = getInvalid();
        perform(MockMvcRequestBuilders.put(REST_URL + BLUE_LAGOON_ID + "/menus/" + OYSTERS_ID)
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
        RestaurantMenuTo invalid = getInvalid();
        invalid.setId(null);
        perform(MockMvcRequestBuilders.post(REST_URL + BLUE_LAGOON_ID + "/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(EXCEPTION_DUPLICATE_NAME_DATE)));
    }
}