package com.github.riafka.graduation_boot.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RestaurantMenuUtilsTest {

    @Test
    void formatPriceToString() {
        Assertions.assertEquals("100.20", RestaurantMenuUtils.formatPriceToString(10020L));
    }

    @Test
    void formatStringToPrice() {
        Assertions.assertEquals(3030L, RestaurantMenuUtils.formatStringToPrice("30.30"));
    }
}