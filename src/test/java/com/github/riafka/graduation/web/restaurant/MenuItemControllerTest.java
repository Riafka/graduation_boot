package com.github.riafka.graduation.web.restaurant;

import com.github.riafka.graduation.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.riafka.graduation.util.MenuItemUtil.createTo;
import static com.github.riafka.graduation.web.restaurant.MenuItemTestData.*;
import static com.github.riafka.graduation.web.restaurant.RestaurantTestData.BLUE_LAGOON_ID;
import static com.github.riafka.graduation.web.user.UserTestData.USER_MAIL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenuItemControllerTest extends AbstractControllerTest {
    private static final String REST_URL = RestaurantController.REST_URL + '/';

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + BLUE_LAGOON_ID + "/menu_items-for-today/" + RED_WINE_ID))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_TO_MATCHER.contentJson(createTo(redWine)));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + BLUE_LAGOON_ID + "/menu_items-for-today/" + BIG_MAC_ID))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getByRestaurantId() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + BLUE_LAGOON_ID + "/menu_items-for-today"))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_TO_MATCHER.contentJson(createTo(friedSquid),
                        createTo(oysters),
                        createTo(redWine),
                        createTo(salmonWithLemon),
                        createTo(tastyDesert)));
    }
}