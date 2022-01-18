package com.github.riafka.graduation_boot.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RestaurantMenuUtilsTest {

    @Test
    void formatPriceToString() {
        Assertions.assertEquals("1" + (char) 160 + "000,20", RestaurantMenuUtils.formatPriceToString(100020L));
    }

    @Test
    void formatStringToPrice() {
        Assertions.assertEquals(100040L, RestaurantMenuUtils.formatStringToPrice("1 000,40"));
        Assertions.assertEquals(100040L, RestaurantMenuUtils.formatStringToPrice("1 000,40"));
    }
}